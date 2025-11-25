package com.shanghaimale.gownkg.service;

import com.shanghaimale.gownkg.util.ImageCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ImageCrawlService {

    @Autowired
    private ImageCrawler imageCrawler;

    /**
     * 爬取图片并返回URL列表
     *
     * @param url      目标网页URL
     * @param selector 图片选择器
     * @return 图片URL列表
     */
    public List<String> crawlImages(String url, String selector) throws IOException {
        // 简单验证URL格式
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("URL格式不正确，请以http://或https://开头");
        }

        // 调用工具类爬取图片
        return imageCrawler.crawlImages(url, selector);
    }
}