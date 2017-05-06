package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.popularmovies.layout.MovieAdapter;
import com.example.android.popularmovies.layout.MovieDetailsActivity;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesList;
import com.example.android.popularmovies.themoviedb.MoviesResult;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private MovieDbHelper movieDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        //Set LayoutManager
        int numberOfColumns;
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            numberOfColumns = 2;
        } else{
            numberOfColumns = 3;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //Set Adapter
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        movieDbHelper = new MovieDbHelper(this);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        new fetchMoviesTask().execute();
    }

    /**
     * Open the clicked movie's details
     * @param movieSelected Movie selected by the thumbnail
     */
    @Override
    public void onClick(MoviesResult movieSelected) {
        Context context = this;
        Class detailsClass = MovieDetailsActivity.class;
        Intent intentToStartMovieDetails = new Intent(context, detailsClass);
        intentToStartMovieDetails.putExtra(Intent.EXTRA_TEXT, String.valueOf(movieSelected.getId()));
        startActivity(intentToStartMovieDetails);
    }

    /**
     * AsyncTask to fetch movies data
     */
    private class fetchMoviesTask extends AsyncTask<URL, Void, MoviesList> {

        /**
         * Tasks to do in background, like fetch movie thumbnails
         * @param params Parameters
         *
         * @return List of movies
         */
        @Override
        protected MoviesList doInBackground(URL... params) {

            return movieDbHelper.getMovies();
        }

        /**
         * Tasks to be executed when doInBackground finishes
         * @param moviesList Movies list, fetched from API
         */
        @Override
        protected void onPostExecute(MoviesList moviesList) {
            if (moviesList != null && moviesList.getTotalResults() > 0) {
                mMovieAdapter.setData(moviesList.getResults());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }
}
