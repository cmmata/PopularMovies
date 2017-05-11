package com.example.android.popularmovies.util;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Some network related functions, based on NetworkUtils from Udacity's examples.
 */
public class NetworkUtils {

    /**
     * Call The Movie Database API with the selected Uri
     * @param movieDbApiUrl API call parameter
     *
     * @return JSON with the API result
     */
    public static String getApiCallResult(Uri movieDbApiUrl) {
        URL movieDbUrl = null;
        try {
            movieDbUrl = new URL(movieDbApiUrl.toString());
        } catch (MalformedURLException e) {
            Log.e("PopularMovies", "exception", e);
        }
        String apiCallResult = null;
        try {
            apiCallResult = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
        } catch (IOException e) {
            Log.e("PopularMovies", "exception", e);
        }

        return apiCallResult;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
