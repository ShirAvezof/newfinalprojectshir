package com.example.finalprojectshir2.Manager.ManagerLogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.InputValidator;
import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.Manager.ManagerRegister.ManagerRegisterActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Manager;
import com.example.finalprojectshir2.repositories.ManagerRepository;

public class ManagerLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailEditText, passwordEditText;
    private TextView gotoSignUp, forgotPasswordTextView;
    private Button loginButton;
    private ManagerRepository managerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        gotoSignUp = findViewById(R.id.gotoSignUp);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        loginButton = findViewById(R.id.loginButton);

        // Initialize repository
        managerRepository = new ManagerRepository();

        // Set click listeners
        loginButton.setOnClickListener(this);
        gotoSignUp.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == gotoSignUp) {
            Intent i = new Intent(this, ManagerRegisterActivity.class);
            startActivity(i);
        } else if (v == loginButton) {
            attemptLogin();
        } else if (v == forgotPasswordTextView) {
            // TODO: Implement forgot password functionality
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input

        String emailError = InputValidator.validateEmail(email);
        if (!emailError.isEmpty()) {
            emailEditText.setError(emailError);
            return;
        }

        String passwordError = InputValidator.validatePassword(password);
        if (!passwordError.isEmpty()) {
            passwordEditText.setError(passwordError);
            return;
        }

        loginButton.setEnabled(false);
        managerRepository.loginManager(email, password, new FirebaseCallback<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    Toast.makeText(ManagerLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerLoginActivity.this, ManagerHomeActivity.class);
                    startActivity(intent);
                    finish(); // Close login activity
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    loginButton.setEnabled(true);
                    Toast.makeText(ManagerLoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show();
                });
            }
        }, this);
    }
}