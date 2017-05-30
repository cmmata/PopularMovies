package com.example.android.popularmovies.tasks;

import com.example.android.popularmovies.themoviedb.Movie;

/**
 * Listener used by FetchMovieDetailsTask to communicate with MovieDetailsActivity
 */
public interface FetchMovieDetailsListener {
    /**
     * On movie details downloaded
     * @param moviesList List of movies
     */
    void onDownloadComplete(Movie moviesList);
}
