package com.mobile.docktalk.models;

public class Professional {

    private int id;
    private String title;
    private String description;
    private String code;

    public Professional() {
    }

    public Professional(int id, String title, String description, String code) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
