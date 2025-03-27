package com.example.finalprojectshir2.Manager.ManagerProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.KinderGartenProfile;
import com.example.finalprojectshir2.Parent.ParentProfile.ParentProfileActivity;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManagerProfileActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_profile);
        bottomNavManager = findViewById(R.id.bottomNavManager);
        bottomNavManager.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
            Intent i = new Intent(this, ManagerProfileActivity.class);
            return true;
        }
        else if (item.getItemId() == R.id.nav_ganHome){
            Intent i = new Intent(this,     KinderGartenProfile.class);
            startActivity(i);
        }
        return false;
    }
}