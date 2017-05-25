package com.example.android.popularmovies.layout;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.tasks.FetchMovieDetailsListener;
import com.example.android.popularmovies.tasks.FetchMovieDetailsTask;
import com.example.android.popularmovies.themoviedb.Genre;
import com.example.android.popularmovies.themoviedb.Movie;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.Reviews;
import com.example.android.popularmovies.themoviedb.Videos;
import com.example.android.popularmovies.themoviedb.VideosResult;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler, FetchMovieDetailsListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ImageView mMoviePoster;
    private Integer mMovieId;
    private TextView mMovieTitle;
    private TextView mMovieDate;
    private TextView mMovieRate;
    private TextView mMovieGenre;
    private TextView mMovieSynopsis;
    private MovieDbHelper movieDbHelper;
    private Context context;
    private RecyclerView mRecyclerViewVideos;
    private VideoAdapter mVideoAdapter;
    private RecyclerView mRecyclerViewReviews;
    private ReviewAdapter mReviewAdapter;
    private Movie movie;
    private Intent fatherIntent;
    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;

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
        initializeVideos();
        initializeReviews();
        fatherIntent = getIntent();
        movieDbHelper = new MovieDbHelper(this);
        if (fatherIntent != null && fatherIntent.hasExtra(Intent.EXTRA_TEXT)) {
            mMovieId = Integer.valueOf(fatherIntent.getStringExtra(Intent.EXTRA_TEXT));
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    /**
     * Updates the movie trailers
     * @param movieDetails Details
     */
    @Override
    public void onDownloadComplete(Movie movieDetails) {
        if (movieDetails != null) {
            mMovieTitle.setText(movieDetails.getTitle());
            mMovieDate.setText(movieDetails.getReleaseDate());
            StringBuilder genres = new StringBuilder();
            String sep = "";
            for (Genre genre : movieDetails.getGenres()) {
                genres.append(sep);
                genres.append(genre.getName());
                sep = ", ";
            }
            mMovieGenre.setText(genres.toString());
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
     * Load movie details, via database or API
     * @param fatherIntent Intent who calls
     * @param data Movie data, if found in database
     */
    private void loadMovieData(Intent fatherIntent, Cursor data) {

        if (null != data && data.getCount() > 0) {
            movie = new Movie(data);
        } else {
            movie = new Movie(mMovieId);
        }
        new FetchMovieDetailsTask(this, movieDbHelper).execute(movie);
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
                String selection = "_id=?";
                String [] args = {mMovieId.toString()};

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            selection,
                            args,
                            MovieContract.MovieEntry._ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loadMovieData(fatherIntent, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loadMovieData(fatherIntent, null);
    }
}
