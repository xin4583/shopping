package com.a.shopping.controller;

import com.a.shopping.entity.Result;
import com.a.shopping.entity.SalesStatistics;
import com.a.shopping.entity.SalesStatisticsDTO;
import com.a.shopping.repository.SalesStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private SalesStatisticsRepository statisticsRepository;
    /**
     * 查询店铺下商品的每日营业额（支持日期范围）
     */
    @GetMapping("/product/daily")
    public Result getProductDailyStats(
            @RequestParam Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<SalesStatistics> stats = statisticsRepository
                .findByShopIdAndProductIsNotNullAndStatDateBetween(shopId, start, end);
        List<SalesStatisticsDTO> statsDTO = new ArrayList<>();
        for (SalesStatistics stat : stats) {
            statsDTO.add(new SalesStatisticsDTO() {{
                setId(stat.getId());
                setStatDate(stat.getStatDate());
                setProductId(stat.getProduct().getId());
                setProductName(stat.getProduct().getName());
                setShopId(shopId);
                setDailyAmount(stat.getDailyAmount());
                setTotalAmount(stat.getTotalAmount());
            }});
        }
        return Result.suc(statsDTO);
    }

    /**
     * 查询店铺的每日营业额（支持日期范围）
     */
    @GetMapping("/shop/daily")
    public Result getShopDailyStats(
            @RequestParam Long shopId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<SalesStatistics> stats = statisticsRepository
                .findByShopIdAndProductIsNullAndStatDateBetween(shopId, start, end);
        return Result.suc(stats);
    }
    /**
     * 查询店铺总营业额
     */
    @GetMapping("/shop/total")
    public Result getShopTotal(@RequestParam Long shopId) {
        // 总营业额为最新统计记录的totalAmount（商品为null的店铺维度）
        Optional<BigDecimal> latestStat = statisticsRepository.sumTotalByShopIdBeforeDateAndProductIsNull(shopId, LocalDate.now().plusDays(1));
        return Result.suc(latestStat);
    }
    /**
     * 查询店铺下每个商品的总营业额
     */
    @GetMapping("/product/total")
    public Result getProductTotalStats(@RequestParam Long shopId) {
        List<SalesStatistics> stats = statisticsRepository.findLatestProductStatsByShopId(shopId);
        List<SalesStatisticsDTO> result = new ArrayList<>();

        for (SalesStatistics stat : stats) {
            SalesStatisticsDTO dto = new SalesStatisticsDTO();
            dto.setProductId(stat.getProduct().getId());
            dto.setProductName(stat.getProduct().getName());
            dto.setShopId(shopId);
            dto.setTotalAmount(stat.getTotalAmount()); // 商品累计总营业额
            result.add(dto);
        }

        return Result.suc(result);
    }
}