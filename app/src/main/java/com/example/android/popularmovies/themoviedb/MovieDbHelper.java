package com.example.android.popularmovies.themoviedb;

import android.content.Context;
import android.net.Uri;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.util.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Class to interact with The Movie DB's API
 * @see <a href="https://www.themoviedb.org/documentation/api">TheMovieDB API Documentation</a>
 */
public class MovieDbHelper {

    /**
     * API Token
     */
    private String api_token;

    /**
     * Default order when we open the App
     */
    private String default_order = "popular";

    /**
     * Selected order (popular / top_rated)
     */
    private String order;

    /**
     * URL to call
     */
    private static String API_URL = "http://api.themoviedb.org/3/movie/";

    /**
     * API Key parameter
     */
    private static String PARAM_API = "api_key";

    /**
     * Results page parameter
     */
    private static String PARAM_PAGE = "page";

    //One can set the API response language by using the parameter '&language=es'

    /**
     * Constructor
     * @param context App's context
     */
    public MovieDbHelper(Context context) {
        this.api_token = context.getString(R.string.THE_MOVIE_DB_API_TOKEN);
        this.order = this.default_order;
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
        if (order.equals("popular") || order.equals("top_rated")) {
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
     * @return movies list
     */
    public MoviesList getMovies(int pageNumber) {
        Uri movieDbApiUrl = Uri.parse(API_URL).buildUpon()
                .appendPath(order)
                .appendQueryParameter(PARAM_API, api_token)
                .appendQueryParameter(PARAM_PAGE, String.valueOf(pageNumber))
                .build();
        URL movieDbUrl = null;
        try {
            movieDbUrl = new URL(movieDbApiUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String movieResults = null;
        try {
            movieResults = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        MoviesList moviesList = gson.fromJson(movieResults, MoviesList.class);

        return moviesList;
    }
}
