package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ViandCountResultDto {

    @SerializedName("results")
    private List<PostDto> results;

    public ViandCountResultDto() {
    }

    public ViandCountResultDto(List<PostDto> results) {
        this.results = results;
    }

    public List<PostDto> getResults() {
        return results;
    }

    public void setResults(List<PostDto> results) {
        this.results = results;
    }

}