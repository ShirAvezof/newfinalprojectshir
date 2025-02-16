package com.example.finalprojectshir2.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.Parent.SearchActivity.ActivitySearchKinderGarten;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ParentProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_favorites){
            Intent i = new Intent(this, FavoriteKindergarndsActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_home ){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v == searchButton) {
            Intent i =new Intent(this, ActivitySearchKinderGarten.class);
            startActivity(i);
        }
    }
}