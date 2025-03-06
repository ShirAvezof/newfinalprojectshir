package com.example.finalprojectshir2.Home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.Parent.SearchActivity.ActivitySearchKinderGarten;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "HomeActivity";

    // UI components
    private BottomNavigationView bottomNavigationView;
    private TextInputEditText searchEditText;
    private MaterialButton searchButton;

    // Filter checkboxes
    private MaterialCheckBox onlineCamerasCheckbox;
    private MaterialCheckBox closedCircuitCheckbox;
    private MaterialCheckBox fridayActiveCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        setupListeners();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        // Initialize filter checkboxes
        onlineCamerasCheckbox = findViewById(R.id.onlineCamerasCheckbox);
        closedCircuitCheckbox = findViewById(R.id.closedCircuitCheckbox);
        fridayActiveCheckbox = findViewById(R.id.fridayActiveCheckbox);

        // Setup bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void setupListeners() {
        // Search button click listener
        searchButton.setOnClickListener(v -> navigateToSearchActivity());

        // No longer disable the button when text is empty
        // as we want to allow searching with empty city to show all results
    }

    private void navigateToSearchActivity() {
        String city = searchEditText.getText().toString().trim();
        // Allow empty city to show all results

        // Get filter values
        boolean hasOnlineCameras = onlineCamerasCheckbox.isChecked();
        boolean hasClosedCircuit = closedCircuitCheckbox.isChecked();
        boolean isActiveOnFriday = fridayActiveCheckbox.isChecked();

        // Create intent to SearchActivity
        Intent intent = new Intent(this, ActivitySearchKinderGarten.class);

        // Pass search parameters
        intent.putExtra("city", city);
        intent.putExtra("hasOnlineCameras", hasOnlineCameras);
        intent.putExtra("hasClosedCircuitCameras", hasClosedCircuit);
        intent.putExtra("isActiveOnFriday", isActiveOnFriday);

        // Start the activity
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ParentProfileActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.nav_favorites) {
            Intent i = new Intent(this, FavoriteKindergarndsActivity.class);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.nav_home) {
            return true;
        }
        return false;
    }
}