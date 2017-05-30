package com.example.android.popularmovies.themoviedb;

import android.database.Cursor;

import com.example.android.popularmovies.data.MovieContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A movie's genre. Generated with <a href="http://www.jsonschema2pojo.org/">this online tool</a>
 */
public class Movie {

  @SerializedName("adult")
  @Expose
    private Boolean adult;
  @SerializedName("backdrop_path")
  @Expose
    private String backdropPath;
  @SerializedName("belongs_to_collection")
  @Expose
    private Object belongsToCollection;
  @SerializedName("budget")
  @Expose
    private Integer budget;
  @SerializedName("genres")
  @Expose
    private List<Genre> genres = null;
  @SerializedName("homepage")
  @Expose
    private String homepage;
  @SerializedName("id")
  @Expose
    private Integer id;
  @SerializedName("imdb_id")
  @Expose
    private String imdbId;
  @SerializedName("original_language")
  @Expose
    private String originalLanguage;
  @SerializedName("original_title")
  @Expose
    private String originalTitle;
  @SerializedName("overview")
  @Expose
    private String overview;
  @SerializedName("popularity")
  @Expose
    private Double popularity;
  @SerializedName("poster_path")
  @Expose
    private String posterPath;
  @SerializedName("production_companies")
  @Expose
    private List<ProductionCompany> productionCompanies = null;
  @SerializedName("production_countries")
  @Expose
    private List<ProductionCountry> productionCountries = null;
  @SerializedName("release_date")
  @Expose
    private String releaseDate;
  @SerializedName("revenue")
  @Expose
    private Integer revenue;
  @SerializedName("runtime")
  @Expose
    private Integer runtime;
  @SerializedName("spoken_languages")
  @Expose
    private List<SpokenLanguage> spokenLanguages = null;
  @SerializedName("status")
  @Expose
    private String status;
  @SerializedName("tagline")
  @Expose
    private String tagline;
  @SerializedName("title")
  @Expose
    private String title;
  @SerializedName("video")
  @Expose
    private Boolean video;
  @SerializedName("vote_average")
  @Expose
    private Double voteAverage;
  @SerializedName("vote_count")
  @Expose
    private Integer voteCount;

    private Videos trailers;

    private Reviews reviews;

    private static String THUMB_URL = "http://image.tmdb.org/t/p/w342/";

    private boolean favorite;

    /**
     * Constructor with movie ID
     * @param movieId Movie ID
     */
    public Movie(Integer movieId) {
        this.id = movieId;
        this.favorite = false;
    }

    /**
     * Constructor with database data
     * @param movieData Movie data from database
     */
    public Movie(Cursor movieData) {
        if (movieData.getCount() > 0) {
            this.id = movieData.getInt(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
            this.title = movieData.getString(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String genre = movieData.getString(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENRE));
            this.genres = new ArrayList<>();
            for (String item : Arrays.asList(genre.split("\\s*,\\s*"))) {
                Genre genreTemp = new Genre();
                genreTemp.setName(item);
                this.genres.add(genreTemp);
            }
            this.releaseDate = movieData.getString(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            this.overview = movieData.getString(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_SYNOPSIS));
            this.voteAverage = movieData.getDouble(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATE));
            String urlPath = movieData.getString(movieData.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            String[] posterParts = urlPath.split("/");
            this.posterPath = posterParts[posterParts.length - 1];
            this.favorite = true;
        }
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getGenresString() {
        StringBuilder genresReturn = new StringBuilder();
        String sep = "";
        if (null != this.getGenres() && !this.getGenres().isEmpty()) {
            for (Genre genre : this.getGenres()) {
                genresReturn.append(sep);
                genresReturn.append(genre.getName());
                sep = ", ";
            }
        }

        return genresReturn.toString();
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return THUMB_URL + posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Videos getTrailers() {
        return trailers;
    }

    public void setTrailers(Videos trailers) {
        this.trailers = trailers;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
