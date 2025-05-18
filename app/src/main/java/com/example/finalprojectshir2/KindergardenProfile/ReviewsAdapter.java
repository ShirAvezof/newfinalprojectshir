package com.example.finalprojectshir2.KindergardenProfile;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.Review;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private static final String TAG = "ReviewsAdapter";

    private List<Review> reviews;
    private Context context;
    private OnReviewActionListener listener;

    public interface OnReviewActionListener {
        void onDeleteReview(Review review);
    }

    public ReviewsAdapter(Context context, List<Review> reviews, OnReviewActionListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.nameTextView.setText(review.getUserName());
        holder.commentTextView.setText(review.getComment());
        holder.ratingBar.setRating(review.getRating());

        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = sdf.format(review.getCreatedAtDate());
        holder.dateTextView.setText(formattedDate);

        // Show delete option only if this review belongs to the current user
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (currentUserId != null && currentUserId.equals(review.getUserId())) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteReview(review));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
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
        ImageView deleteButton;

        ReviewViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.reviewNameTextView);
            commentTextView = itemView.findViewById(R.id.reviewCommentTextView);
            dateTextView = itemView.findViewById(R.id.reviewDateTextView);
            ratingBar = itemView.findViewById(R.id.reviewRatingBar);
            deleteButton = itemView.findViewById(R.id.deleteReviewButton);
        }
    }
}