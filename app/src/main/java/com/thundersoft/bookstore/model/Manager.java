package com.thundersoft.bookstore.model;

import org.litepal.crud.DataSupport;

public class Manager extends DataSupport {

    private int id;
    private int managerImage;
    private String managerName;
    private String managerIntroduce;
    private String managerAccount;
    private String managerPassword;
    private String managerEmail;

    public Manager() {
        super();
    }

    public Manager(String managerAccount, String managerPassword, String managerEmail) {
        this.managerAccount = managerAccount;
        this.managerPassword = managerPassword;
        this.managerEmail = managerEmail;
    }



    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManagerAccount() {
        return managerAccount;
    }

    public void setManagerAccount(String managerAccount) {
        this.managerAccount = managerAccount;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public int getManagerImage() {
        return managerImage;
    }

    public void setManagerImage(int managerImage) {
        this.managerImage = managerImage;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerIntroduce() {
        return managerIntroduce;
    }

    public void setManagerIntroduce(String managerIntroduce) {
        this.managerIntroduce = managerIntroduce;
    }
}
