package com.example.finalprojectshir2.Parent.ParentProfile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.Parent.Login.LoginActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentProfileActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        ParentProfilePresenter.ParentProfileView {

    private BottomNavigationView bottomNavigationView;
    private Dialog editProfileDialog;
    private EditText editParentName;
    private Button btnConfirm, btnCancel, btnLogout;
    private TextView tvParentName, tvParentEmail;
    private Button btnEditProfile;
    private ProgressBar progressBar;
    private ParentProfilePresenter presenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);

        // Initialize the presenter
        presenter = new ParentProfilePresenter(this);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tvParentName = findViewById(R.id.tvParentName);
        tvParentEmail = findViewById(R.id.tvParentEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);

        // Setup edit profile dialog
        setupEditProfileDialog();

        // Set listeners
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        btnLogout.setOnClickListener(v -> presenter.logoutUser());

        // Load user profile
        presenter.loadUserProfile();
    }

    private void setupEditProfileDialog() {
        editProfileDialog = new Dialog(this);
        editProfileDialog.setContentView(R.layout.editparentprofile);

        editParentName = editProfileDialog.findViewById(R.id.editParentName);
        btnConfirm = editProfileDialog.findViewById(R.id.btnConfirm);
        btnCancel = editProfileDialog.findViewById(R.id.btnCancel);

        btnConfirm.setOnClickListener(v -> {
            String newName = editParentName.getText().toString().trim();
            if (!newName.isEmpty()) {
                presenter.updateUserName(newName, this);
                editProfileDialog.dismiss();
            } else {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> editProfileDialog.dismiss());
    }

    private void showEditProfileDialog() {
        String currentName = tvParentName.getText().toString();
        editParentName.setText(currentName);
        editProfileDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.nav_favorites){
            Intent i = new Intent(this, FavoriteKindergarndsActivity.class);
            startActivity(i);
            return true;
        }
        else if (item.getItemId() == R.id.nav_profile){
            return true;
        }

        return false;
    }

    // ParentProfileView implementation יישום
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayUserProfile(User user) {
        if (user != null) {
            tvParentName.setText(user.getUserName());
            tvParentEmail.setText(user.getUserEmail());
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateProfileSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, com.example.finalprojectshir2.Parent.Login.LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}