package com.example.android.popularmovies.themoviedb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A movie's genre. Generated with <a href="http://www.jsonschema2pojo.org/">this online tool</a>
 */
class Genre {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
