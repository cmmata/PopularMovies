package com.example.android.popularmovies.themoviedb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Movie's videos array list. Generated with <a href="http://www.jsonschema2pojo.org/">this online tool</a>
 */
public class Videos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideosResult> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<VideosResult> getResults() {
        return results;
    }

    public void setResults(List<VideosResult> results) {
        this.results = results;
    }
}