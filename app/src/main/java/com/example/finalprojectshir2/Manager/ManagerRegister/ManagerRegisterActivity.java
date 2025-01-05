package com.example.finalprojectshir2.Manager.ManagerRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.Manager;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManagerRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButtonm;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        signUpButtonm = findViewById(R.id.signUpButtonm);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signUpButtonm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpButtonm) {
            registerManager();
        }
    }

    private void registerManager() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password length
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Manager object with full name
        Manager manager = new Manager(firstName + " " + lastName, password, email);

        // Save to Firestore
        db.collection("managers")
                .document(email) // Using email as document ID
                .set(manager)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ManagerRegisterActivity.this,
                            "Registration successful!", Toast.LENGTH_SHORT).show();

                    // Pass manager data to home activity
                    Intent i = new Intent(ManagerRegisterActivity.this,
                            ManagerHomeActivity.class);
                    i.putExtra("MANAGER_EMAIL", email);
                    i.putExtra("MANAGER_NAME", firstName + " " + lastName);
                    startActivity(i);
                    finish(); // Close registration activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManagerRegisterActivity.this,
                            "Registration failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}