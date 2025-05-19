package com.example.finalprojectshir2.Manager.ManagerProfile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.Manager.ManagerHome.ManagerKindergartenProfile;
import com.example.finalprojectshir2.Manager.ManagerLogin.ManagerLoginActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.Manager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagerProfileActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavManager;
    private TextView tvManagerName;
    private Dialog dialog;
    private Button editProfileButton;
    private EditText editManagerName;
    private Button btnConfirm, btnCancel, btnLogout;
    private String kindergartenId;
    private ProgressBar progressBar;

    private ManagerProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);

        initializeViews();
        setupPresenter();
        setupListeners();

        kindergartenId = getIntent().getStringExtra("kindergarten_id");

        // Load manager details using the presenter
        presenter.loadManagerDetails();
    }

    private void initializeViews() {
        bottomNavManager = findViewById(R.id.bottomNavManager);
        tvManagerName = findViewById(R.id.tvManagerName);
        editProfileButton = findViewById(R.id.editProfileButton);
        progressBar = findViewById(R.id.progressBar);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (progressBar == null) {
            // Create progress bar programmatically if not found in layout
            progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);

            // Add to the root view
            View rootView = findViewById(android.R.id.content);
            if (rootView instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) rootView).addView(progressBar);

                // Center the progress bar
                progressBar.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                progressBar.setX((float) (rootView.getWidth() / 2 - 50));
                progressBar.setY((float) (rootView.getHeight() / 2 - 50));
            }
        }

        // Initialize dialog for editing profile
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_manager);

        editManagerName = dialog.findViewById(R.id.editManagerName);
        btnConfirm = dialog.findViewById(R.id.btnConfirm);
        btnCancel = dialog.findViewById(R.id.btnCancel);
    }

    private void setupPresenter() {
        presenter = new ManagerProfilePresenter(this, this);
    }

    private void setupListeners() {
        bottomNavManager.setOnNavigationItemSelectedListener(this);

        // Edit button listener
        editProfileButton.setOnClickListener(v -> {
            editManagerName.setText(tvManagerName.getText());
            dialog.show();
        });

        // Confirm button listener
        btnConfirm.setOnClickListener(v -> {
            String newName = editManagerName.getText().toString();

            if (newName.trim().isEmpty()) {
                Toast.makeText(this, "שם לא יכול להיות ריק", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.updateManagerProfile(newName);
            dialog.dismiss();
        });

        // Cancel button listener
        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }


    public void displayManagerDetails(Manager manager) {
        if (manager != null) {
            tvManagerName.setText(manager.getManagerName());
        }
    }

    public void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void onProfileUpdateSuccess(String name) {
        tvManagerName.setText(name);
        Toast.makeText(this, "הפרופיל עודכן בהצלחה", Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_profile) {
            // Already on profile page
            return true;
        }
        else if (itemId == R.id.nav_ganHome) {
            if (kindergartenId == null || kindergartenId.isEmpty()) {
                Toast.makeText(this, "לא התקבל מזהה גן", Toast.LENGTH_SHORT).show();
                return false;
            }

            Intent i = new Intent(this, ManagerKindergartenProfile.class);
            i.putExtra(ManagerKindergartenProfile.EXTRA_KINDERGARTEN_ID, kindergartenId);
            startActivity(i);
            return true;
        }
        else if (itemId == R.id.nav_home) {
            Intent i = new Intent(this, ManagerHomeActivity.class);
            if (kindergartenId != null) {
                i.putExtra("kindergarten_id", kindergartenId);
            }
            startActivity(i);
            return true;
        }
        else if (itemId == R.id.nav_home) {
            Intent i = new Intent(this, ManagerLoginActivity.class);
            startActivity(i);
            return true;
        }


        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDestroy();
        }
    }
}