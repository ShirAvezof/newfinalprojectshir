package com.example.finalprojectshir2.KindergardenProfile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.models.Review;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;
import com.example.finalprojectshir2.repositories.ReviewRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements ReviewsAdapter.OnReviewActionListener {
    private static final String TAG = "ReviewsActivity";
    public static final String EXTRA_KINDERGARTEN_ID = "kindergarten_id";
    public static final String EXTRA_KINDERGARTEN_NAME = "kindergarten_name";

    private String kindergartenId;
    private String kindergartenName;
    private ReviewRepository reviewRepository;
    private KinderGartenRepository kindergartenRepository;

    private RecyclerView reviewsRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewsList = new ArrayList<>();
    private TextView emptyReviewsTextView;
    private ProgressBar progressBar;
    private FloatingActionButton addReviewFab;
    private TextView kindergartenNameTextView;
    private RatingBar averageRatingBar;
    private TextView ratingCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Log.d(TAG, "onCreate: Initializing ReviewsActivity");

        // Get the kindergarten_id from the intent
        kindergartenId = getIntent().getStringExtra(EXTRA_KINDERGARTEN_ID);
        kindergartenName = getIntent().getStringExtra(EXTRA_KINDERGARTEN_NAME);

        if (kindergartenId == null || kindergartenId.isEmpty()) {
            showError("No kindergarten ID provided");
            finish();
            return;
        }

        initializeViews();
        setupToolbar();
        initializeRepositories();
        loadKindergartenDetails();
        setupRecyclerView();
        loadReviews();

        addReviewFab.setOnClickListener(v -> showAddReviewDialog());
    }

    private void initializeViews() {
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        emptyReviewsTextView = findViewById(R.id.emptyReviewsTextView);
        progressBar = findViewById(R.id.reviewsProgressBar);
        addReviewFab = findViewById(R.id.addReviewFab);
        kindergartenNameTextView = findViewById(R.id.kindergartenNameTextView);
        averageRatingBar = findViewById(R.id.averageRatingBar);
        ratingCountTextView = findViewById(R.id.ratingCountTextView);

        if (kindergartenName != null && !kindergartenName.isEmpty()) {
            kindergartenNameTextView.setText(kindergartenName);
        }

        Log.d(TAG, "Views initialized");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.reviewsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initializeRepositories() {
        reviewRepository = new ReviewRepository();
        kindergartenRepository = new KinderGartenRepository();
        Log.d(TAG, "Repositories initialized");
    }

    private void loadKindergartenDetails() {
        Log.d(TAG, "Loading kindergarten details for ID: " + kindergartenId);
        showLoading();

        kindergartenRepository.getKinderGartenById(kindergartenId, new FirebaseCallback<KinderGarten>() {
            @Override
            public void onSuccess(KinderGarten result) {
                hideLoading();
                if (result != null) {
                    updateKindergartenDetails(result);
                }
            }

            @Override
            public void onError(String error) {
                hideLoading();
                showError("Error loading kindergarten details: " + error);
            }
        });
    }

    private void updateKindergartenDetails(KinderGarten kindergarten) {
        runOnUiThread(() -> {
            kindergartenNameTextView.setText(kindergarten.getGanname());

            // Check if averageRating exists in the model, if not, you might need to add it
            float rating = 0f;
            if (kindergarten.getAverageRating() != null) {
                rating = kindergarten.getAverageRating();
            }
            averageRatingBar.setRating(rating);

            // Check if reviewCount exists in the model, if not, you might need to add it
            int count = 0;
            if (kindergarten.getReviewCount() != null) {
                count = kindergarten.getReviewCount();
            }
            String reviewCountText = "(" + count + " חוות דעת)";
            ratingCountTextView.setText(reviewCountText);
        });
    }

    private void setupRecyclerView() {
        reviewsAdapter = new ReviewsAdapter(this, reviewsList, this);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewsAdapter);
        Log.d(TAG, "RecyclerView setup complete");
    }

    private void loadReviews() {
        Log.d(TAG, "Loading reviews for kindergarten ID: " + kindergartenId);
        showLoading();

        reviewRepository.getReviewsByKindergarten(kindergartenId, new FirebaseCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                hideLoading();
                updateReviewsList(result);
            }

            @Override
            public void onError(String error) {
                hideLoading();
                showError("Error loading reviews: " + error);
                showEmptyState();
            }
        });
    }

    private void updateReviewsList(List<Review> reviews) {
        runOnUiThread(() -> {
            reviewsList.clear();
            reviewsList.addAll(reviews);
            reviewsAdapter.notifyDataSetChanged();

            if (reviews.isEmpty()) {
                showEmptyState();
            } else {
                hideEmptyState();
            }
        });
    }

    private void showAddReviewDialog() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            showError("יש להתחבר כדי להוסיף חוות דעת");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_review, null);
        builder.setView(dialogView);

        RatingBar ratingBar = dialogView.findViewById(R.id.newReviewRatingBar);
        EditText commentEditText = dialogView.findViewById(R.id.newReviewCommentEditText);
        Button cancelButton = dialogView.findViewById(R.id.cancelReviewButton);
        Button submitButton = dialogView.findViewById(R.id.submitReviewButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        submitButton.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentEditText.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(this, "אנא דרג/י את הגן", Toast.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Toast.makeText(this, "אנא הוסף/י תיאור", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create review object
            String userId = currentUser.getUid();
            String userName = currentUser.getDisplayName() != null ?
                    currentUser.getDisplayName() : "משתמש/ת";

            Review newReview = new Review(kindergartenId, userId, userName, comment, rating);

            // Show loading
            dialog.dismiss();
            showLoading();

            // Save review to Firebase
            reviewRepository.addReview(newReview, new FirebaseCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    hideLoading();
                    Toast.makeText(ReviewsActivity.this, "חוות הדעת נוספה בהצלחה", Toast.LENGTH_SHORT).show();

                    // Reload reviews and kindergarten details to update ratings
                    loadReviews();
                    loadKindergartenDetails();
                }

                @Override
                public void onError(String error) {
                    hideLoading();
                    showError("Error adding review: " + error);
                }
            });
        });
    }

    @Override
    public void onDeleteReview(Review review) {
        new AlertDialog.Builder(this)
                .setTitle("מחיקת חוות דעת")
                .setMessage("האם אתה בטוח שברצונך למחוק את חוות הדעת הזו?")
                .setPositiveButton("כן", (dialog, which) -> {
                    showLoading();
                    reviewRepository.deleteReview(review.getId(), kindergartenId, new FirebaseCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            hideLoading();
                            Toast.makeText(ReviewsActivity.this, "חוות הדעת נמחקה בהצלחה", Toast.LENGTH_SHORT).show();

                            // Reload reviews and kindergarten details to update ratings
                            loadReviews();
                            loadKindergartenDetails();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError("Error deleting review: " + error);
                        }
                    });
                })
                .setNegativeButton("לא", null)
                .show();
    }

    private void showEmptyState() {
        runOnUiThread(() -> {
            emptyReviewsTextView.setVisibility(View.VISIBLE);
            reviewsRecyclerView.setVisibility(View.GONE);
        });
    }

    private void hideEmptyState() {
        runOnUiThread(() -> {
            emptyReviewsTextView.setVisibility(View.GONE);
            reviewsRecyclerView.setVisibility(View.VISIBLE);
        });
    }

    private void showLoading() {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));
    }

    private void hideLoading() {
        runOnUiThread(() -> progressBar.setVisibility(View.GONE));
    }

    private void showError(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}