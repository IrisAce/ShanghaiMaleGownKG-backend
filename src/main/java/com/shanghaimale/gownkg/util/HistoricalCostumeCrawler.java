package com.shanghaimale.gownkg.util;

import com.shanghaimale.gownkg.entity.CrawledImage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HistoricalCostumeCrawler {

    // 影视资源站点列表（专注历史剧的平台）
    private static final String[] MOVIE_PLATFORMS = {
            "https://image.baidu.com/search/index?tn=baiduimage&fm=result&ie=utf-8&word=",
            "https://cn.bing.com/images/search?q=",
            "https://www.douban.com/search?q=",
//            "https://movie.mtime.com/search/?q=", // 时光网
//            "https://movie.douban.com/subject_search?search_text=", // 豆瓣电影剧照专用
//           "http://find.nlc.cn/search/doSearch?query=", // 国家数字图书馆
//            "https://www.imdb.com/find?q=", // IMDb
//            "https://www.themoviedb.org/search?query=" // TMDb
    };

    // 核心筛选关键词库
    private static final String[] TIME_PERIOD_KEYWORDS = {
            "清末", "晚清", "民国", "1912-1949", "民初", "抗战时期"
    };

    private static final String[] REGION_KEYWORDS = {
            "上海", "沪上", "十里洋场", "上海滩"
    };

    private static final String[] CATEGORY_KEYWORDS = {
            "婚礼服", "丧礼服", "社交礼服", "礼服", "长袍马褂", "西装", "中山装"
    };

    private static final String[] MOVIE_TITLE_KEYWORDS = {
            "上海滩", "人间四月天", "京华烟云", "建党伟业", "辛亥革命",
            "觉醒年代", "伪装者", "潜伏", "色戒", "半生缘"
    };
    // 模拟浏览器User-Agent池
    private static final String[] USER_AGENTS = {
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.5 Safari/605.1.15",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36"
    };

    /**
     * 精准爬取历史影视剧中的男性礼服图片
     */
//    public List<CostumeImage> crawlPreciseImages(String category, int num) {
//        List<CostumeImage> result = new ArrayList<>();
//
//        // 构建多维度搜索关键词
//        String baseKeyword = buildSearchKeyword(category);
//
//        try {
//            // 遍历平台进行爬取
//            for (String platform : MOVIE_PLATFORMS) {
//                if (result.size() >= num) break;
//
//                String encodedKeyword = URLEncoder.encode(baseKeyword, StandardCharsets.UTF_8.name());
//                String url = platform + encodedKeyword;
//
//                Document doc = Jsoup.connect(url)
//                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
//                        .referrer("https://www.baidu.com/")
//                        .timeout(15000)
//                        .get();
//
//                // 根据不同平台使用不同的选择器
//                Elements imageElements = selectImagesByPlatform(platform, doc);
//
//                // 筛选并提取有效图片
//                for (Element img : imageElements) {
//                    if (result.size() >= num) break;
//
//                    String imgUrl = extractImageUrl(platform, img);
//                    String title = img.attr("alt") != null ? img.attr("alt") : "";
//
//                    // 多维度验证图片相关性
//                    if (isValidImage(imgUrl, title, category)) {
//                        CostumeImage costumeImage = new CostumeImage();
//                        costumeImage.setUrl(imgUrl);
//                        costumeImage.setTitle(title);
//                        costumeImage.setCategory(category);
//                        costumeImage.setSource(platform);
//                        costumeImage.setPeriod(identifyPeriod(title));
//
//                        result.add(costumeImage);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
    public List<CostumeImage> crawlPreciseImages(String category, int num) {
        List<CostumeImage> result = new ArrayList<>();

        // 扩充平台列表，加入新增的影视资源站点
        String[] extendedPlatforms = Arrays.copyOf(MOVIE_PLATFORMS, MOVIE_PLATFORMS.length + 5);
        extendedPlatforms[MOVIE_PLATFORMS.length] = "https://movie.mtime.com/search/"; // 时光网
//        extendedPlatforms[MOVIE_PLATFORMS.length + 1] = "https://movie.douban.com/subject_search?search_text="; // 豆瓣电影剧照
//        extendedPlatforms[MOVIE_PLATFORMS.length + 2] = "http://find.nlc.cn/search/doSearch?query="; // 中国国家数字图书馆
//        extendedPlatforms[MOVIE_PLATFORMS.length + 2] = "https://www.imdb.com/find?q="; // IMDb
//        extendedPlatforms[MOVIE_PLATFORMS.length + 3] = "https://www.themoviedb.org/search?query="; // TMDb

        // 构建多维度搜索关键词
        String baseKeyword = buildSearchKeyword(category);

        try {
            // 遍历扩充后的平台进行爬取
            for (String platform : extendedPlatforms) {
                if (result.size() >= num) break;

                String encodedKeyword = URLEncoder.encode(baseKeyword, StandardCharsets.UTF_8.name());
                String url = platform + encodedKeyword;

                // 随机选择User-Agent模拟不同浏览器
                String userAgent = USER_AGENTS[new Random().nextInt(USER_AGENTS.length)];

                Document doc = Jsoup.connect(url)
                        .userAgent(userAgent)
                        .referrer("https://www.baidu.com/") // 统一使用谷歌作为引用页
                        .timeout(20000) // 延长超时时间适应更多平台
                        .get();

                // 根据不同平台使用不同的选择器
                Elements imageElements = selectImagesByPlatform(platform, doc);

                // 筛选并提取有效图片
                for (Element img : imageElements) {
                    if (result.size() >= num) break;

                    String imgUrl = extractImageUrl(platform, img);
                    String title = extractTitleByPlatform(platform, img); // 新增平台标题提取方法

                    // 多维度验证图片相关性
                    if (isValidImage(imgUrl, title, category)) {
                        CostumeImage costumeImage = new CostumeImage();
                        costumeImage.setUrl(imgUrl);
                        costumeImage.setTitle(title);
                        costumeImage.setCategory(category);
                        costumeImage.setSource(platform);
                        costumeImage.setPeriod(identifyPeriod(title));

                        result.add(costumeImage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据平台选择合适的图片选择器（扩充版）
     */
    private Elements selectImagesByPlatform(String platform, Document doc) {
        if (platform.contains("baidu")) {
            return doc.select("img[data-src], img[src]");
        } else if (platform.contains("bing")) {
            return doc.select("img.mimg, img[src^='https://tse']");
        } else if (platform.contains("douban.com/search")) { // 原豆瓣搜索
            return doc.select("img[src^='https://img9.doubanio.com']");
        } else if (platform.contains("movie.douban.com")) { // 豆瓣电影剧照
            return doc.select("img[src^='https://img2.doubanio.com/view/photo/s_ratio_poster/public']");
        } else if (platform.contains("mtime.com")) { // 时光网
            return doc.select("div.img-box img, img[class^='lazy-image']");
        } else if (platform.contains("nlc.cn")) { // 国家数字图书馆
            return doc.select("img[src^='/ufile/']");
        } else if (platform.contains("imdb.com")) { // IMDb
            return doc.select("img[class*='ipc-image']");
        } else if (platform.contains("themoviedb.org")) { // TMDb
            return doc.select("img[class*='poster']");
        }
        return doc.select("img");
    }

    /**
     * 从元素中提取高质量图片URL（扩充版）
     */
    private String extractImageUrl(String platform, Element img) {
        if (platform.contains("baidu")) {
            return img.attr("data-src") != null && !img.attr("data-src").isEmpty()
                    ? img.attr("data-src")
                    : img.attr("src");
        } else if (platform.contains("bing")) {
            return img.attr("src");
        } else if (platform.contains("douban.com/search")) {
            return img.attr("src").replace("/m/", "/l/");
        } else if (platform.contains("movie.douban.com")) { // 豆瓣电影剧照
            return img.attr("src").replace("s_ratio_poster", "l_ratio_poster"); // 高清剧照
        } else if (platform.contains("mtime.com")) { // 时光网
            String originalUrl = img.attr("data-original") != null && !img.attr("data-original").isEmpty()
                    ? img.attr("data-original")
                    : img.attr("src");
            return originalUrl.replace("_180x240", ""); // 去除缩略图标识
        } else if (platform.contains("nlc.cn")) { // 国家数字图书馆
            return "http://find.nlc.cn" + img.attr("src"); // 补全相对路径
        } else if (platform.contains("imdb.com")) { // IMDb
            String src = img.attr("src");
            // 替换尺寸参数为原始大小
            return src.contains("._V1_") ? src.replace("._V1_", "._V1_QL75_UX600") : src;
        } else if (platform.contains("themoviedb.org")) { // TMDb
            return "https://image.tmdb.org/t/p/original" + img.attr("src"); // 拼接原始图片路径
        }
        return img.attr("src");
    }

    /**
     * 针对不同平台提取图片标题
     */
    private String extractTitleByPlatform(String platform, Element img) {
        if (platform.contains("movie.douban.com")) {
            return img.attr("alt") != null ? img.attr("alt") : img.parent().text();
        } else if (platform.contains("mtime.com")) {
            return img.attr("title") != null ? img.attr("title") : img.parent().attr("title");
        } else if (platform.contains("nlc.cn")) {
            return img.attr("alt") != null ? img.attr("alt") : img.nextElementSibling().text();
        } else if (platform.contains("imdb.com") || platform.contains("themoviedb.org")) {
            return img.attr("alt") != null ? img.attr("alt") : "";
        }
        // 默认使用alt属性
        return img.attr("alt") != null ? img.attr("alt") : "";
    }


    /**
     * 根据来源爬取图片
     */
    public List<CrawledImage> crawlImages(String keyword, int num, Source source) throws IOException {
        switch (source) {
//            case BAIDU:
//                return crawlBaiduImages(keyword, num);
//            case BING:
//                return crawlBingImages(keyword, num);
            case PX500:
                return crawl500pxImages(keyword, num);
            default:
                throw new IllegalArgumentException("不支持的爬取来源");
        }
    }

    /**
     * 爬取500px网站图片（视觉中国）
     */
    public List<CrawledImage> crawl500pxImages(String keyword, int num) throws IOException {
        List<CrawledImage> images = new ArrayList<>();
        // 构建500px搜索URL，增加时期和地域限定词
        String fullKeyword = keyword + " 清末 民国 上海 男性 历史照片";
        String encodedKeyword = URLEncoder.encode(fullKeyword, "UTF-8");
        String url = "https://500px.com/search?query=" + encodedKeyword + "&type=photos";

        // 随机选择User-Agent
        String userAgent = USER_AGENTS[new Random().nextInt(USER_AGENTS.length)];

        // 500px使用JavaScript动态加载，需要特殊处理
        Document doc = Jsoup.connect(url)
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                .header("Referer", "https://500px.com/")
                .timeout(15000)
                .get();

        // 500px的图片数据在script标签中以JSON形式存在
        Elements scriptElements = doc.select("script[type='application/json']");
        for (Element script : scriptElements) {
            String scriptData = script.data();
            if (scriptData.contains("photo_stream")) {
                // 使用正则提取图片URL和相关信息
                Pattern pattern = Pattern.compile("\"image_url\":\"(https://[^\\\"]+)\"");
                Matcher matcher = pattern.matcher(scriptData);

                Pattern titlePattern = Pattern.compile("\"title\":\"([^\"]+)\"");
                Matcher titleMatcher = titlePattern.matcher(scriptData);

                List<String> urls = new ArrayList<>();
                List<String> titles = new ArrayList<>();

                while (matcher.find() && urls.size() < num) {
                    urls.add(matcher.group(1).replace("\\/", "/"));
                }

                while (titleMatcher.find() && titles.size() < num) {
                    titles.add(titleMatcher.group(1));
                }

                // 组装图片信息
                for (int i = 0; i < urls.size() && i < num; i++) {
                    String imgUrl = urls.get(i);
                    // 过滤掉广告和无关图片
                    if (isValidHistoricalImage(imgUrl, titles.get(i), keyword)) {
                        CrawledImage image = new CrawledImage();
                        image.setKeyword(keyword);
                        image.setImageUrl(imgUrl);
                        image.setSource("500px");
                        image.setTitle(titles.get(i));
                        image.setPeriod(determinePeriod(titles.get(i)));
                        image.setCrawlTime(System.currentTimeMillis());
                        images.add(image);
                    }
                }
                break;
            }
        }

        // 如果第一次爬取数量不足，尝试加载更多（模拟滚动加载）
        if (images.size() < num) {
            // 500px的分页加载逻辑
            String nextPageUrl = url + "&page=2";
            Document nextDoc = Jsoup.connect(nextPageUrl)
                    .userAgent(userAgent)
                    .timeout(15000)
                    .get();

            // 重复解析逻辑（实际实现中可封装为方法）
            Elements nextScriptElements = nextDoc.select("script[type='application/json']");
            for (Element script : nextScriptElements) {
                String scriptData = script.data();
                if (scriptData.contains("photo_stream")) {
                    Pattern pattern = Pattern.compile("\"image_url\":\"(https://[^\\\"]+)\"");
                    Matcher matcher = pattern.matcher(scriptData);

                    Pattern titlePattern = Pattern.compile("\"title\":\"([^\"]+)\"");
                    Matcher titleMatcher = titlePattern.matcher(scriptData);

                    List<String> urls = new ArrayList<>();
                    List<String> titles = new ArrayList<>();

                    while (matcher.find() && urls.size() < (num - images.size())) {
                        urls.add(matcher.group(1).replace("\\/", "/"));
                    }

                    while (titleMatcher.find() && titles.size() < (num - images.size())) {
                        titles.add(titleMatcher.group(1));
                    }

                    for (int i = 0; i < urls.size() && images.size() < num; i++) {
                        String imgUrl = urls.get(i);
                        if (isValidHistoricalImage(imgUrl, titles.get(i), keyword)) {
                            CrawledImage image = new CrawledImage();
                            image.setKeyword(keyword);
                            image.setImageUrl(imgUrl);
                            image.setSource("500px");
                            image.setTitle(titles.get(i));
                            image.setPeriod(determinePeriod(titles.get(i)));
                            image.setCrawlTime(System.currentTimeMillis());
                            images.add(image);
                        }
                    }
                    break;
                }
            }
        }

        return images;
    }


    /**
     * 验证图片是否为有效的历史服饰图片
     */
    private boolean isValidHistoricalImage(String url, String title, String keyword) {
        // 过滤掉现代摄影作品和明显不相关的图片
        String lowerTitle = title.toLowerCase();
        String lowerKeyword = keyword.toLowerCase();

        // 必须包含核心关键词
        if (!lowerTitle.contains(lowerKeyword) &&
                !lowerTitle.contains("晚清") &&
                !lowerTitle.contains("民国") &&
                !lowerTitle.contains("上海")) {
            return false;
        }

        // 排除现代相关词汇
        String[] modernTerms = {"现代", "当代", "时尚", "新款", "cosplay", "影视道具"};
        for (String term : modernTerms) {
            if (lowerTitle.contains(term)) {
                return false;
            }
        }

        // 验证图片URL是否为有效图片格式
        return url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png");
    }

    /**
     * 确定图片所属历史时期
     */
    private String determinePeriod(String title) {
        String lowerTitle = title.toLowerCase();
        if (lowerTitle.contains("晚清") || lowerTitle.contains("1843") || lowerTitle.contains("1911")) {
            return "1843-1911（清末）";
        } else if (lowerTitle.contains("民初") || lowerTitle.contains("1912") || lowerTitle.contains("1927")) {
            return "1912-1927（民初）";
        } else if (lowerTitle.contains("抗战") || lowerTitle.contains("1937") || lowerTitle.contains("1945")) {
            return "1937-1945（抗战）";
        } else if (lowerTitle.contains("解放前夕") || lowerTitle.contains("1945") || lowerTitle.contains("1949")) {
            return "1945-1949（解放前夕）";
        } else {
            return "民国时期"; // 默认分类
        }
    }


    /**
     * 构建多维度搜索关键词
     */
    private String buildSearchKeyword(String category) {
        // 基础关键词：类别 + 时期 + 地区 + 影视限定
        StringBuilder keyword = new StringBuilder();
        keyword.append(category)
                .append(" 男性 ")
                .append("清末民国 ")
                .append("上海 ")
                .append("影视截图 ");

        // 添加相关影视作品名增加精准度
        keyword.append(" ").append(String.join(" ", MOVIE_TITLE_KEYWORDS));

        return keyword.toString();
    }

    /**
     * 根据平台选择合适的图片选择器
     */
//    private Elements selectImagesByPlatform(String platform, Document doc) {
//        if (platform.contains("baidu")) {
//            // 百度图片列表项通常在class为imgbox的容器中
//            return doc.select(".imgbox img[data-imgurl], .imgbox img[src]");
//        } else if (platform.contains("bing")) {
//            return doc.select("img.mimg, img[src^='https://tse']");
//        } else if (platform.contains("douban")) {
//            // 匹配所有doubanio域名的图片，不限制具体服务器编号
//            return doc.select("img[src^='https://img.doubanio.com'], img[src^='https://img9.doubanio.com']");
//        }
//        return doc.select("img");
//    }

    /**
     * 从元素中提取高质量图片URL
     */
//    private String extractImageUrl(String platform, Element img) {
//        if (platform.contains("baidu")) {
//            // 优先取原图URL，无则降级取缩略图
//            String originalUrl = img.attr("data-imgurl");
//            return !originalUrl.isEmpty() ? originalUrl : img.attr("data-src");
//        } else if (platform.contains("bing")) {
//            return img.attr("src");
//        } else if (platform.contains("douban")) {
//            return img.attr("src"); // 直接使用原始URL，避免错误替换
//        }
//        return img.attr("src");
//    }

    /**
     * 多维度验证图片有效性
     */
    private boolean isValidImage(String url, String title, String category) {
        // 过滤无效URL
        if (url == null || url.isEmpty() || !url.startsWith("http") || url.contains("placeholder")) {
            return false;
        }

        // 过滤过小图片
        if (url.contains("thumbnail") || url.contains("small") || url.contains("mini")) {
            return false;
        }


        // 标题关键词验证
        String lowerTitle = title.toLowerCase();
        boolean hasTimeKeyword = false;
        boolean hasRegionKeyword = false;

        for (String kw : TIME_PERIOD_KEYWORDS) {
            if (lowerTitle.contains(kw.toLowerCase())) {
                hasTimeKeyword = true;
                break;
            }
        }

        for (String kw : REGION_KEYWORDS) {
            if (lowerTitle.contains(kw.toLowerCase())) {
                hasRegionKeyword = true;
                break;
            }
        }

        // 至少匹配一个时期关键词和地区关键词
        return hasTimeKeyword && hasRegionKeyword &&
                lowerTitle.contains(category.toLowerCase()) &&
                (lowerTitle.contains("男") || lowerTitle.contains("男性"));
    }

    /**
     * 识别图片所属历史时期
     */
    private String identifyPeriod(String title) {
        String lowerTitle = title.toLowerCase();

        if (lowerTitle.contains("清末") || lowerTitle.contains("晚清")) {
            return "1843-1911";
        } else if (lowerTitle.contains("民初") || lowerTitle.contains("1912") || lowerTitle.contains("袁世凯")) {
            return "1912-1927";
        } else if (lowerTitle.contains("抗战") || lowerTitle.contains("1937") || lowerTitle.contains("三十年代")) {
            return "1937-1945";
        } else if (lowerTitle.contains("解放") || lowerTitle.contains("1945") || lowerTitle.contains("1949")) {
            return "1945-1949";
        } else if (lowerTitle.contains("民国")) {
            return "1912-1949";
        }

        return "未知时期";
    }

    // 爬取来源枚举
    public enum Source {
        BAIDU, BING, PX500
    }

    // 内部类：存储爬取的礼服图片信息
    public static class CostumeImage {
        private String url;
        private String title;
        private String category; // 婚礼服/丧礼服/社交礼服
        private String source;
        private String period; // 历史时期
        private String thumbnailUrl;

        // Getters and Setters
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
