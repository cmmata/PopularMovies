package com.example.android.popularmovies.layout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.tasks.FetchMovieDetailsListener;
import com.example.android.popularmovies.tasks.FetchMovieDetailsTask;
import com.example.android.popularmovies.themoviedb.Genre;
import com.example.android.popularmovies.themoviedb.Movie;
import com.example.android.popularmovies.themoviedb.MovieDbHelper;
import com.example.android.popularmovies.themoviedb.MoviesResult;
import com.example.android.popularmovies.themoviedb.Videos;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler, FetchMovieDetailsListener {

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieDate;
    private TextView mMovieRate;
    private TextView mMovieGenre;
    private TextView mMovieSynopsis;
    private MovieDbHelper movieDbHelper;
    private Context context;
    private RecyclerView mRecyclerViewVideos;
    private VideoAdapter mVideoAdapter;

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
        mRecyclerViewVideos = (RecyclerView) findViewById(R.id.recyclerview_videos);
        LinearLayoutManager videosManager = new LinearLayoutManager(this);
        mRecyclerViewVideos.setLayoutManager(videosManager);
        mVideoAdapter = new VideoAdapter(this);
        mRecyclerViewVideos.setAdapter(mVideoAdapter);
        Intent fatherIntent = getIntent();
        movieDbHelper = new MovieDbHelper(this);
        if (fatherIntent != null && fatherIntent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieId = fatherIntent.getStringExtra(Intent.EXTRA_TEXT);
            new FetchMovieDetailsTask(this, movieDbHelper).execute(movieId);
        }
    }

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
        }
    }

    @Override
    public void onClick(MoviesResult movieSelected) {
        //TODO Open youtube intent
    }
}
