package com.example.finalprojectshir2.Manager.ManagerRegister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.InputValidator;
import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.Manager;
import com.example.finalprojectshir2.repositories.ManagerRepository;

public class ManagerRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButtonm;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private ManagerRepository managerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        // Initialize Repository
        managerRepository = new ManagerRepository();

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

        // Validate name fields
        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in both name fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email using InputValidator
        String emailError = InputValidator.validateEmail(email);
        if (!emailError.isEmpty()) {
            emailEditText.setError(emailError);
            return;
        }

        // Validate password using InputValidator
        String passwordError = InputValidator.validatePassword(password);
        if (!passwordError.isEmpty()) {
            passwordEditText.setError(passwordError);
            return;
        }

        // Create manager object
        Manager manager = new Manager(firstName + " " + lastName, password, email);

        // Disable signup button to prevent multiple submissions
        signUpButtonm.setEnabled(false);

        // Use repository to add manager
        managerRepository.addManager(manager, new FirebaseCallback<Manager>() {
            @Override
            public void onSuccess(Manager result) {
                runOnUiThread(() -> {
                    signUpButtonm.setEnabled(true);
                    Toast.makeText(ManagerRegisterActivity.this,
                            "Registration successful!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(ManagerRegisterActivity.this,
                            ManagerHomeActivity.class);
                    i.putExtra("MANAGER_EMAIL", email);
                    i.putExtra("MANAGER_NAME", firstName + " " + lastName);
                    startActivity(i);
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    signUpButtonm.setEnabled(true);
                    Toast.makeText(ManagerRegisterActivity.this,
                            "Registration failed: " + error,
                            Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}