package com.example.android.popularmovies.layout;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.themoviedb.VideosResult;

import java.util.List;

/**
 * Cache of the children views for a video list item.
 */
public class VideoAdapterViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final TextView mVideoTextView;
    private List<VideosResult> mVideosList;
    private VideoAdapterOnClickHandler mClickHandler;

    public VideoAdapterViewHolder(View view, List<VideosResult> mVideosList, VideoAdapterOnClickHandler mClickHandler) {
        super(view);
        mVideoTextView = (TextView) view.findViewById(R.id.movie_trailer_title);
        this.mVideosList = mVideosList;
        this.mClickHandler = mClickHandler;
        view.setOnClickListener(this);
    }

    /**
     * This gets called by the child views during a click.
     *
     * @param v The View that was clicked
     */
    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        VideosResult videoSelected = mVideosList.get(adapterPosition);
        mClickHandler.onVideoClick(videoSelected);
    }
}
