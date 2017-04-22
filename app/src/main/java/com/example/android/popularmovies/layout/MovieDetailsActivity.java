package com.example.android.popularmovies.layout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.themoviedb.Movie;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private MovieDbHelper movieDbHelper;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        context = this;

        mMoviePoster = (ImageView) findViewById(R.id.detail_movie_thumb);
        mMovieTitle = (TextView) findViewById(R.id.detail_movie_title);
        Intent fatherIntent = getIntent();
        movieDbHelper = new MovieDbHelper(this);
        if (fatherIntent != null && fatherIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieId = fatherIntent.getStringExtra(Intent.EXTRA_TEXT);
            new fetchMovieDetailsTask().execute(movieId);
        }
    }

    /**
     * AsyncTask to fetch movie's data
     */
    private class fetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

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
         * @param moviesDetails Movie details, fetched from API
         */
        @Override
        protected void onPostExecute(Movie moviesDetails) {
            if (moviesDetails != null) {
                mMovieTitle.setText(moviesDetails.getTitle());
                Log.d("Movies", moviesDetails.getPosterPath());
                Picasso.with(context)
                        .load(moviesDetails.getPosterPath())
                        .into(mMoviePoster, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                mMoviePoster.setImageResource(android.R.drawable.gallery_thumb);
                            }
                        });
            }
        }
    }
}
