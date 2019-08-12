package com.thundersoft.bookstore.model;

import org.litepal.crud.DataSupport;

public class Management extends DataSupport {

    private int id;

    private String bannerManagement;

    private String bookRecommendId;

    public int getId() {
        return id;
    }

    public String getBookRecommendId() {
        return bookRecommendId;
    }

    public void setBookRecommendId(String bookRecommendId) {
        this.bookRecommendId = bookRecommendId;
    }

    public String getBannerManagement() {
        return bannerManagement;
    }

    public void setBannerManagement(String bannerManagement) {
        this.bannerManagement = bannerManagement;
    }

}
