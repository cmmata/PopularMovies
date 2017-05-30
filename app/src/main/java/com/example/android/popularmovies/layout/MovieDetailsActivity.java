package com.example.android.popularmovies.layout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.tasks.FetchMovieDetailsListener;
import com.example.android.popularmovies.tasks.FetchMovieDetailsTask;
import com.example.android.popularmovies.themoviedb.Movie;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.Reviews;
import com.example.android.popularmovies.themoviedb.Videos;
import com.example.android.popularmovies.themoviedb.VideosResult;
import com.squareup.picasso.Picasso;

/**
 * Details view
 */
public class MovieDetailsActivity extends AppCompatActivity implements VideoAdapterOnClickHandler, FetchMovieDetailsListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView mMoviePoster;
    private int mMovieId;
    private TextView mMovieTitle;
    private TextView mMovieDate;
    private TextView mMovieRate;
    private TextView mMovieGenre;
    private TextView mMovieSynopsis;
    private Button mMarkAsFavorite;
    private MovieDbHelper movieDbHelper;
    private Context context;
    private RecyclerView mRecyclerViewVideos;
    private VideoAdapter mVideoAdapter;
    private RecyclerView mRecyclerViewReviews;
    private ReviewAdapter mReviewAdapter;
    private Movie movieSelected;
    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 1;

    /**
     * Create a details view
     * @param savedInstanceState App's instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        context = this;

        mMoviePoster = (ImageView) findViewById(R.id.detail_movie_thumb);
        mMovieTitle = (TextView) findViewById(R.id.detail_movie_title);
        mMovieDate = (TextView) findViewById(R.id.detail_movie_date);
        mMovieRate = (TextView) findViewById(R.id.detail_movie_rate);
        mMovieGenre = (TextView) findViewById(R.id.detail_movie_genre);
        mMovieSynopsis = (TextView) findViewById(R.id.detail_movie_synopsis);
        mMarkAsFavorite = (Button) findViewById(R.id.detail_movie_favorite);
        initializeVideos();
        initializeReviews();
        Intent fatherIntent = getIntent();
        movieDbHelper = new MovieDbHelper(this);
        if (fatherIntent != null && fatherIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieId = Integer.valueOf(fatherIntent.getStringExtra(Intent.EXTRA_TEXT));
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    /**
     * Updates the movieSelected trailers
     * @param movieDetails Details
     */
    @Override
    public void onDownloadComplete(Movie movieDetails) {
        if (movieDetails != null) {
            mMovieTitle.setText(movieDetails.getTitle());
            mMovieDate.setText(movieDetails.getReleaseDate());
            mMovieGenre.setText(movieDetails.getGenresString());
            mMovieRate.setText(getResources().getString(R.string.rating, movieDetails.getVoteAverage()));
            mMovieSynopsis.setText(movieDetails.getOverview());
            Log.d("Movies", movieDetails.getPosterPath());
            Picasso.with(context)
                    .load(movieDetails.getPosterPath())
                    .into(mMoviePoster, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            mMoviePoster.setImageResource(android.R.drawable.gallery_thumb);
                        }
                    });
            Videos trailers = movieDetails.getTrailers();
            mVideoAdapter.setData(trailers.getResults());
            Reviews reviews = movieDetails.getReviews();
            mReviewAdapter.setData(reviews.getResults());
            movieSelected = movieDetails;
        }
    }

    /**
     * VideoClicked
     * @param videoSelected trailer selected
     */
    @Override
    public void onVideoClick(VideosResult videoSelected) {
        String videoSite = videoSelected.getSite();
        String videoKey = videoSelected.getKey();
        if (videoSite.equals(getString(R.string.youtube_video))) {
            Uri youtubeUri = Uri.parse("http://www.youtube.com/watch?v=" + videoKey);
            Intent openYoutube = new Intent(Intent.ACTION_VIEW, youtubeUri);
            startActivity(openYoutube);
        }
    }

    /**
     * Load movieSelected details, via database or API
     * @param data Movie data, if found in database
     */
    private void loadMovieData(Cursor data) {

        if (null != data && data.getCount() > 0) {
            data.moveToFirst();
            movieSelected = new Movie(data);
            buttonFavorite(true);
        } else {
            movieSelected = new Movie(mMovieId);
            buttonFavorite(false);
        }
        new FetchMovieDetailsTask(this, movieDbHelper).execute(movieSelected);
    }

    /**
     * Initializes videos recyclerview and associated components
     */
    private void initializeVideos() {
        mRecyclerViewVideos = (RecyclerView) findViewById(R.id.recyclerview_videos);
        LinearLayoutManager videosManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewVideos.setLayoutManager(videosManager);
        mRecyclerViewVideos.setHasFixedSize(true);
        mVideoAdapter = new VideoAdapter(this);
        mRecyclerViewVideos.setAdapter(mVideoAdapter);
    }

    /**
     * Initializes reviews recyclerview and associated components
     */
    private void initializeReviews() {
        mRecyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerview_reviews);
        LinearLayoutManager reviewsManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewReviews.setLayoutManager(reviewsManager);
        mRecyclerViewReviews.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mRecyclerViewReviews.setAdapter(mReviewAdapter);
    }

    /**
     * Button Mark as favorite pressed
     * @param view View
     */
    public void markFavorite(View view) {
        if (movieSelected.isFavorite()) {
            String selection = MovieContract.MovieEntry.COLUMN_ID + "=?";
            String [] args = {Integer.toString(mMovieId)};
            getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, args);
        } else {
            ContentValues cv = new ContentValues();
            cv.put(MovieContract.MovieEntry.COLUMN_ID, movieSelected.getId());
            cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movieSelected.getTitle());
            cv.put(MovieContract.MovieEntry.COLUMN_GENRE, movieSelected.getGenresString());
            cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieSelected.getReleaseDate());
            cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movieSelected.getOverview());
            cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieSelected.getPosterPath());
            cv.put(MovieContract.MovieEntry.COLUMN_USER_RATE, movieSelected.getVoteAverage());
            getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,cv);
        }
        buttonFavorite(!movieSelected.isFavorite());
    }

    /**
     * Sets the button status (favorited or not)
     * @param active Favorite movie
     */
    private void buttonFavorite(boolean active) {
        if (active) {
            movieSelected.setFavorite(true);
            mMarkAsFavorite.setText(R.string.remove_favorite);
            mMarkAsFavorite.setBackgroundResource(R.color.colorFavoriteOff);
        } else {
            movieSelected.setFavorite(false);
            mMarkAsFavorite.setText(R.string.mark_favorite);
            mMarkAsFavorite.setBackgroundResource(R.color.colorFavoriteOn);
        }
    }

    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mVideoData = null;

            // onStartLoading() is called when a loader first starts loading data
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

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                String selection = MovieContract.MovieEntry.COLUMN_ID + "=?";
                String[] args = {Integer.toString(mMovieId)};

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

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mVideoData = data;
                super.deliverResult(data);
            }
        };

    }

    /**
     * Finished loading movie data
     * @param loader Database Loader
     * @param data   Cursor with movie's data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loadMovieData(data);
    }

    /**
     * When loader resets
     * @param loader Database Loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loadMovieData(null);
    }
}
