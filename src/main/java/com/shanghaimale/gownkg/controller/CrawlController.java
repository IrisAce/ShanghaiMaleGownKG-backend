package com.shanghaimale.gownkg.controller;


import com.shanghaimale.gownkg.util.HistoricalCostumeCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crawl")
public class CrawlController {

    @Autowired
    private HistoricalCostumeCrawler crawler;

    /**
     * 精准爬取接口
     * 请求参数: {
     * "category": "婚礼服",  // 可选值: 婚礼服/丧礼服/社交礼服
     * "num": 20              // 爬取数量
     * }
     */
    @PostMapping("/historical-costumes")
    public List<HistoricalCostumeCrawler.CostumeImage> crawlCostumes(@RequestBody Map<String, Object> params) {
        // 参数验证
        String category = (String) params.get("category");
        if (category == null || (!"婚礼服".equals(category) && !"丧礼服".equals(category) && !"社交礼服".equals(category))) {
            throw new IllegalArgumentException("请指定有效的礼服类别: 婚礼服/丧礼服/社交礼服");
        }

        int num = params.get("num") != null ? Integer.parseInt(params.get("num").toString()) : 10;
        num = Math.min(num, 50); // 限制最大爬取数量

        // 执行爬取
        return crawler.crawlPreciseImages(category, num);
    }
}
