package com.example.android.popularmovies.tasks;

import com.example.android.popularmovies.themoviedb.MoviesList;

/**
 * Listener used by FetchMoviesTask to communicate with MainActivity
 */
public interface FetchMoviesListener {
    /**
     * List of movies downloaded
     * @param moviesList List of movies
     */
    void onDownloadComplete(MoviesList moviesList);
}
