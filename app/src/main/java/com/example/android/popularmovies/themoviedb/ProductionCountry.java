package com.example.android.popularmovies.themoviedb;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A movie's production country. Generated with <a href="http://www.jsonschema2pojo.org/">this online tool</a>
 */
class ProductionCountry {

    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
