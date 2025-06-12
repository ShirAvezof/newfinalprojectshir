package com.example.finalprojectshir2.Home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.finalprojectshir2.BackgroundMusicService;
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


    private BottomNavigationView bottomNavigationView;
    private TextInputEditText searchEditText;
    private MaterialButton searchButton;
    private ImageButton musicButton;
    private boolean isPlaying = false;


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
        musicButton=findViewById(R.id.musicButton);

        onlineCamerasCheckbox = findViewById(R.id.onlineCamerasCheckbox);
        closedCircuitCheckbox = findViewById(R.id.closedCircuitCheckbox);
        fridayActiveCheckbox = findViewById(R.id.fridayActiveCheckbox);


        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        musicButton.setOnClickListener(view -> {
            if (!isPlaying) {
                startService(new Intent(HomeActivity.this, BackgroundMusicService.class));
                musicButton.setImageDrawable(getResources().getDrawable(R.drawable.play_music));
                isPlaying = !isPlaying;

            } else {
                stopService(new Intent(HomeActivity.this, BackgroundMusicService.class));
                musicButton.setImageDrawable(getResources().getDrawable(R.drawable.stop_music));
                isPlaying = !isPlaying;
            }
        });

    }

    private void setupListeners() {

        searchButton.setOnClickListener(v -> navigateToSearchActivity());


    }
// מעבירה את הנתונים הדרושים למסך התוצאה עם רשימת הגנים
    private void navigateToSearchActivity() {
        String city = searchEditText.getText().toString().trim();

        boolean hasOnlineCameras = onlineCamerasCheckbox.isChecked();
        boolean hasClosedCircuit = closedCircuitCheckbox.isChecked();
        boolean isActiveOnFriday = fridayActiveCheckbox.isChecked();

        // Create intent to SearchActivity
        Intent intent = new Intent(this, ActivitySearchKinderGarten.class);


        intent.putExtra("city", city);
        intent.putExtra("hasOnlineCameras", hasOnlineCameras);
        intent.putExtra("hasClosedCircuitCameras", hasClosedCircuit);
        intent.putExtra("isActiveOnFriday", isActiveOnFriday);

        // Start the activity
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, BackgroundMusicService.class));
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