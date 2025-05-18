package com.example.finalprojectshir2.Manager.ManagerHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.CreateKindergardens.CreateKindergartenActivity;
import com.example.finalprojectshir2.Manager.ManagerProfile.ManagerProfileActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.repositories.ManagerRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private Button startButton;
    private Button kExists;
    private TextView welcomeTextView;
    private BottomNavigationView bottomNavigationView;
    private String kindergartenId;
    private ManagerRepository managerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        initializeViews();
        setupRepository();
        setupListeners();

        // Get kindergarten ID from intent
        kindergartenId = getIntent().getStringExtra("kindergarten_id");

        // Update UI based on whether kindergarten ID exists
        updateUIBasedOnKindergartenId();

        // Customize welcome message with manager's name if available
        customizeWelcomeMessage();
    }

    private void initializeViews() {
        startButton = findViewById(R.id.startButton);
        kExists = findViewById(R.id.kExists);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        bottomNavigationView = findViewById(R.id.bottomNavManager);
    }

    private void setupRepository() {
        managerRepository = new ManagerRepository();
    }

    private void setupListeners() {
        startButton.setOnClickListener(this);
        kExists.setOnClickListener(this);

        // Set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void updateUIBasedOnKindergartenId() {
        // Hide/show buttons based on kindergarten ID
        if (kindergartenId == null || kindergartenId.isEmpty()) {
            startButton.setVisibility(View.VISIBLE);
            kExists.setVisibility(View.GONE);
        } else {
            startButton.setVisibility(View.GONE);
            kExists.setText("צפייה בפרטי הגן");
            kExists.setVisibility(View.VISIBLE);
        }
    }

    private void customizeWelcomeMessage() {
        // Could enhance to get manager's name from repository and customize welcome message
        String managerId = managerRepository.getCurrentManagerId();
        if (managerId != null) {
            welcomeTextView.setText("ברוך הבא למערכת ניהול הגן");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.startButton) {
            Intent i = new Intent(this, CreateKindergartenActivity.class);
            startActivity(i);
        }
        else if (id == R.id.kExists) {
            if (kindergartenId == null || kindergartenId.isEmpty()) {
                Toast.makeText(this, "אין גן זמין", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent(this, ManagerKindergartenProfile.class);
            i.putExtra(ManagerKindergartenProfile.EXTRA_KINDERGARTEN_ID, kindergartenId);
            startActivity(i);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            // Already on home page
            return true;
        }
        else if (itemId == R.id.nav_profile) {
            Intent i = new Intent(this, ManagerProfileActivity.class);
            if (kindergartenId != null) {
                i.putExtra("kindergarten_id", kindergartenId);
            }
            startActivity(i);
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

        return false;
    }
}