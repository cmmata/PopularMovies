package com.example.android.popularmovies.themoviedb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A movie's spoken language. Generated with <a href="http://www.jsonschema2pojo.org/">this online tool</a>
 */
class SpokenLanguage {
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("name")
    @Expose
    private String name;

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
