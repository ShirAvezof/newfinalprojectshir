package com.example.finalprojectshir2.KindergardenProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;

public class KindergardenProfileActivity extends AppCompatActivity {
    private static final String TAG = "KGProfileActivity";
    public static final String EXTRA_KINDERGARTEN_ID = "kindergarten_id";

    // UI elements
    private ProgressBar progressBar;
    private TextView ganNameTextView;
    private TextView ownerNameTextView;
    private TextView addressTextView;
    private TextView phoneTextView;
    private TextView aboutTextView;
    private TextView hoursTextView;
    private CheckBox onlineCamerasCheckBox;
    private CheckBox closedCircuitCamerasCheckBox;
    private CheckBox activeFridaysCheckBox;
    private ImageView businessLicenseImageView;
    private Button backButton;
    private Button reviewsButton;

    // Gallery images
    private ImageView[] galleryImages;

    // Presenter
    private KindergardenProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kindergarden_profile);
        Log.d(TAG, "onCreate: Initializing activity");

        initializeViews();
        setupPresenter();

        // Get kindergarten ID from intent
        String kindergartenId = getIntent().getStringExtra(EXTRA_KINDERGARTEN_ID);
        if (kindergartenId == null || kindergartenId.isEmpty()) {
            showError("No kindergarten ID provided");
            finish();
            return;
        }

        // Load kindergarten details
        presenter.loadKindergartenDetails(kindergartenId);
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        if (progressBar == null) {
            // Add a progress bar to your layout if it doesn't exist
            Log.w(TAG, "ProgressBar not found in layout");
        }

        // Initialize TextViews
        ganNameTextView = findViewById(R.id.ganNameTextView);
        ownerNameTextView = findViewById(R.id.ownerNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        aboutTextView = findViewById(R.id.aboutTextView);
        hoursTextView = findViewById(R.id.hoursTextView);

        // Initialize CheckBoxes
        onlineCamerasCheckBox = findViewById(R.id.onlineCamerasCheckBox);
        closedCircuitCamerasCheckBox = findViewById(R.id.closedCircuitCamerasCheckBox);
        activeFridaysCheckBox = findViewById(R.id.activeFridaysCheckBox);

        // Initialize ImageView
        businessLicenseImageView = findViewById(R.id.businessLicenseImageView);

        // Initialize gallery images
        galleryImages = new ImageView[3];
        galleryImages[0] = findViewById(R.id.galleryImage1);
        galleryImages[1] = findViewById(R.id.galleryImage2);
        galleryImages[2] = findViewById(R.id.galleryImage3);

        // Initialize buttons
        backButton = findViewById(R.id.backButton);
        reviewsButton = findViewById(R.id.reviewsButton);

        // Set up back button click listener
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        // Set up reviews button click listener
        if (reviewsButton != null) {
            reviewsButton.setOnClickListener(v -> openReviews());
        }

        Log.d(TAG, "Views initialized");
    }

    private void setupPresenter() {
        presenter = new KindergardenProfilePresenter(this);
        Log.d(TAG, "Presenter setup complete");
    }

    public void showLoading() {
        Log.d(TAG, "Showing loading indicator");
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoading() {
        Log.d(TAG, "Hiding loading indicator");
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void showError(String error) {
        Log.e(TAG, "Error occurred: " + error);
        runOnUiThread(() -> {
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
        });
    }

    public void displayKindergartenDetails(KinderGarten kindergarten) {
        Log.d(TAG, "Displaying details for: " + kindergarten.getGanname());
        runOnUiThread(() -> {
            // Set text values
            if (ganNameTextView != null) ganNameTextView.setText(kindergarten.getGanname());
            if (ownerNameTextView != null) ownerNameTextView.setText(kindergarten.getOwnerName());
            if (addressTextView != null) {
                String fullAddress = kindergarten.getAddress();
                addressTextView.setText(fullAddress);
            }
            if (phoneTextView != null) phoneTextView.setText(kindergarten.getPhone());
            if (aboutTextView != null) aboutTextView.setText(kindergarten.getAboutgan());
            if (hoursTextView != null) hoursTextView.setText(kindergarten.getHours());

            // Set checkbox values
            if (onlineCamerasCheckBox != null) {
               // onlineCamerasCheckBox.setChecked(kindergarten.isHasOnlineCameras());
                onlineCamerasCheckBox.setEnabled(false);
            }

            if (closedCircuitCamerasCheckBox != null) {
              //  closedCircuitCamerasCheckBox.setChecked(kindergarten.isHasClosedCircuitCameras());
                closedCircuitCamerasCheckBox.setEnabled(false); // Just for display
            }

            if (activeFridaysCheckBox != null) {
             //   activeFridaysCheckBox.setChecked(kindergarten.isActiveOnFridays());
                activeFridaysCheckBox.setEnabled(false); // Just for display
            }

            // Handle business license image
            if (businessLicenseImageView != null) {
                    businessLicenseImageView.setImageResource(android.R.drawable.checkbox_on_background);
                    businessLicenseImageView.setVisibility(View.VISIBLE);
            }

            // Load gallery images - assuming you have image URLs in your model
            // This would require an image loading library like Glide or Picasso
            loadGalleryImages(kindergarten);
        });
    }

    private void loadGalleryImages(KinderGarten kindergarten) {
        // This is a placeholder - you'll need to implement based on how your images are stored
        // For example, if you're using Glide:
        /*
        if (kindergarten.getImageUrls() != null && !kindergarten.getImageUrls().isEmpty()) {
            for (int i = 0; i < Math.min(galleryImages.length, kindergarten.getImageUrls().size()); i++) {
                if (galleryImages[i] != null) {
                    Glide.with(this)
                         .load(kindergarten.getImageUrls().get(i))
                         .placeholder(R.drawable.placeholder_image)
                         .error(R.drawable.error_image)
                         .into(galleryImages[i]);
                }
            }
        }
        */

        // For now, we'll just log that this needs implementation
        Log.d(TAG, "Image loading would happen here. Implement with your image loading library.");
    }

    private void openReviews() {
        // TODO: Implement navigation to reviews screen
        Toast.makeText(this, "Reviews feature coming soon", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
        //    presenter.onDestroy();
            presenter = null;
        }
    }
}