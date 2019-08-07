package com.thundersoft.bookstore.model;

import org.litepal.crud.DataSupport;

public class Management extends DataSupport {

    //推荐书籍管理
    private int bookManagementId[];
    //轮播图管理
    private String bannerManagement[];

    public int[] getBookManagementId() {
        return bookManagementId;
    }

    public void setBookManagementId(int[] bookManagementId) {
        this.bookManagementId = bookManagementId;
    }

    public String[] getBannerManagement() {
        return bannerManagement;
    }

    public void setBannerManagement(String[] bannerManagement) {
        this.bannerManagement = bannerManagement;
    }
}
