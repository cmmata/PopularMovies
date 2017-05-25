package com.example.android.popularmovies.layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.themoviedb.ReviewsResult;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private List<ReviewsResult> mReviewsList;
    private Context context;

    /**
     * Cache of the children views for a review list item.
     */
    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewContent;
        public final TextView mReviewAuthor;
        public final TextView mReviewUrl;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = (TextView) view.findViewById(R.id.movie_review_author);
            mReviewContent = (TextView) view.findViewById(R.id.movie_review_content);
            mReviewUrl = (TextView) view.findViewById(R.id.movie_review_url);
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
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param reviewAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        ReviewsResult reviewSelected = mReviewsList.get(position);
        String reviewAuthor = reviewSelected.getAuthor();
        String reviewContent = reviewSelected.getContent();
        String reviewUrl = reviewSelected.getUrl();
        reviewAdapterViewHolder.mReviewContent.setText(reviewContent);
        reviewAdapterViewHolder.mReviewAuthor.setText(reviewAuthor);
        reviewAdapterViewHolder.mReviewUrl.setText(reviewUrl);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mReviewsList) return 0;
        return mReviewsList.size();
    }

    /**
     * Sets Adapter data
     * @param reviewsResults List of ReviewsResult
     */
    public void setData(List<ReviewsResult> reviewsResults) {
        mReviewsList = reviewsResults;
        notifyDataSetChanged();
    }
}
