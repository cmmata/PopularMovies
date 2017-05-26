package com.example.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * SQLite contract for Movies Entity
 */
public class MovieContract {
    public static final String AUTHORITY = "com.example.android.popularmovies.movieslist";
    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    // Define the possible paths for accessing data in this contract
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_USER_RATE = "userRate";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_POSTER_PATH = "posterPath";
    }
}
