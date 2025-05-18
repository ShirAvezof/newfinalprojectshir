package com.example.finalprojectshir2.Parent.ParentProfile;

import android.annotation.SuppressLint;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectshir2.FavoriteKindergarnds.FavoriteKindergarndsActivity;
import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import android.widget.TextView;
import android.widget.Toast;


public class ParentProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    Dialog dialog;
    private EditText editParentName;
    private Button btnConfirm,btnCancel;
    private TextView tvParentName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    dialog = new Dialog(this) ;
    dialog.setContentView(R.layout.editparentprofile);
    //tvParentName = findViewById(R.id.tvParentName);
    editParentName = dialog.findViewById(R.id.editParentName);
    btnConfirm = dialog.findViewById(R.id.btnConfirm);
    btnCancel = dialog.findViewById(R.id.btnCancel);

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