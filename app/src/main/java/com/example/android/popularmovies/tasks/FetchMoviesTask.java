package com.example.android.popularmovies.tasks;

import android.os.AsyncTask;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesList;

/**
 * AsyncTask to fetch movies data
 */
public class FetchMoviesTask  extends AsyncTask<String, Void, MoviesList> {

    /**
     * Listener to communicate to MainActivity
     */
    private final FetchMoviesListener listener;
    private MovieDbHelper movieDbHelper;
    private boolean isOnline;

    /**
     * Constructor
     * @param moviesListener Activity's listener
     * @param movieDbHelper  Helper to fetch MovieDatabase's API
     * @param isOnline       To check if the App has internet connection
     */
    public FetchMoviesTask (FetchMoviesListener moviesListener, MovieDbHelper movieDbHelper, boolean isOnline) {
        this.listener = moviesListener;
        this.movieDbHelper = movieDbHelper;
        this.isOnline = isOnline;
    }

    /**
     * Tasks to do in background, like fetch movie thumbnails
     * @param params Parameters
     *
     * @return List of movies
     */
    @Override
    protected MoviesList doInBackground(String... params) {
        if (params.length > 0) {
            movieDbHelper.setOrder(params[0]);
        }
        MoviesList moviesList = null;
        if (isOnline) {
            moviesList = movieDbHelper.getMovies();
        }

        return moviesList;
    }

    /**
     * Tasks to be executed when doInBackground finishes
     * @param moviesList Movies list, fetched from API
     */
    @Override
    protected void onPostExecute(MoviesList moviesList) {
        if(listener != null) listener.onDownloadComplete(moviesList);
    }
}
