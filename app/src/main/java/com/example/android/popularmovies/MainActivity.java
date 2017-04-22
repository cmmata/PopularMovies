package com.example.android.popularmovies;

import android.content.Context;
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
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesList;
import com.example.android.popularmovies.themoviedb.MoviesResult;

import java.net.URL;
import java.util.List;

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

    @Override
    public void onClick(MoviesResult movieSelected) {
        Context context = this;
        //TODO open details activity
    }

    public class fetchMoviesTask extends AsyncTask<URL, Void, MoviesList> {

        @Override
        protected MoviesList doInBackground(URL... params) {
            MoviesList movies = movieDbHelper.getMovies();

            return movies;
        }

        @Override
        protected void onPostExecute(MoviesList moviesList) {
            if (moviesList != null && moviesList.getTotalResults() > 0) {
                mMovieAdapter.setData(moviesList.getResults());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }
}
