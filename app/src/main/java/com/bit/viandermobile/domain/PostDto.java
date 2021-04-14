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

    public PostDto() {
    }

    public PostDto(int id, String title, String file, String content, String type) {
        this.id = id;
        this.title = title;
        this.file = file;
        this.content = content;
        this.type = type;
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

    @Override
    public String toString() {
        return "PostDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}