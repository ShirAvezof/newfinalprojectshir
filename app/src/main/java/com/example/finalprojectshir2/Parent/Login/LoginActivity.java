package com.example.finalprojectshir2.Parent.Login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.Parent.Register.RegisterActivity;
import com.example.finalprojectshir2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText;
    private TextView gotoSignUp;
    private Button buttonLogin,forgotPasswordTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views by ID
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        gotoSignUp = findViewById(R.id.gotoSignUp);
        buttonLogin = findViewById(R.id.loginButton);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Set click listeners
        buttonLogin.setOnClickListener(this);
        gotoSignUp.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == gotoSignUp) {
            // Navigate to the Sign-Up screen
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v == buttonLogin) {
            // Handle Login
            loginUser();
        }
        if (v == forgotPasswordTextView) {
            // Handle Forgot Password (optional)
            // You can add functionality to reset the password here
            Toast.makeText(this, "Password reset functionality", Toast.LENGTH_SHORT).show();
        }
            createCustomDialog();
    }

           private void createCustomDialog() {
            Dialog dialog = new Dialog(this);
            dialog.setTitle("your dialog title");
            dialog.setContentView(R.layout.passdialog);
            dialog.show();
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate the inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in the user using Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Successfully logged in, navigate to the HomeActivity
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Finish the login activity so the user can't go back to it
                    } else {
                        // Authentication failed, show a toast message
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
