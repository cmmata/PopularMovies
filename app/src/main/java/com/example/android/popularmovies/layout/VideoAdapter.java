package com.example.android.popularmovies.layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.themoviedb.MoviesResult;
import com.example.android.popularmovies.themoviedb.VideosResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder>{

    private List<VideosResult> mVideosList;
    private Context context;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final VideoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface VideoAdapterOnClickHandler {
        void onClick(VideosResult videoSelected);
    }

    /**
     * Creates a VideoAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a video list item.
     */
    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mVideoTextView;
        public final ImageView mImageView;

        public VideoAdapterViewHolder(View view) {
            super(view);
            mVideoTextView = (TextView) view.findViewById(R.id.movie_title);
            mImageView = (ImageView) view.findViewById(R.id.movie_thumb);
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
            mClickHandler.onClick(videoSelected);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param VideoAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final VideoAdapterViewHolder VideoAdapterViewHolder, int position) {
        VideosResult videoSelected = mVideosList.get(position);
        VideoAdapterViewHolder.mVideoTextView.setText(videoSelected.getType());
        //TODO predefined icon
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mVideosList) return 0;
        return mVideosList.size();
    }

    /**
     * Sets Adapter data
     * @param videosResults List of VideosResult
     */
    public void setData(List<VideosResult> videosResults) {
        mVideosList = videosResults;
        notifyDataSetChanged();
    }
}