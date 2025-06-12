package com.example.finalprojectshir2.Manager.ManagerReviews;

import android.app.AlertDialog;
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
import com.example.finalprojectshir2.models.ReviewResponse;
import com.example.finalprojectshir2.repositories.KinderGartenRepository;
import com.example.finalprojectshir2.repositories.ReviewRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ManagerReviewsActivity extends AppCompatActivity implements ManagerReviewsAdapter.OnReviewActionListener {
    private static final String TAG = "ManagerReviewsActivity";
    public static final String EXTRA_KINDERGARTEN_ID = "kindergarten_id";
    public static final String EXTRA_KINDERGARTEN_NAME = "kindergarten_name";

    private String kindergartenId;
    private String kindergartenName;
    private ReviewRepository reviewRepository;
    private KinderGartenRepository kindergartenRepository;

    private RecyclerView reviewsRecyclerView;
    private ManagerReviewsAdapter reviewsAdapter;
    private List<Review> reviewsList = new ArrayList<>();
    private TextView emptyReviewsTextView;
    private ProgressBar progressBar;
    private TextView kindergartenNameTextView;
//    private RatingBar averageRatingBar;
    private TextView ratingCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reviews);
        Log.d(TAG, "onCreate: Initializing ManagerReviewsActivity");

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
    }

    private void initializeViews() {
        reviewsRecyclerView = findViewById(R.id.managerReviewsRecyclerView);
        emptyReviewsTextView = findViewById(R.id.emptyReviewsTextView);
        progressBar = findViewById(R.id.reviewsProgressBar);
        kindergartenNameTextView = findViewById(R.id.kindergartenNameTextView);
//        averageRatingBar = findViewById(R.id.averageRatingBar);
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
            getSupportActionBar().setTitle("ניהול חוות דעת");
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

            // Check if averageRating exists in the model
            float rating = 0f;
            if (kindergarten.getAverageRating() != null) {
                rating = kindergarten.getAverageRating();
            }
//            averageRatingBar.setRating(rating);

            // Check if reviewCount exists in the model
            int count = 0;
            if (kindergarten.getReviewCount() != null) {
                count = kindergarten.getReviewCount();
            }
            String reviewCountText = "(" + count + " חוות דעת)";
            ratingCountTextView.setText(reviewCountText);
        });
    }

    private void setupRecyclerView() {
        reviewsAdapter = new ManagerReviewsAdapter(this, reviewsList, this);
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
                            Toast.makeText(ManagerReviewsActivity.this, "חוות הדעת נמחקה בהצלחה", Toast.LENGTH_SHORT).show();

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

    @Override
    public void onRespondToReview(Review review) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_respond_to_review, null);
        builder.setView(dialogView);

        EditText responseEditText = dialogView.findViewById(R.id.responseEditText);
        Button cancelButton = dialogView.findViewById(R.id.cancelResponseButton);
        Button submitButton = dialogView.findViewById(R.id.submitResponseButton);

        // If there's an existing response, show it in the edit text
        if (review.getResponse() != null) {
            responseEditText.setText(review.getResponse().getComment());
        }

        AlertDialog dialog = builder.create();
        dialog.show();

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        submitButton.setOnClickListener(v -> {
            String responseText = responseEditText.getText().toString().trim();

            if (responseText.isEmpty()) {
                Toast.makeText(this, "אנא הוסף תגובה", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create review response object
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                showError("יש להתחבר כדי להגיב");
                dialog.dismiss();
                return;
            }

            String userId = currentUser.getUid();
            String userName = currentUser.getDisplayName() != null ?
                    currentUser.getDisplayName() : "מנהל/ת הגן";

            ReviewResponse response = new ReviewResponse(userId, userName, responseText);

            // Show loading
            dialog.dismiss();
            showLoading();

            // Save response to Firebase
            reviewRepository.addResponseToReview(review.getId(), response, new FirebaseCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    hideLoading();
                    Toast.makeText(ManagerReviewsActivity.this, "התגובה נוספה בהצלחה", Toast.LENGTH_SHORT).show();

                    // Reload reviews to update the UI
                    loadReviews();
                }

                @Override
                public void onError(String error) {
                    hideLoading();
                    showError("Error adding response: " + error);
                }
            });
        });
    }

    @Override
    public void onFlagReview(Review review) {
        new AlertDialog.Builder(this)
                .setTitle("סימון חוות דעת כלא ראויה")
                .setMessage("האם אתה בטוח שברצונך לסמן את חוות הדעת הזו כלא ראויה? חוות הדעת תישלח לבדיקה.")
                .setPositiveButton("כן", (dialog, which) -> {
                    showLoading();
                    reviewRepository.flagReview(review.getId(), new FirebaseCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            hideLoading();
                            Toast.makeText(ManagerReviewsActivity.this, "חוות הדעת סומנה כלא ראויה", Toast.LENGTH_SHORT).show();
                            loadReviews();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError("Error flagging review: " + error);
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