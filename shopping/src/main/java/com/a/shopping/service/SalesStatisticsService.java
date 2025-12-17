package com.a.shopping.service;

import com.a.shopping.entity.*;
import com.a.shopping.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class SalesStatisticsService {
    @Autowired
    private SalesStatisticsRepository statisticsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ShopRepository shopRepository;

    /**
     * 订单支付后更新统计数据
     */
    @Transactional
    public void updateStatistics(Order order) {
        LocalDate today = LocalDate.now();
        Product product = order.getProduct();
        Shop shop = order.getShop();
        BigDecimal payAmount = order.getPayAmount(); // 订单实付金额

        // 1. 更新商品维度统计（按商品+店铺+日期）
        Optional<SalesStatistics> productStatOpt = statisticsRepository
                .findByStatDateAndProductIdAndShopId(today, product.getId(), shop.getId());
        if (productStatOpt.isPresent()) {
            SalesStatistics stat = productStatOpt.get();
            stat.setDailyAmount(stat.getDailyAmount().add(payAmount)); // 累加当日营业额
            stat.setTotalAmount(stat.getTotalAmount().add(payAmount)); // 累加总营业额
            statisticsRepository.save(stat);
        } else {
            SalesStatistics newStat = new SalesStatistics();
            newStat.setStatDate(today);
            newStat.setProduct(product);
            newStat.setShop(shop);
            newStat.setDailyAmount(payAmount);
            // 总营业额 = 历史总营业额 + 本次金额（查询历史累计值）
            BigDecimal historyTotal = statisticsRepository
                    .sumTotalByProductIdAndShopIdBeforeDate(product.getId(), shop.getId(), today)
                    .orElse(BigDecimal.ZERO);
            newStat.setTotalAmount(historyTotal.add(payAmount));
            statisticsRepository.save(newStat);
        }

        // 2. 更新店铺维度统计（按店铺+日期，商品为null）
        Optional<SalesStatistics> shopStatOpt = statisticsRepository
                .findByStatDateAndProductIsNullAndShopId(today, shop.getId());
        if (shopStatOpt.isPresent()) {
            SalesStatistics stat = shopStatOpt.get();
            stat.setDailyAmount(stat.getDailyAmount().add(payAmount));
            stat.setTotalAmount(stat.getTotalAmount().add(payAmount));
            statisticsRepository.save(stat);
        } else {
            SalesStatistics newStat = new SalesStatistics();
            newStat.setStatDate(today);
            newStat.setShop(shop);
            newStat.setDailyAmount(payAmount);
            // 店铺总营业额 = 历史总营业额 + 本次金额
            BigDecimal historyTotal = statisticsRepository
                    .sumTotalByShopIdBeforeDateAndProductIsNull(shop.getId(), today)
                    .orElse(BigDecimal.ZERO);
            newStat.setTotalAmount(historyTotal.add(payAmount));
            statisticsRepository.save(newStat);
        }
    }
}