package com.example.finalprojectshir2.Manager.ManagerHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.CreateKindergardens.CreateKindergartenActivity;
import com.example.finalprojectshir2.R;

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton;
    Button kExists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        kExists = findViewById(R.id.kExists);
        kExists.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == startButton) {
            Intent i = new Intent(this, CreateKindergartenActivity.class);
            startActivity(i);
        }
        else if (v==kExists){
            Intent i = new Intent(this, ManagerKindergartenProfile.class);
            i.putExtra("kindergarten_id", getIntent().getStringExtra("kindergarten_id"));
            startActivity(i);
        }
    }
}