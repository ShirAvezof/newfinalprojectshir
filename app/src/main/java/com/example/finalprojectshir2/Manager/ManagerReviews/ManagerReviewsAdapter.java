package com.example.finalprojectshir2.Manager.ManagerReviews;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.Review;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ManagerReviewsAdapter extends RecyclerView.Adapter<ManagerReviewsAdapter.ReviewViewHolder> {
    private static final String TAG = "ManagerReviewsAdapter";

    private List<Review> reviews;
    private Context context;
    private OnReviewActionListener listener;

    public interface OnReviewActionListener {
        void onDeleteReview(Review review);
        void onRespondToReview(Review review);
        void onFlagReview(Review review);
    }

    public ManagerReviewsAdapter(Context context, List<Review> reviews, OnReviewActionListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.nameTextView.setText(review.getUserName());
        holder.commentTextView.setText(review.getComment());
//        holder.ratingBar.setRating(review.getRating());

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(review.getCreatedAtDate());
        holder.dateTextView.setText(formattedDate);

        // Handle response section
        if (review.hasResponse()) {
            holder.responseCard.setVisibility(View.VISIBLE);
            holder.responseNameTextView.setText(review.getResponse().getUserName());
            holder.responseTextView.setText(review.getResponse().getComment());

            // Format response date
            String responseDate = sdf.format(review.getResponse().getCreatedAtDate());
            holder.responseDateTextView.setText(responseDate);

            // Change respond button text to "Edit Response"
            holder.respondButton.setText("ערוך תגובה");
        } else {
            holder.responseCard.setVisibility(View.GONE);
            holder.respondButton.setText("הגב");
        }

        // Handle flagged reviews
        if (review.isFlagged()) {
            holder.flaggedIndicator.setVisibility(View.VISIBLE);
            holder.flagButton.setVisibility(View.GONE); // Already flagged
        } else {
            holder.flaggedIndicator.setVisibility(View.GONE);
            holder.flagButton.setVisibility(View.VISIBLE);
        }

        // Set up button listeners
        holder.respondButton.setOnClickListener(v -> listener.onRespondToReview(review));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteReview(review));
        holder.flagButton.setOnClickListener(v -> listener.onFlagReview(review));
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public void updateReviews(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView commentTextView;
        TextView dateTextView;
        RatingBar ratingBar;
        Button respondButton;
        ImageView deleteButton;
        ImageView flagButton;
        View flaggedIndicator;

        // Response card elements
        CardView responseCard;
        TextView responseNameTextView;
        TextView responseTextView;
        TextView responseDateTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.reviewNameTextView);
            commentTextView = itemView.findViewById(R.id.reviewCommentTextView);
            dateTextView = itemView.findViewById(R.id.reviewDateTextView);
//            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            respondButton = itemView.findViewById(R.id.respondButton);
            deleteButton = itemView.findViewById(R.id.deleteReviewButton);
            flagButton = itemView.findViewById(R.id.flagReviewButton);
            flaggedIndicator = itemView.findViewById(R.id.flaggedIndicator);

            responseCard = itemView.findViewById(R.id.responseCard);
            responseNameTextView = itemView.findViewById(R.id.responseNameTextView);
            responseTextView = itemView.findViewById(R.id.responseTextView);
            responseDateTextView = itemView.findViewById(R.id.responseDateTextView);
        }
    }
}