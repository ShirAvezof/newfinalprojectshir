package com.example.finalprojectshir2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManagerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView gotoSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_login);

        gotoSignUp = findViewById(R.id.gotoSignUp);
        gotoSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == gotoSignUp) {
            Intent i = new Intent(this, ManagerRegisterActivity.class);
            startActivity(i);
        }
    }
}