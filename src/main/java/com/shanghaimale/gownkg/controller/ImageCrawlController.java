package com.shanghaimale.gownkg.controller;

import com.shanghaimale.gownkg.service.ImageCrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/image-crawl")
public class ImageCrawlController {

    @Autowired
    private ImageCrawlService imageCrawlService;

    /**
     * 图片爬取接口
     *
     * @param url      目标网页URL
     * @param selector 图片选择器（默认img）
     * @return 包含图片URL列表的响应
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crawlImages(
            @RequestParam String url,
            @RequestParam(required = false, defaultValue = "img") String selector) {

        Map<String, Object> response = new HashMap<>();
        try {
            List<String> imageUrls = imageCrawlService.crawlImages(url, selector);
            response.put("success", true);
            response.put("message", "爬取成功");
            response.put("data", imageUrls);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "爬取失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}