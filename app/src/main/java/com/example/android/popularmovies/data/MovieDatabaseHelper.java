package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.data.MovieContract.*;

/**
 * DBHelper to work with Movie database
 */
public class MovieDatabaseHelper extends SQLiteOpenHelper {
    /**
     * The database name
     */
    private static final String DATABASE_NAME = "movies.db";
    /**
     * Database version. Updated on every schema change
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor
     * @param context Context
     */
    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create database
     * @param db Database used
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_GENRE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_USER_RATE + " REAL NOT NULL " + "); ";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    /**
     * Upgrade database
     * @param db         Database used
     * @param oldVersion Old version
     * @param newVersion New one
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
