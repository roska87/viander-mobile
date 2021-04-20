package com.bit.viandermobile.models;

public class ViandMenuViewModel {

    private String title;
    private String image;
    private String day;
    private int price;
    private boolean checked;

    public ViandMenuViewModel() {
    }

    public ViandMenuViewModel(String title, String image, String day, int price, boolean checked) {
        this.title = title;
        this.image = image;
        this.day = day;
        this.price = price;
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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