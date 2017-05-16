package com.example.android.popularmovies.tasks;

import com.example.android.popularmovies.themoviedb.MoviesList;

/**
 * Listener used by FetchMoviesTask to communicate with MainActivity
 */
public interface FetchMoviesListener {
    void onDownloadComplete(MoviesList moviesList);
}
