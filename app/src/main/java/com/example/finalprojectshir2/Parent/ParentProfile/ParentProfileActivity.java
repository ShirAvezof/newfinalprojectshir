package com.example.finalprojectshir2.Parent.ParentProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parent_profile);
    bottomNavigationView = findViewById(R.id.bottomNavigationView);

    bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_favorites){
            Intent i = new Intent(this, FavoriteKindergarndsActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.nav_profile ){
            return true;
        }

        return false;
    }
}