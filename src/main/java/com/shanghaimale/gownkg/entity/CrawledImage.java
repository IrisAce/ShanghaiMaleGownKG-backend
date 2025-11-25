package com.shanghaimale.gownkg.entity;

public class CrawledImage {
    private String keyword;
    private String imgUrl;
    private String source;
    private String title;
    private String period;
    private String imageUrl;
    private Long crawlTime;

//    private String url;
//    private String title;
//    private String category; // 婚礼服/丧礼服/社交礼服
//    private String source;
//    private String period; // 历史时期
//    private String thumbnailUrl;

    public Long getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(Long crawlTime) {
        this.crawlTime = crawlTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }


//                        image.setKeyword(keyword);
//                        image.setImageUrl(imgUrl);
//                        image.setSource("500px");
//                        image.setTitle(titles.get(i));
//                        image.setPeriod(determinePeriod(titles.get(i)));
//                        image.setCrawlTime(System.currentTimeMillis());
}
