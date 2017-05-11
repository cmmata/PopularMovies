package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.layout.MovieAdapter;
import com.example.android.popularmovies.layout.MovieDetailsActivity;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesList;
import com.example.android.popularmovies.themoviedb.MoviesResult;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private MovieDbHelper movieDbHelper;
    private ActionBar mActionBar;
    private String mainTitle;
    private String orderSelected = MovieDbHelper.POPULAR_ORDER;
    private static final String ORDER_SELECTED_KEY = "orderSelected";
    private static final String TITLE_KEY = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(ORDER_SELECTED_KEY)) {
            orderSelected = savedInstanceState.getString(ORDER_SELECTED_KEY);
        }
        if(savedInstanceState != null && savedInstanceState.containsKey(TITLE_KEY)) {
            mainTitle = savedInstanceState.getString(TITLE_KEY);
        } else {
            mainTitle = getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.sort_popular);
        }
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
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mainTitle);
        new FetchMoviesTask().execute(orderSelected);
    }

    /**
     * Open the clicked movie's details
     * @param movieSelected Movie selected by the thumbnail
     */
    @Override
    public void onClick(MoviesResult movieSelected) {
        if (isOnline()) {
            Context context = this;
            Class detailsClass = MovieDetailsActivity.class;
            Intent intentToStartMovieDetails = new Intent(context, detailsClass);
            intentToStartMovieDetails.putExtra(Intent.EXTRA_TEXT, String.valueOf(movieSelected.getId()));
            startActivity(intentToStartMovieDetails);
        } else {
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), duration);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            String popular = getResources().getString(R.string.sort_popular);
            orderSelected = MovieDbHelper.POPULAR_ORDER;
            mainTitle = getResources().getString(R.string.app_name) + " - " + popular;
            mActionBar.setTitle(mainTitle);
            new FetchMoviesTask().execute(MovieDbHelper.POPULAR_ORDER);
            return true;
        }

        if (id == R.id.sort_rated) {
            String rated = getResources().getString(R.string.sort_rated);
            orderSelected = MovieDbHelper.RATED_ORDER;
            mainTitle = getResources().getString(R.string.app_name) + " - " + rated;
            mActionBar.setTitle(mainTitle);
            new FetchMoviesTask().execute(MovieDbHelper.RATED_ORDER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ORDER_SELECTED_KEY, orderSelected);
        outState.putString(TITLE_KEY, mainTitle);
        super.onSaveInstanceState(outState);
    }

    /**
     * Check that app has connection to internet
     *
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * AsyncTask to fetch movies data
     */
    private class FetchMoviesTask extends AsyncTask<String, Void, MoviesList> {

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
            if (isOnline()) {
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
            if (moviesList == null || moviesList.getTotalResults() <= 0) {
                mErrorMessageDisplay.setText(R.string.connection_error);
                mErrorMessageDisplay.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setData(moviesList.getResults());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            }
        }
    }
}
