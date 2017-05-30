package com.example.android.popularmovies;

import android.content.SharedPreferences;
import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.layout.MovieAdapter;
import com.example.android.popularmovies.layout.MovieDetailsActivity;
import com.example.android.popularmovies.tasks.FetchMoviesListener;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesList;
import com.example.android.popularmovies.themoviedb.MoviesResult;
import com.example.android.popularmovies.tasks.FetchMoviesTask;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, FetchMoviesListener, LoaderManager.LoaderCallbacks<Cursor> {

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
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

    /**
     * On create Main activity
     * @param savedInstanceState App's state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(ORDER_SELECTED_KEY)) {
                orderSelected = savedInstanceState.getString(ORDER_SELECTED_KEY);
            }
            if(savedInstanceState.containsKey(TITLE_KEY)) {
                mainTitle = savedInstanceState.getString(TITLE_KEY);
            }
        } else {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            if (null != pref.getString(ORDER_SELECTED_KEY, null) && null != pref.getString(TITLE_KEY, null)) {
                orderSelected = pref.getString(ORDER_SELECTED_KEY, null);
                mainTitle = pref.getString(TITLE_KEY, null);
            } else {
                mainTitle = getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.sort_popular);
            }
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
        if (null != mActionBar) {
            mActionBar.setTitle(mainTitle);
        }
        if (orderSelected.equals(MovieDbHelper.FAVORITES_ORDER)) {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            new FetchMoviesTask(this, movieDbHelper, isOnline()).execute(orderSelected);
        }
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

    /**
     * Create menu options
     * @param menu App menu
     *
     * @return menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Select a menu option
     * @param item Menu item
     *
     * @return menu item selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            String popular = getResources().getString(R.string.sort_popular);
            orderSelected = MovieDbHelper.POPULAR_ORDER;
            mainTitle = getResources().getString(R.string.app_name) + " - " + popular;
            mActionBar.setTitle(mainTitle);
            new FetchMoviesTask(this, movieDbHelper, isOnline()).execute(MovieDbHelper.POPULAR_ORDER);
            return true;
        } else if (id == R.id.sort_rated) {
            String rated = getResources().getString(R.string.sort_rated);
            orderSelected = MovieDbHelper.RATED_ORDER;
            mainTitle = getResources().getString(R.string.app_name) + " - " + rated;
            mActionBar.setTitle(mainTitle);
            new FetchMoviesTask(this, movieDbHelper, isOnline()).execute(MovieDbHelper.RATED_ORDER);
            return true;
        } else if (id == R.id.sort_favorites) {
            String favorites = getResources().getString(R.string.sort_favorites);
            orderSelected = MovieDbHelper.FAVORITES_ORDER;
            mainTitle = getResources().getString(R.string.app_name) + " - " + favorites;
            mActionBar.setTitle(mainTitle);
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Save state of the app
     * @param outState State
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORDER_SELECTED_KEY, orderSelected);
        outState.putString(TITLE_KEY, mainTitle);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ORDER_SELECTED_KEY, orderSelected);
        editor.putString(TITLE_KEY, mainTitle);
        editor.apply();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orderSelected = savedInstanceState.getString(ORDER_SELECTED_KEY);
        mainTitle = savedInstanceState.getString(TITLE_KEY);
    }

    /**
     * Check that app has connection to internet
     *
     * @return boolean
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Tasks to do when the background thread finishes fetching Movies data
     * @param moviesList List of movies fetched
     */
    @Override
    public void onDownloadComplete(MoviesList moviesList) {
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

    /**
     * Create Database Loader
     * @param id   ID
     * @param args Arguments
     *
     * @return Cursor with loaded data
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mVideoData = null;

            /**
             * called when a loader first starts loading data
             */
            @Override
            protected void onStartLoading() {
                if (mVideoData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mVideoData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            /**
             * Performs asynchronous loading of data
             */
            @Override
            public Cursor loadInBackground() {
                String selection = "";
                String[] args = {};

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            selection,
                            args,
                            MovieContract.MovieEntry._ID);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    return null;
                }
            }

            /**
             * Sends the result of the load, a Cursor, to the registered listener
             * @param data Cursor with returned data
             */
            public void deliverResult(Cursor data) {
                mVideoData = data;
                super.deliverResult(data);
            }
        };
    }

    /**
     * Data is loaded
     * @param loader Loader to call
     * @param data   Data retrieved
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loadMoviesList(data);
    }

    /**
     * Loader reset
     * @param loader Loader to call
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loadMoviesList(null);
    }

    /**
     * Fill Recyclerview with database data
     * @param moviesList Cursor with data
     */
    private void loadMoviesList(Cursor moviesList) {
        if (orderSelected.equals(MovieDbHelper.FAVORITES_ORDER)) {
            if (null != moviesList && moviesList.getCount() > 0){
                mRecyclerView.setVisibility(View.VISIBLE);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                mMovieAdapter.setCursorData(moviesList);
            } else{
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_favorites), duration);
                toast.show();
            }
        }
    }
}
