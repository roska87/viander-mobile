package com.bit.viandermobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ViandMenuViewModel implements Parcelable {

    private int id;
    private int dayNumber;
    private String title;
    private String content;
    private String image;
    private String day;
    private int price;
    private boolean checked;

    public ViandMenuViewModel() {
    }

    public ViandMenuViewModel(int id, int dayNumber, String title, String content, String image, String day, int price, boolean checked) {
        this.id = id;
        this.dayNumber = dayNumber;
        this.title = title;
        this.content = content;
        this.image = image;
        this.day = day;
        this.price = price;
        this.checked = checked;
    }

    protected ViandMenuViewModel(Parcel in) {
        id = in.readInt();
        dayNumber = in.readInt();
        title = in.readString();
        content = in.readString();
        image = in.readString();
        day = in.readString();
        price = in.readInt();
        checked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(dayNumber);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(day);
        dest.writeInt(price);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViandMenuViewModel> CREATOR = new Creator<ViandMenuViewModel>() {
        @Override
        public ViandMenuViewModel createFromParcel(Parcel in) {
            return new ViandMenuViewModel(in);
        }

        @Override
        public ViandMenuViewModel[] newArray(int size) {
            return new ViandMenuViewModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}