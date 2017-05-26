package com.example.android.popularmovies.layout;

import com.example.android.popularmovies.themoviedb.VideosResult;

/**
 * Handler to capture movie clicks
 */
public interface VideoAdapterOnClickHandler {
    void onVideoClick(VideosResult videoSelected);
}
