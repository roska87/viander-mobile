package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class ViandCountDto {

    @SerializedName("id")
    private int id;
    @SerializedName("viand_id")
    private int viandId;
    @SerializedName("count")
    private int count;

    public ViandCountDto() {
    }

    public ViandCountDto(int id, int viand_id, int count) {
        this.id = id;
        this.viandId = viand_id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViandId() {
        return viandId;
    }

    public void setViandId(int viandId) {
        this.viandId = viandId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}