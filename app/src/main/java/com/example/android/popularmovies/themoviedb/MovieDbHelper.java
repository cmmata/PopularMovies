package com.example.android.popularmovies.themoviedb;

import android.content.Context;
import android.net.Uri;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.util.NetworkUtils;
import com.google.gson.Gson;

/**
 * Class to interact with The Movie DB's API
 * @see <a href="https://www.themoviedb.org/documentation/api">TheMovieDB API Documentation</a>
 */
public class MovieDbHelper {

    /**
    * API Token
    */
    private String apiToken;

    /**
     * Default order when we open the App
     */
    public static final String POPULAR_ORDER = "popular";

    /**
     * Order by top rated
     */
    public static final String RATED_ORDER = "top_rated";

    /**
     * Show only favorites
     */
    public static final String FAVORITES_ORDER = "favorites";

    /**
     * Selected order (popular / top_rated)
     */
    private String order;

    /**
     * URL to call
     */
    private static final String API_URL = "http://api.themoviedb.org/3/movie/";

    /**
     * API Key parameter
     */
    private static final String PARAM_API = "api_key";

    /**
     * Results page parameter
     */
    private static final String PARAM_PAGE = "page";

    /**
     * Tag to access movie's videos
     */
    private static final String VIDEO_TAG = "videos";

    /**
     * Tag to access movie's reviews
     */
    private static final String REVIEW_TAG = "reviews";

    //TODO One can set the API response language by using the parameter '&language=es'

    /**
     * Constructor
     * @param context App's context
     */
    public MovieDbHelper(Context context) {
        this.apiToken = context.getString(R.string.THE_MOVIE_DB_API_TOKEN);
        this.order = POPULAR_ORDER;
    }

    /**
     * Returns the actual order of the API search (popular / top_rated)
     * @return String
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the desired order of the API search
     * @param order Wanted order (popular / top_rated)
     */
    public void setOrder(String order) {
        if (POPULAR_ORDER.equals(order) || RATED_ORDER.equals(order)) {
            this.order = order;
        }
    }

    /**
     * List movies from the first page, sorted by order
     *
     * @return movies list
     */
    public MoviesList getMovies() {
        return getMovies(1);
    }

    /**
     * List movies from the page pageNumber, sorted by order
     * @param pageNumber Page number
     *
     * @return Movies list
     */
    public MoviesList getMovies(int pageNumber) {
        Uri movieDbApiUrl = Uri.parse(API_URL).buildUpon()
                .appendPath(order)
                .appendQueryParameter(PARAM_API, apiToken)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(pageNumber))
                .build();
        String movieResults = NetworkUtils.getApiCallResult(movieDbApiUrl);
        Gson gson = new Gson();

        return gson.fromJson(movieResults, MoviesList.class);
    }

    /**
     * Get the movie details
     * @param movie Movie's ID
     *
     * @return Movie details
     */
    public Movie getMovieDetails(Movie movie) {
        String movieId = movie.getId().toString();
        Gson gson = new Gson();
        Movie movieResult;
        if (null == movie.getTitle()) {
            Uri movieDbApiUrl = Uri.parse(API_URL).buildUpon()
                    .appendPath(movieId)
                    .appendQueryParameter(PARAM_API, apiToken)
                    .build();
            String movieDetails = NetworkUtils.getApiCallResult(movieDbApiUrl);
            movieResult = gson.fromJson(movieDetails, Movie.class);
        } else {
            movieResult = new Movie(Integer.valueOf(movieId));
        }
        //Get videos
        Uri movieDbApiUrlVideos = Uri.parse(API_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(VIDEO_TAG)
                .appendQueryParameter(PARAM_API, apiToken)
                .build();
        String movieVideos = NetworkUtils.getApiCallResult(movieDbApiUrlVideos);
        Videos trailers = gson.fromJson(movieVideos, Videos.class);
        movieResult.setTrailers(trailers);
        //Get reviews
        Uri movieDbApiUrlReviews = Uri.parse(API_URL).buildUpon()
                .appendPath(movieId)
                .appendPath(REVIEW_TAG)
                .appendQueryParameter(PARAM_API, apiToken)
                .build();
        String movieReview = NetworkUtils.getApiCallResult(movieDbApiUrlReviews);
        Reviews reviews = gson.fromJson(movieReview, Reviews.class);
        movieResult.setReviews(reviews);

        return movieResult;
    }
}
