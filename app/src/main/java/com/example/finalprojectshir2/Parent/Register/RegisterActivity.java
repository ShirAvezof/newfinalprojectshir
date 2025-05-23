package com.example.finalprojectshir2.Parent.Register;
import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.InternetConnectionReceiver;
import com.example.finalprojectshir2.MainActivity;
import com.example.finalprojectshir2.NetworkReceiver;
import com.example.finalprojectshir2.Parent.Login.LoginActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.User;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

public class RegisterActivity extends AppCompatActivity {

    private EditText lastNameEditText, firstNameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private RegisterPresenter presenter;
    InternetConnectionReceiver internetConectionReceiver;
    NetworkReceiver networkReceiver;
    IntentFilter intentConnectionFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firestore and FirebaseAuth
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        presenter = new RegisterPresenter(this);

        // Initialize UI components
        lastNameEditText = findViewById(R.id.lastNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButtonp);

        internetConectionReceiver = new InternetConnectionReceiver();
        networkReceiver = new NetworkReceiver();
        intentConnectionFilter = new IntentFilter();
        intentConnectionFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        // Set up sign up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String lastName = lastNameEditText.getText().toString().trim();
                String firstName = firstNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                User user = new User(firstName + " " + lastName,password,email);
                // Validate inputs
                if (lastName.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.submitUser(user);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(internetConectionReceiver, intentConnectionFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetConectionReceiver);

    }

    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public void onUserSuccess(User user) {
        Toast.makeText(this,"נרשמת בהצלחה", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

   /* private void saveUserToFirestore(String firstName, String lastName, String email) {
        // Create a new user document in Firestore
  /*      CollectionReference usersCollection = firestore.collection("users");
        DocumentReference newUserRef = usersCollection.document();


         User user = new  com.example.finalprojectshir2.models.User(firstName, lastName, email);


        newUserRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    navigateToHome();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterActivity.this, "Error saving user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToHome() {
        // Navigate to HomeActivity after successful registration
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the registration screen
    }
    */
}