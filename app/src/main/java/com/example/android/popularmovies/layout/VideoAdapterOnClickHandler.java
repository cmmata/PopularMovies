package com.example.android.popularmovies.layout;

import com.example.android.popularmovies.themoviedb.VideosResult;

/**
 * Handler to capture movie clicks
 */
public interface VideoAdapterOnClickHandler {
    /**
     * When user taps on a video
     * @param videoSelected Video where user clicked
     */
    void onVideoClick(VideosResult videoSelected);
}
