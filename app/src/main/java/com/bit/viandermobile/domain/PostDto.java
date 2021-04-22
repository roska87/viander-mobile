package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class PostDto {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("file")
    private String file;
    @SerializedName("content")
    private String content;
    @SerializedName("type")
    private String type;
    @SerializedName("price")
    private Integer price;
    @SerializedName("author")
    private String author;

    public PostDto() {
    }

    public PostDto(int id, String title, String file, String content, String type, Integer price, String author) {
        this.id = id;
        this.title = title;
        this.file = file;
        this.content = content;
        this.type = type;
        this.price = price;
        this.author = author;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getDescription() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", file='" + file + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", author='" + author + '\'' +
                '}';
    }
}