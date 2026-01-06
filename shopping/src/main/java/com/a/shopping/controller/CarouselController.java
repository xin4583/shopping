package com.a.shopping.controller;

import com.a.shopping.entity.Carousel;
import com.a.shopping.entity.Result;
import com.a.shopping.repository.CarouselRepository;
import com.a.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselRepository carouselRepository;
    @Autowired
    private ProductRepository productRepository;
    @PostMapping("/add")
    public Result add(
            @RequestParam("title") String title,
            @RequestParam("image") MultipartFile image,
            @RequestParam("sort") Integer sort,
            @RequestParam("productId") Long productId ) throws IOException {
        if (image.isEmpty()) {
            return Result.fail("轮播图图片不能为空");
        }
        Carousel carousel = new Carousel();
        carousel.setTitle(title);
        carousel.setImage(image.getBytes());
        carousel.setSort(sort);
        carousel.setProductId(productId);
        Carousel saved = carouselRepository.save(carousel);
        return saved != null ? Result.suc() : Result.fail("添加失败");
    }

    /**
     * 删除轮播图
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        if (!carouselRepository.existsById(id)) {
            return Result.fail("轮播图不存在");
        }
        carouselRepository.deleteById(id);
        return Result.suc();
    }

    /**
     * 更新轮播图
     */
    @PostMapping("/update")
    public Result update(
            @RequestParam("id") Long id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "sort", required = false) Integer sort,
            @RequestParam(value = "productId", required = false) Long productId
            ) throws IOException {
        Optional<Carousel> carouselOpt = carouselRepository.findById(id);
        if (carouselOpt.isEmpty()) {
            return Result.fail("轮播图不存在");
        }
        Carousel carousel = carouselOpt.get();
        if (title != null) {
            carousel.setTitle(title);
        }
        if (image != null && !image.isEmpty()) {
            carousel.setImage(image.getBytes());
        }
        if (sort != null) {
            carousel.setSort(sort);
        }
        if (productId != null) {
            carousel.setProductId(productId);
        }
        carouselRepository.save(carousel);
        return Result.suc();
    }

    /**
     * 获取所有轮播图（按排序号升序）
     */
    @GetMapping("/listAll")
    public Result listAll() {
        List<Carousel> carousels = carouselRepository.findAll();
        // 按排序号升序排列
        carousels.sort((c1, c2) -> c1.getSort().compareTo(c2.getSort()));
        return Result.suc(carousels);
    }

    /**
     * 根据ID查询轮播图
     */
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        Optional<Carousel> carousel = carouselRepository.findById(id);
        return carousel.isPresent() ? Result.suc(carousel.get()) : Result.fail("轮播图不存在");
    }
}