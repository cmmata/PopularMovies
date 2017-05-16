package com.example.android.popularmovies.tasks;

import com.example.android.popularmovies.themoviedb.Movie;

/**
 * Listener used by FetchMovieDetailsTask to communicate with MovieDetailsActivity
 */
public interface FetchMovieDetailsListener {
    void onDownloadComplete(Movie moviesList);
}
