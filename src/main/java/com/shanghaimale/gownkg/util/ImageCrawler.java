package com.shanghaimale.gownkg.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageCrawler {

    /**
     * 从指定URL爬取图片
     *
     * @param url      目标网页URL
     * @param selector 图片元素选择器（如img的CSS选择器）
     * @return 图片URL列表
     */
    public List<String> crawlImages(String url, String selector) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // 连接目标网页
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(10000)
                .get();

        // 选择图片元素
        Elements imageElements = document.select(selector);

        // 提取图片URL
        for (Element element : imageElements) {
            String imgUrl = element.attr("src");
            // 处理相对路径
            if (!imgUrl.startsWith("http")) {
                if (url.endsWith("/")) {
                    imgUrl = url + imgUrl;
                } else {
                    imgUrl = url + "/" + imgUrl;
                }
            }
            imageUrls.add(imgUrl);
        }

        return imageUrls;
    }
}