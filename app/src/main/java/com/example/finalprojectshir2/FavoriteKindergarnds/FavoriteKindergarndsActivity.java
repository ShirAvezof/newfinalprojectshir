package com.example.finalprojectshir2.FavoriteKindergarnds;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoriteKindergarndsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite_kindergarnds);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ParentProfileActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_home){
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_favorites ){
            return true;
        }
        return false;
    }
}