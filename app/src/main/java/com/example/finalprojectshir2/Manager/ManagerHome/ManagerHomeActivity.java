package com.example.finalprojectshir2.Manager.ManagerHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.CreateKindergardens.CreateKindergartenActivity;
import com.example.finalprojectshir2.KinderGartenProfile;
import com.example.finalprojectshir2.R;

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == startButton) {
            Intent i = new Intent(this, CreateKindergartenActivity.class);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, KinderGartenProfile.class);
//            i.putExtra("kindergarten_id",)
            startActivity(i);
        }
    }
}