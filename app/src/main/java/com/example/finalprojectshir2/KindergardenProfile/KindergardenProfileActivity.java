package com.example.finalprojectshir2.KindergardenProfile;

import android.graphics.Bitmap;
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
import androidx.core.content.ContextCompat;

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
    private TextView aboutLabelTextView;
    private TextView hoursLabelTextView;
    private TextView galleryLabelTextView;

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

        // Apply styling to views
        applyStyles();

        // Get the kindergarten_id from the intent
        String kindergartenId = getIntent().getStringExtra("kindergarten_id");
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
        ganNameTextView = findViewById(R.id.ganNameTextView);
        ownerNameTextView = findViewById(R.id.ownerNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        aboutTextView = findViewById(R.id.aboutTextView);
        hoursTextView = findViewById(R.id.hoursTextView);

        // Find label TextViews if they exist in your layout
        aboutLabelTextView = findViewById(R.id.aboutLabelTextView);
        hoursLabelTextView = findViewById(R.id.hoursLabelTextView);
        galleryLabelTextView = findViewById(R.id.galleryLabelTextView);

        onlineCamerasCheckBox = findViewById(R.id.onlineCamerasCheckBox);
        closedCircuitCamerasCheckBox = findViewById(R.id.closedCircuitCamerasCheckBox);
        activeFridaysCheckBox = findViewById(R.id.activeFridaysCheckBox);

        businessLicenseImageView = findViewById(R.id.businessLicenseImageView);

        galleryImages = new ImageView[3];
        galleryImages[0] = findViewById(R.id.galleryImage1);
        galleryImages[1] = findViewById(R.id.galleryImage2);
        galleryImages[2] = findViewById(R.id.galleryImage3);

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

    private void applyStyles() {
        // Style the back button
        if (backButton != null) {
            backButton.setTextColor(0xFF4285F4); // Google Blue
        }

        // Style the reviews button
        if (reviewsButton != null) {
            reviewsButton.setBackgroundColor(0xFF4285F4); // Google Blue
            reviewsButton.setTextColor(0xFFFFFFFF); // White
        }

        // Style the checkboxes
        if (onlineCamerasCheckBox != null) {
            onlineCamerasCheckBox.setTextColor(0xFF333333); // Dark Gray
        }

        if (closedCircuitCamerasCheckBox != null) {
            closedCircuitCamerasCheckBox.setTextColor(0xFF333333); // Dark Gray
        }

        if (activeFridaysCheckBox != null) {
            activeFridaysCheckBox.setTextColor(0xFF333333); // Dark Gray
        }

        // Style the text views
        if (ganNameTextView != null) {
            ganNameTextView.setTextColor(0xFF4285F4); // Google Blue
            ganNameTextView.setTextSize(22); // Larger text
        }

        if (ownerNameTextView != null) {
            ownerNameTextView.setTextColor(0xFF666666); // Medium Gray
            ownerNameTextView.setTextSize(16);
        }

        // Style the label text views if they exist
        if (aboutLabelTextView != null) {
            aboutLabelTextView.setTextColor(0xFF4285F4); // Google Blue
            aboutLabelTextView.setTextSize(18);
        }

        if (hoursLabelTextView != null) {
            hoursLabelTextView.setTextColor(0xFF4285F4); // Google Blue
            hoursLabelTextView.setTextSize(18);
        }

        if (galleryLabelTextView != null) {
            galleryLabelTextView.setTextColor(0xFF4285F4); // Google Blue
            galleryLabelTextView.setTextSize(18);
        }




    }

    private void loadMainImage(KinderGarten kindergarten) {

        ImageView mainImageView = findViewById(R.id.galleryImage3);
        if (mainImageView == null) {
            Log.w(TAG, "Main image view not found in layout");
            return;
        }


        if (kindergarten.getImage() != null && !kindergarten.getImage().isEmpty()) {
            try {
                // Convert Base64 string to bitmap
                byte[] decodedString = android.util.Base64.decode(kindergarten.getImage(), android.util.Base64.DEFAULT);
                Bitmap decodedBitmap = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (decodedBitmap != null) {
                    // Apply a subtle animation to the image loading
                    mainImageView.setAlpha(0f);
                    mainImageView.setImageBitmap(decodedBitmap);
                    mainImageView.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setInterpolator(new android.view.animation.DecelerateInterpolator())
                            .start();
                    mainImageView.setVisibility(View.VISIBLE);

                    Log.d(TAG, "Successfully loaded main image");
                } else {
                    Log.e(TAG, "Failed to decode bitmap from Base64 string");

                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading image: " + e.getMessage());

            }
        } else {
            Log.d(TAG, "No image available for this kindergarten");

        }
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
            Toast.makeText(this, "שגיאה: " + error, Toast.LENGTH_LONG).show();
        });
    }

    public void displayKindergartenDetails(KinderGarten kindergarten) {
        Log.d(TAG, "Displaying details for: " + kindergarten.getGanname());
        runOnUiThread(() -> {
            // Set text values
            if (ganNameTextView != null) ganNameTextView.setText(kindergarten.getGanname());
            if (ownerNameTextView != null) ownerNameTextView.setText("מנהל/ת: " + kindergarten.getOwnerName());

            if (addressTextView != null) {
                String fullAddress = "כתובת: " + kindergarten.getAddress();
                addressTextView.setText(fullAddress);
            }

            if (phoneTextView != null) phoneTextView.setText("טלפון: " + kindergarten.getPhone());

            if (aboutTextView != null) {
                String aboutText = kindergarten.getAboutgan();
                if (aboutText != null && !aboutText.isEmpty()) {
                    aboutTextView.setText(aboutText);
                    aboutTextView.setVisibility(View.VISIBLE);
                } else {
                    aboutTextView.setText("אין מידע זמין");
                }
            }

            if (hoursTextView != null) {
                String hoursText = kindergarten.getHours();
                if (hoursText != null && !hoursText.isEmpty()) {
                    hoursTextView.setText(hoursText);
                } else {
                    hoursTextView.setText("לא צוינו שעות פעילות");
                }
            }

            // Set checkbox values
            if (onlineCamerasCheckBox != null) {
                onlineCamerasCheckBox.setChecked(kindergarten.isHasOnlineCameras());
                onlineCamerasCheckBox.setEnabled(false); // Just for display
            }

            if (closedCircuitCamerasCheckBox != null) {
                closedCircuitCamerasCheckBox.setChecked(kindergarten.isHasClosedCircuitCameras());
                closedCircuitCamerasCheckBox.setEnabled(false); // Just for display
            }

            if (activeFridaysCheckBox != null) {
                activeFridaysCheckBox.setChecked(kindergarten.isActiveOnFriday());
                activeFridaysCheckBox.setEnabled(false); // Just for display
            }

            // Handle business license image
            if (businessLicenseImageView != null) {
                if (kindergarten.isHasBusinessLicense()) {
                    businessLicenseImageView.setImageResource(android.R.drawable.checkbox_on_background);
                    businessLicenseImageView.setColorFilter(0xFF4CAF50); // Green color
                } else {
                    businessLicenseImageView.setImageResource(android.R.drawable.checkbox_off_background);
                    businessLicenseImageView.setColorFilter(0xFFE53935); // Red color
                }
                businessLicenseImageView.setVisibility(View.VISIBLE);
            }

            loadMainImage(kindergarten);
        });
    }

    private void setupGalleryPlaceholders() {
        // Set placeholder images for gallery
        for (ImageView galleryImage : galleryImages) {
            if (galleryImage != null) {
                galleryImage.setImageResource(android.R.drawable.ic_menu_gallery);
                galleryImage.setBackgroundColor(0xFFEEEEEE);
                galleryImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void openReviews() {
        // For future implementation
        Toast.makeText(this, "חוות דעת יתווספו בקרוב", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}