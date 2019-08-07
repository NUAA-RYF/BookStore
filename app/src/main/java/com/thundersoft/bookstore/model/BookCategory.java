package com.thundersoft.bookstore.model;

import org.litepal.crud.DataSupport;

public class BookCategory extends DataSupport {
    private int categoryId;

    private String bookCategoryName;

    private boolean isSelected;


    public BookCategory(String bookCategoryName, int categoryId,  boolean isSelected) {
        this.categoryId = categoryId;
        this.bookCategoryName = bookCategoryName;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public BookCategory() {
        super();
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getBookCategoryName() {
        return bookCategoryName;
    }

    public void setBookCategoryName(String bookCatagoryName) {
        this.bookCategoryName = bookCatagoryName;
    }
}
