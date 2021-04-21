package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PostViandCountDto {

    @SerializedName("viand_ids")
    private List<Integer> viandIds;

    public PostViandCountDto() {
    }

    public PostViandCountDto(List<Integer> viandIds) {
        this.viandIds = viandIds;
    }

    public List<Integer> getViandIds() {
        return viandIds;
    }

    public void setViandIds(List<Integer> viandIds) {
        this.viandIds = viandIds;
    }

}