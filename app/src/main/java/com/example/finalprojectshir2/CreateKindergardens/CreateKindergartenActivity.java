package com.example.finalprojectshir2.CreateKindergardens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.KindergardenProfile.KindergardenProfileActivity;
import com.example.finalprojectshir2.Manager.ManagerHome.ManagerKindergartenProfile;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateKindergartenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateKindergartenActivity";
    private EditText kindergartenNameEditText, ownerNameEditText, addressEditText, phoneEditText, aboutEditText, hoursEditText;
    private CheckBox onlineCamerasCheckbox, closedCircuitCheckbox, fridayActiveCheckbox;
    private Button submitButton, uploadPhotosButton, uploadLicenseButton;
    private ImageView imageView, licenseImageView;
    private FirebaseFirestore firestore;
    //שירות ניהול המשתמשים
    private FirebaseAuth mAuth;
    private CreateKindergartenPresenter presenter;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2, LICENSE_GALLERY = 3, LICENSE_CAMERA = 4;
    private boolean hasImage = false;
    private boolean hasLicenseImage = false;
    private ImageView currentSelectedImageView; // Track which ImageView we're currently uploading to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_kindergarten);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        presenter = new CreateKindergartenPresenter(this);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        // EditText fields
        kindergartenNameEditText = findViewById(R.id.kindergartenNameEditText);
        ownerNameEditText = findViewById(R.id.ownerNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        aboutEditText = findViewById(R.id.aboutEditText);
        hoursEditText = findViewById(R.id.hoursEditText);

        // Checkboxes
        onlineCamerasCheckbox = findViewById(R.id.onlineCamerasCheckbox);
        closedCircuitCheckbox = findViewById(R.id.closedCircuitCheckbox);
        fridayActiveCheckbox = findViewById(R.id.fridayActiveCheckbox);

        // Buttons
        submitButton = findViewById(R.id.submitButton);
        uploadPhotosButton = findViewById(R.id.uploadPhotosButton);
        uploadLicenseButton = findViewById(R.id.uploadLicenseButton); // New button for license

        // ImageViews
        imageView = findViewById(R.id.imageView2);
        licenseImageView = findViewById(R.id.licenseImageView); // New ImageView for license
    }

    private void setupListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    submitForm();
                }
            }
        });

        uploadPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedImageView = imageView; // Set the current target
                showPictureDialog();
            }
        });

        uploadLicenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectedImageView = licenseImageView; // Set the current target
                showPictureDialog();
            }
        });
    }

    private boolean validateInput() {
        boolean isValid = true;

        // Check kindergarten name
        if (kindergartenNameEditText.getText().toString().trim().isEmpty()) {
            kindergartenNameEditText.setError("שם הגן נדרש");
            isValid = false;
        }

        // Check owner name
        if (ownerNameEditText.getText().toString().trim().isEmpty()) {
            ownerNameEditText.setError("שם הבעלים נדרש");
            isValid = false;
        }

        // Check address
        if (addressEditText.getText().toString().trim().isEmpty()) {
            addressEditText.setError("כתובת נדרשת");
            isValid = false;
        }
        if (aboutEditText.getText().toString().isEmpty()){
            aboutEditText.setError("אודות הגן");
            isValid = false;
        }
        if (hoursEditText.getText().toString().isEmpty()){
            hoursEditText.setError("שעות נדרשות");
            isValid = false;
        }

        if (kindergartenNameEditText.getText().toString().isEmpty()||ownerNameEditText.getText().toString().isEmpty()||
                addressEditText.getText().toString().trim().isEmpty()||aboutEditText.getText().toString().isEmpty()||
                hoursEditText.getText().toString().isEmpty()|| phoneEditText.getText().toString().isEmpty())
        {
            Toast.makeText(this, "אחד או יותר מהשדות ריקים, נא למלא" , Toast.LENGTH_SHORT).show();
        }

        // Check phone number
        String phone = phoneEditText.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneEditText.setError("מספר טלפון נדרש");
            isValid = false;
        } else if (phone.length() != 10) {
            phoneEditText.setError("מספר טלפון חייב להיות בן 10 ספרות");
            isValid = false;
        }

        // Check if image is selected
        if (!hasImage) {
            Toast.makeText(this, "נא להעלות תמונה של הגן", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Check if license image is selected
        if (!hasLicenseImage) {
            Toast.makeText(this, "נא להעלות תמונה של רישיון העסק", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
    private void submitForm() {
        // Get values from form fields
        String kindergartenName = kindergartenNameEditText.getText().toString().trim();
        String ownerName = ownerNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String aboutGan = aboutEditText.getText().toString().trim();
        String hours = hoursEditText.getText().toString().trim();

        // Get values from checkboxes
        boolean hasOnlineCameras = onlineCamerasCheckbox.isChecked();
        boolean hasClosedCircuitCameras = closedCircuitCheckbox.isChecked();
        boolean isActiveOnFriday = fridayActiveCheckbox.isChecked();

        // Create enhanced KinderGarten object
        KinderGarten gan = new KinderGarten();
        gan.setGanname(kindergartenName);
        gan.setOwnerName(ownerName);
        gan.setAddress(address);
        gan.setPhone(phone);
        gan.setAboutgan(aboutGan);
        gan.setHours(hours);

        gan.setHasOnlineCameras(hasOnlineCameras);
        gan.setHasClosedCircuitCameras(hasClosedCircuitCameras);
        gan.setActiveOnFriday(isActiveOnFriday);

        // Convert kindergarten image to base64 and add to KinderGarten object
        String base64Image = imageViewToBase64(imageView);
        if (base64Image != null) {
            gan.setImage(base64Image);
        }

        // Convert license image to base64 and add to KinderGarten object
        String base64LicenseImage = imageViewToBase64(licenseImageView);
        if (base64LicenseImage != null) {
            gan.setLicenseImage(base64LicenseImage);
        }

        // Submit to Firestore through presenter
        presenter.submitKinderGarten(gan);

        Intent i = new Intent(this, ManagerKindergartenProfile.class);
        i.putExtra(ManagerKindergartenProfile.EXTRA_KINDERGARTEN_ID, gan.getId());
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        currentSelectedImageView = imageView; // Default to main image
        showPictureDialog();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("נא לבחור מאיפה להוסיף תמונה:");
        String[] pictureDialogItems = {
                "מהגלריה",
                "מהמצלמה" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (currentSelectedImageView == licenseImageView) {
            startActivityForResult(galleryIntent, LICENSE_GALLERY);
        } else {
            startActivityForResult(galleryIntent, GALLERY);
        }
    }

    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (currentSelectedImageView == licenseImageView) {
                startActivityForResult(intent, LICENSE_CAMERA);
            } else {
                startActivityForResult(intent, CAMERA);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
//ממירה את התמונה בהתאם
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY || requestCode == LICENSE_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    if (requestCode == LICENSE_GALLERY) {
                        processLicenseImage(bitmap);
                    } else {
                        processSelectedImage(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA || requestCode == LICENSE_CAMERA) {
            if (data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (requestCode == LICENSE_CAMERA) {
                    processLicenseImage(bitmap);
                } else {
                    processSelectedImage(bitmap);
                }
            }
        }
    }

    private void processSelectedImage(Bitmap bitmap) {
        // Compress the bitmap to reduce file size before encoding to base64
        Bitmap compressedBitmap = getResizedBitmap(bitmap, 800); // Max 800px width/height
        imageView.setImageBitmap(compressedBitmap);
        hasImage = true;
        Toast.makeText(this, "התמונה נטענה בהצלחה", Toast.LENGTH_SHORT).show();
    }

    private void processLicenseImage(Bitmap bitmap) {
        // Compress the bitmap to reduce file size before encoding to base64
        Bitmap compressedBitmap = getResizedBitmap(bitmap, 800); // Max 800px width/height
        licenseImageView.setImageBitmap(compressedBitmap);
        hasLicenseImage = true;
        Toast.makeText(this, "תמונת רישיון העסק נטענה בהצלחה", Toast.LENGTH_SHORT).show();
    }

    // Method to resize bitmap to prevent large base64 strings
    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private String imageViewToBase64(ImageView imageView) {
        try {
            if (imageView.getDrawable() == null) {
                return null;
            }

            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream); // 70% quality compression
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error converting image to base64: " + e.getMessage());
            return null;
        }
    }

    public void showSuccess(KinderGarten garten) {
        Toast.makeText(this, "הגן נוסף בהצלחה", Toast.LENGTH_SHORT).show();
        // Navigate back to previous screen or to a confirmation screen
        finish();
    }

    public void showError(String message) {
        Toast.makeText(this, "שגיאה: " + message, Toast.LENGTH_LONG).show();
    }
}