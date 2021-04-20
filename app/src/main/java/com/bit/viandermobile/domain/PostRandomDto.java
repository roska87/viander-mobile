package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostRandomDto {

    @SerializedName("results")
    private List<PostDto> results = new ArrayList<>();

    public PostRandomDto() {
    }

    public PostRandomDto(List<PostDto> results) {
        this.results = results;
    }

    public List<PostDto> getResults() {
        return results;
    }

    public void setResults(List<PostDto> results) {
        this.results = results;
    }
}