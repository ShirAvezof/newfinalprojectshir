package com.example.finalprojectshir2.Manager.ManagerHome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Manager.ManagerProfile.ManagerProfileActivity;
import com.example.finalprojectshir2.Manager.ManagerReviews.ManagerReviewsActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;


public class ManagerKindergartenProfile extends AppCompatActivity  {
    private static final String TAG = "ManagerKGProfile";
    public static final String EXTRA_KINDERGARTEN_ID = "kindergarten_id";
    private static final int REQUEST_IMAGE_PICK = 1001;

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
    private Button reviewsButton;
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;
    private Button backButton;
    private TextView aboutLabelTextView;
    private TextView hoursLabelTextView;
    private TextView galleryLabelTextView;

    // License type and image elements
    private TextView licenseLabelTextView;
    private TextView licenseTypeTextView;
    private EditText licenseTypeEditText;
    private Button uploadLicenseImageButton;
    private ImageView licenseImageView;
    private String selectedLicenseImageBase64;

    private EditText ownerNameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText aboutEditText;
    private RatingBar profileRatingBar;
    private TextView reviewCountTextView;
    private EditText hoursEditText;
    private LinearLayout buttonContainer;

    private ImageView[] galleryImages;


    private KinderGarten currentKindergarten;

    private boolean isEditMode = false;

    private ManagerKindergartenProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_kindergarten_profile);
        Log.d(TAG, "onCreate: Initializing activity");

        initializeViews();
        setupPresenter();
        applyStyles();
        setupListeners();

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
        ganNameTextView = findViewById(R.id.ganNameTextView);
        ownerNameTextView = findViewById(R.id.ownerNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        aboutTextView = findViewById(R.id.aboutTextView);
        hoursTextView = findViewById(R.id.hoursTextView);
        aboutLabelTextView = findViewById(R.id.aboutLabelTextView);
        hoursLabelTextView = findViewById(R.id.hoursLabelTextView);
        galleryLabelTextView = findViewById(R.id.galleryLabelTextView);
        profileRatingBar = findViewById(R.id.profileRatingBar);
        reviewCountTextView = findViewById(R.id.reviewCountTextView);
        onlineCamerasCheckBox = findViewById(R.id.onlineCamerasCheckBox);
        closedCircuitCamerasCheckBox = findViewById(R.id.closedCircuitCamerasCheckBox);
        activeFridaysCheckBox = findViewById(R.id.activeFridaysCheckBox);
        businessLicenseImageView = findViewById(R.id.businessLicenseImageView);
        backButton = findViewById(R.id.backButton);

        // Initialize license views
        licenseLabelTextView = findViewById(R.id.licenseLabelTextView);
        licenseTypeTextView = findViewById(R.id.licenseTypeTextView);
        licenseImageView = findViewById(R.id.licenseImageView);
        uploadLicenseImageButton = findViewById(R.id.uploadLicenseImageButton);

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        galleryImages = new ImageView[3];
        galleryImages[0] = findViewById(R.id.galleryImage1);
        galleryImages[1] = findViewById(R.id.galleryImage2);
        galleryImages[2] = findViewById(R.id.galleryImage3);

        reviewsButton = findViewById(R.id.reviewsButton);

        // Create button container
        View parent = (View) findViewById(R.id.reviewsButton).getParent();
        if (parent instanceof LinearLayout) {
            LinearLayout parentLayout = (LinearLayout) parent;

            buttonContainer = new LinearLayout(this);
            buttonContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            buttonContainer.setOrientation(LinearLayout.VERTICAL);

            // Add edit button
            editButton = new Button(this);
            editButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            editButton.setText("ערוך פרטי גן");
            buttonContainer.addView(editButton);

            // Create save button
            saveButton = new Button(this);
            saveButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            saveButton.setText("שמור שינויים");
            saveButton.setVisibility(View.GONE);
            buttonContainer.addView(saveButton);

            // Create cancel button
            cancelButton = new Button(this);
            cancelButton.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            cancelButton.setText("בטל שינויים");
            cancelButton.setVisibility(View.GONE);
            buttonContainer.addView(cancelButton);

            // Add the button container to the layout before the reviews button
            parentLayout.addView(buttonContainer, parentLayout.indexOfChild(reviewsButton));
        }

        Log.d(TAG, "Views initialized");
    }

    private void setupListeners() {
        // Set up button click listeners
        if (reviewsButton != null) {
            reviewsButton.setOnClickListener(v -> openReviews());
        }

        if (editButton != null) {
            editButton.setOnClickListener(v -> enableEditMode());
        }

        if (saveButton != null) {
            saveButton.setOnClickListener(v -> saveChanges());
        }

        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> cancelChanges());
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        if (uploadLicenseImageButton != null) {
            uploadLicenseImageButton.setOnClickListener(v -> selectLicenseImage());
        }


    }

    private void applyStyles() {
        // Style the reviews button
        if (reviewsButton != null) {
            reviewsButton.setBackgroundColor(0xFF4285F4); // Google Blue
            reviewsButton.setTextColor(0xFFFFFFFF); // White
        }

        // Style the edit button
        if (editButton != null) {
            editButton.setBackgroundColor(0xFF4CAF50); // Green
            editButton.setTextColor(0xFFFFFFFF); // White
        }

        // Style the save button
        if (saveButton != null) {
            saveButton.setBackgroundColor(0xFF4CAF50); // Green
            saveButton.setTextColor(0xFFFFFFFF); // White
        }

        // Style the cancel button
        if (cancelButton != null) {
            cancelButton.setBackgroundColor(0xFFE53935); // Red
            cancelButton.setTextColor(0xFFFFFFFF); // White
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

        // Style the label text views
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

        // Style the license label
        if (licenseLabelTextView != null) {
            licenseLabelTextView.setTextColor(0xFF4285F4); // Google Blue
            licenseLabelTextView.setTextSize(18);
        }

        // Style the license upload button
        if (uploadLicenseImageButton != null) {
            uploadLicenseImageButton.setBackgroundColor(0xFF4285F4); // Google Blue
            uploadLicenseImageButton.setTextColor(0xFFFFFFFF); // White
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

    private void loadLicenseImage(KinderGarten kindergarten) {
        if (licenseImageView == null) {
            Log.w(TAG, "License image view not found in layout");
            return;
        }

        if (kindergarten.getLicenseImage() != null && !kindergarten.getLicenseImage().isEmpty()) {
            try {
                // Convert Base64 string to bitmap
                byte[] decodedString = android.util.Base64.decode(
                        kindergarten.getLicenseImage(), android.util.Base64.DEFAULT);
                Bitmap decodedBitmap = android.graphics.BitmapFactory.decodeByteArray(
                        decodedString, 0, decodedString.length);

                if (decodedBitmap != null) {
                    licenseImageView.setAlpha(0f);
                    licenseImageView.setImageBitmap(decodedBitmap);
                    licenseImageView.animate()
                            .alpha(1f)
                            .setDuration(500)
                            .setInterpolator(new android.view.animation.DecelerateInterpolator())
                            .start();
                    licenseImageView.setVisibility(View.VISIBLE);

                    Log.d(TAG, "Successfully loaded license image");
                } else {
                    Log.e(TAG, "Failed to decode license bitmap from Base64 string");
                    licenseImageView.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading license image: " + e.getMessage());
                licenseImageView.setVisibility(View.GONE);
            }
        } else {
            Log.d(TAG, "No license image available for this kindergarten");
            licenseImageView.setVisibility(View.GONE);
        }
    }

    private void enableEditMode() {
        if (currentKindergarten == null) {
            showError("No kindergarten data available to edit");
            return;
        }
        isEditMode = true;
        // Create edit fields if not already created
        createEditFields();

        ownerNameTextView.setVisibility(View.GONE);
        ownerNameEditText.setVisibility(View.VISIBLE);
        ownerNameEditText.setText(currentKindergarten.getOwnerName());

        addressTextView.setVisibility(View.GONE);
        addressEditText.setVisibility(View.VISIBLE);
        addressEditText.setText(currentKindergarten.getAddress());

        phoneTextView.setVisibility(View.GONE);
        phoneEditText.setVisibility(View.VISIBLE);
        phoneEditText.setText(currentKindergarten.getPhone());

        aboutTextView.setVisibility(View.GONE);
        aboutEditText.setVisibility(View.VISIBLE);
        aboutEditText.setText(currentKindergarten.getAboutgan());

        hoursTextView.setVisibility(View.GONE);
        hoursEditText.setVisibility(View.VISIBLE);
        hoursEditText.setText(currentKindergarten.getHours());

        // Show license type edit
        licenseTypeTextView.setVisibility(View.GONE);
        licenseTypeEditText.setVisibility(View.VISIBLE);
        licenseTypeEditText.setText(currentKindergarten.getLicenseType());

        // Show license image upload button
        if (uploadLicenseImageButton != null) {
            uploadLicenseImageButton.setVisibility(View.VISIBLE);
        }

        onlineCamerasCheckBox.setEnabled(true);
        closedCircuitCamerasCheckBox.setEnabled(true);
        activeFridaysCheckBox.setEnabled(true);

        // Hide edit button and show save/cancel buttons
        editButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

        // Disable reviews button during edit
        reviewsButton.setEnabled(false);
        reviewsButton.setAlpha(0.5f);

        Toast.makeText(this, "מצב עריכה פעיל", Toast.LENGTH_SHORT).show();
    }

    private void createEditFields() {
        View basicInfoContainer = (View) ownerNameTextView.getParent();
        View aboutContainer = (View) aboutTextView.getParent();
        View hoursContainer = (View) hoursTextView.getParent();
        View licenseContainer = (View) licenseTypeTextView.getParent();

        // Create owner name edit text if not exists
        if (ownerNameEditText == null && basicInfoContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) basicInfoContainer;
            ownerNameEditText = new EditText(this);
            ownerNameEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            ownerNameEditText.setTextColor(0xFF333333);
            ownerNameEditText.setTextSize(16);
            ownerNameEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            ownerNameEditText.setHint("מנהל/ת הגן");
            ownerNameEditText.setVisibility(View.GONE);
            container.addView(ownerNameEditText, container.indexOfChild(ownerNameTextView) + 1);
        }

        // Create address edit text if not exists
        if (addressEditText == null && basicInfoContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) basicInfoContainer;
            addressEditText = new EditText(this);
            addressEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            addressEditText.setTextColor(0xFF333333);
            addressEditText.setTextSize(16);
            addressEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            addressEditText.setHint("כתובת");
            addressEditText.setVisibility(View.GONE);
            container.addView(addressEditText, container.indexOfChild(addressTextView) + 1);
        }

        // Create phone edit text if not exists
        if (phoneEditText == null && basicInfoContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) basicInfoContainer;
            phoneEditText = new EditText(this);
            phoneEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            phoneEditText.setTextColor(0xFF333333);
            phoneEditText.setTextSize(16);
            phoneEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            phoneEditText.setHint("טלפון");
            phoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            phoneEditText.setVisibility(View.GONE);
            container.addView(phoneEditText, container.indexOfChild(phoneTextView) + 1);
        }

        // Create about edit text if not exists
        if (aboutEditText == null && aboutContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) aboutContainer;
            aboutEditText = new EditText(this);
            aboutEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            aboutEditText.setTextColor(0xFF333333);
            aboutEditText.setTextSize(14);
            aboutEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            aboutEditText.setHint("אודות הגן");
            aboutEditText.setLines(4);
            aboutEditText.setMaxLines(8);
            aboutEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            aboutEditText.setVisibility(View.GONE);
            container.addView(aboutEditText, container.indexOfChild(aboutTextView) + 1);
        }

        // Create hours edit text if not exists
        if (hoursEditText == null && hoursContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) hoursContainer;
            hoursEditText = new EditText(this);
            hoursEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            hoursEditText.setTextColor(0xFF333333);
            hoursEditText.setTextSize(14);
            hoursEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            hoursEditText.setHint("שעות פעילות");
            hoursEditText.setLines(2);
            hoursEditText.setMaxLines(4);
            hoursEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            hoursEditText.setVisibility(View.GONE);
            container.addView(hoursEditText, container.indexOfChild(hoursTextView) + 1);
        }

        // Create license type edit text if not exists
        if (licenseTypeEditText == null && licenseContainer instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) licenseContainer;
            licenseTypeEditText = new EditText(this);
            licenseTypeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            licenseTypeEditText.setTextColor(0xFF333333);
            licenseTypeEditText.setTextSize(16);
            licenseTypeEditText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            licenseTypeEditText.setHint("סוג רישיון");
            licenseTypeEditText.setVisibility(View.GONE);
            container.addView(licenseTypeEditText, container.indexOfChild(licenseTypeTextView) + 1);
        }
    }

    private void saveChanges() {
        if (currentKindergarten == null) {
            showError("No kindergarten data to save");
            return;
        }

        showLoading();
        // Update the kindergarten
        currentKindergarten.setOwnerName(ownerNameEditText.getText().toString().trim());
        currentKindergarten.setAddress(addressEditText.getText().toString().trim());
        currentKindergarten.setPhone(phoneEditText.getText().toString().trim());
        currentKindergarten.setAboutgan(aboutEditText.getText().toString().trim());
        currentKindergarten.setHours(hoursEditText.getText().toString().trim());
        currentKindergarten.setLicenseType(licenseTypeEditText.getText().toString().trim());

        // Update checkbox values
        currentKindergarten.setHasOnlineCameras(onlineCamerasCheckBox.isChecked());
        currentKindergarten.setHasClosedCircuitCameras(closedCircuitCamerasCheckBox.isChecked());
        currentKindergarten.setActiveOnFriday(activeFridaysCheckBox.isChecked());

        // Save license image if selected
        if (selectedLicenseImageBase64 != null && !selectedLicenseImageBase64.isEmpty()) {
            currentKindergarten.setLicenseImage(selectedLicenseImageBase64);
        }

        // Save changes to database
        presenter.saveKindergartenDetails(currentKindergarten);
    }

    private void cancelChanges() {
        disableEditMode();

        // Display the current data again
        if (currentKindergarten != null) {
            displayKindergartenDetails(currentKindergarten);
        }

        Toast.makeText(this, "השינויים בוטלו", Toast.LENGTH_SHORT).show();
    }

    private void disableEditMode() {
        isEditMode = false;

        // Show TextViews and hide EditTexts
        ownerNameTextView.setVisibility(View.VISIBLE);
        ownerNameEditText.setVisibility(View.GONE);

        addressTextView.setVisibility(View.VISIBLE);
        addressEditText.setVisibility(View.GONE);

        phoneTextView.setVisibility(View.VISIBLE);
        phoneEditText.setVisibility(View.GONE);

        aboutTextView.setVisibility(View.VISIBLE);
        aboutEditText.setVisibility(View.GONE);

        hoursTextView.setVisibility(View.VISIBLE);
        hoursEditText.setVisibility(View.GONE);

        licenseTypeTextView.setVisibility(View.VISIBLE);
        licenseTypeEditText.setVisibility(View.GONE);

        // Hide license upload button
        if (uploadLicenseImageButton != null) {
            uploadLicenseImageButton.setVisibility(View.GONE);
        }

        // Disable checkboxes
        onlineCamerasCheckBox.setEnabled(false);
        closedCircuitCamerasCheckBox.setEnabled(false);
        activeFridaysCheckBox.setEnabled(false);

        // Show edit button and hide save/cancel buttons
        editButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        // Re-enable reviews button
        reviewsButton.setEnabled(true);
        reviewsButton.setAlpha(1.0f);

        // Reset selected image data
        selectedLicenseImageBase64 = null;
    }

    private void selectLicenseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            try {
                // Get the image from data
                android.net.Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    // Show a loading indicator
                    Toast.makeText(this, "מעבד את התמונה...", Toast.LENGTH_SHORT).show();

                    // Process the image in a background thread
                    new Thread(() -> {
                        try {
                            // Convert to Base64
                            java.io.InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                            Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(imageStream);

                            // Resize the image to reduce memory usage
                            bitmap = resizeBitmap(bitmap, 800); // Max width or height of 800px

                            // Convert to Base64
                            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            selectedLicenseImageBase64 = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);

                            // Update UI on main thread
                            Bitmap finalBitmap = bitmap;
                            runOnUiThread(() -> {
                                licenseImageView.setImageBitmap(finalBitmap);
                                licenseImageView.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "התמונה נבחרה בהצלחה", Toast.LENGTH_SHORT).show();
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing image: " + e.getMessage());
                            runOnUiThread(() -> {
                                Toast.makeText(this, "שגיאה בעיבוד התמונה: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                        }
                    }).start();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error selecting image: " + e.getMessage());
                Toast.makeText(this, "שגיאה בבחירת התמונה: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // Helper method to resize bitmap
    private Bitmap resizeBitmap(Bitmap bitmap, int maxDimension) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratio = (float) width / (float) height;

        if (width > height && width > maxDimension) {
            width = maxDimension;
            height = (int) (width / ratio);
        } else if (height > width && height > maxDimension) {
            height = maxDimension;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public void onSaveSuccess() {
        hideLoading();
        disableEditMode();
        Toast.makeText(this, "השינויים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
    }

    public void onSaveError(String error) {
        hideLoading();
        Toast.makeText(this, "שגיאה בשמירת השינויים: " + error, Toast.LENGTH_LONG).show();
    }

    private void setupPresenter() {
        presenter = new ManagerKindergartenProfilePresenter(this);
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
        // Store the current kindergarten data for editing
        this.currentKindergarten = kindergarten;

        runOnUiThread(() -> {
            // Set text values
            if (ganNameTextView != null) ganNameTextView.setText(kindergarten.getGanname());
            if (ownerNameTextView != null) ownerNameTextView.setText("מנהל/ת: " + kindergarten.getOwnerName());

            if (addressTextView != null) {
                String fullAddress = "כתובת: " + kindergarten.getAddress();
                addressTextView.setText(fullAddress);
            }

            if (profileRatingBar != null) {
                Float rating = kindergarten.getAverageRating();
                profileRatingBar.setRating(rating != null ? rating : 0f);
            }

            // Set review count if available
            if (reviewCountTextView != null) {
                Integer count = kindergarten.getReviewCount();
                reviewCountTextView.setText("(" + (count != null ? count : 0) + ")");
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

            // Set license type text
            if (licenseTypeTextView != null) {
                String licenseType = kindergarten.getLicenseType();
                if (licenseType != null && !licenseType.isEmpty()) {
                    licenseTypeTextView.setText("סוג רישיון: " + licenseType);
                } else {
                    licenseTypeTextView.setText("סוג רישיון: לא צוין");
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

            // Load main gallery image
            loadMainImage(kindergarten);

            // Load license image
            loadLicenseImage(kindergarten);

            // Enable edit button now that we have data
            if (editButton != null) {
                editButton.setEnabled(true);
            }
        });
    }

    private void openReviews() {
        if (currentKindergarten == null || currentKindergarten.getId() == null || currentKindergarten.getId().isEmpty()) {
            Toast.makeText(this, "שגיאה: לא נמצא מזהה גן", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ManagerReviewsActivity.class);
        intent.putExtra(ManagerReviewsActivity.EXTRA_KINDERGARTEN_ID, currentKindergarten.getId());
        intent.putExtra(ManagerReviewsActivity.EXTRA_KINDERGARTEN_NAME, currentKindergarten.getGanname());
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;
    }
}