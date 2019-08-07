package com.thundersoft.bookstore.model;

import org.litepal.crud.DataSupport;

public class Book extends DataSupport {
    private String bookName;
    private String bookCatalog;
    private String bookTag;
    private String bookAbstract;
    private String bookContent;
    private String imageurl;
    private String readerCount;
    private String bookOnline;
    private String bookBytime;
    private String categoryId;

    private String bookRentTime;


    private int isVisible;
    //借阅状态,0未被借阅,1申请借阅,2正在被借阅
    private int onState;


    private int bookId;
    private int imageId;

    public Book(String bookName, String bookCatalog, String bookTag,
                String bookAbstract, String bookContent, String imageurl,
                String readerCount, String bookOnline, String bookBytime,
                String categoryId, int isVisible, int onState) {
        this.bookName = bookName;
        this.bookCatalog = bookCatalog;
        this.bookTag = bookTag;
        this.bookAbstract = bookAbstract;
        this.bookContent = bookContent;
        this.imageurl = imageurl;
        this.readerCount = readerCount;
        this.bookOnline = bookOnline;
        this.bookBytime = bookBytime;
        this.categoryId = categoryId;
        this.isVisible = isVisible;
        this.onState = onState;
    }
    public int getId() {
        return bookId;
    }

    public String getBookRentTime() {
        return bookRentTime;
    }

    public void setBookRentTime(String bookRentTime) {
        this.bookRentTime = bookRentTime;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOnState() {
        return onState;
    }

    public void setOnState(int onState) {
        this.onState = onState;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBookCatalog() {
        return bookCatalog;
    }

    public void setBookCatalog(String bookCatalog) {
        this.bookCatalog = bookCatalog;
    }

    public String getBookTag() {
        return bookTag;
    }

    public void setBookTag(String bookTag) {
        this.bookTag = bookTag;
    }

    public String getBookAbstract() {
        return bookAbstract;
    }

    public void setBookAbstract(String bookAbstract) {
        this.bookAbstract = bookAbstract;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getReaderCount() {
        return readerCount;
    }

    public void setReaderCount(String readerCount) {
        this.readerCount = readerCount;
    }

    public String getBookOnline() {
        return bookOnline;
    }

    public void setBookOnline(String bookOnline) {
        this.bookOnline = bookOnline;
    }

    public String getBookBytime() {
        return bookBytime;
    }

    public void setBookBytime(String bookBytime) {
        this.bookBytime = bookBytime;
    }

    public Book() {
        super();
    }

    public Book(int imageId, String bookName, String bookContent) {
        this.imageId = imageId;
        this.bookName = bookName;
        this.bookContent = bookContent;
    }

    public Book(int imageId, String bookName) {
        this.imageId = imageId;
        this.bookName = bookName;
    }


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookContent() {
        return bookContent;
    }

    public void setBookContent(String bookContent) {
        this.bookContent = bookContent;
    }

}
