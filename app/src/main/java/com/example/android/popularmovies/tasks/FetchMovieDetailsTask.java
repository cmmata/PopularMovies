package com.example.android.popularmovies.tasks;

import android.os.AsyncTask;
import com.example.android.popularmovies.themoviedb.Movie;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;

/**
 * AsyncTask to fetch movie's data
 */
public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

    private FetchMovieDetailsListener listener;
    private MovieDbHelper movieDbHelper;

    /**
     * Constructor
     * @param listener Listener to talk with MovieDetailsActivity
     * @param movieDbHelper Helper to fetch MovieDatabase's API
     */
    public FetchMovieDetailsTask(FetchMovieDetailsListener listener, MovieDbHelper movieDbHelper){
        this.listener = listener;
        this.movieDbHelper = movieDbHelper;
    }
    /**
     * Tasks to do in background, like fetch movie details
     * @param params Parameters
     *
     * @return Selected movie details
     */
    @Override
    protected Movie doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String movieId = params[0];

        return movieDbHelper.getMovieDetails(movieId);
    }

    /**
     * Tasks to be executed when doInBackground finishes
     * @param movieDetails Movie details, fetched from API
     */
    @Override
    protected void onPostExecute(Movie movieDetails) {
        if(listener != null) listener.onDownloadComplete(movieDetails);

    }
}
