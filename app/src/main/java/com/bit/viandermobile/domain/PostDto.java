package com.bit.viandermobile.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PostDto implements Parcelable {

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

    protected PostDto(Parcel in) {
        id = in.readInt();
        title = in.readString();
        file = in.readString();
        content = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        author = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(file);
        dest.writeString(content);
        dest.writeString(type);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        dest.writeString(author);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDto> CREATOR = new Creator<PostDto>() {
        @Override
        public PostDto createFromParcel(Parcel in) {
            return new PostDto(in);
        }

        @Override
        public PostDto[] newArray(int size) {
            return new PostDto[size];
        }
    };

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