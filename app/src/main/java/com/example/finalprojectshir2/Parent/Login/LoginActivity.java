package com.example.finalprojectshir2.Parent.Login;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Home.HomeActivity;
import com.example.finalprojectshir2.InputValidator;
import com.example.finalprojectshir2.InternetConnectionReceiver;
import com.example.finalprojectshir2.NetworkReceiver;
import com.example.finalprojectshir2.Parent.Register.RegisterActivity;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.callbacks.FirebaseCallback;
import com.example.finalprojectshir2.models.User;
import com.example.finalprojectshir2.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailEditText, passwordEditText, etEmail, etSubj, etPass2;
    private TextView gotoSignUp;
    private Button buttonLogin,forgotPasswordTextView, btnSend;
    private FirebaseAuth mAuth;
    InternetConnectionReceiver internetConectionReceiver;
    NetworkReceiver networkReceiver;
    IntentFilter intentConnectionFilter;


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


        // debug
        emailEditText.setText("stav@gmail.com");
        passwordEditText.setText("Sstav71!");
        // Set click listeners
        buttonLogin.setOnClickListener(this);
        gotoSignUp.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);

        internetConectionReceiver = new InternetConnectionReceiver();
        networkReceiver = new NetworkReceiver();
        intentConnectionFilter = new IntentFilter();
        intentConnectionFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
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
            createCustomDialog();
        }

    }

//           private void createCustomDialog() {
//            Dialog dialog = new Dialog(this);
//            dialog.setTitle("your dialog title");
//            dialog.setContentView(R.layout.passdialog);
//            dialog.show();
//    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        String validPassword = InputValidator.validatePassword(password);
        String validEmail = InputValidator.validateEmail(email);

        if (!validEmail.isEmpty() || !validPassword.isEmpty()) {
            if (!validPassword.isEmpty()) {
                Toast.makeText(this, validPassword, Toast.LENGTH_SHORT).show();
            }
            if (!validEmail.isEmpty()) {
                Toast.makeText(this, validEmail, Toast.LENGTH_SHORT).show();
            }
            return;
        }

        UserRepository userRepository = new UserRepository();
        userRepository.loginUser(email, password, new FirebaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginActivity.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(LoginActivity.this, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.d("LOGIN_TAG", errorMessage);
            }
        }, LoginActivity.this);
    }


    private void createCustomDialog() {
        Dialog dialog  = new Dialog (this);
        dialog.setTitle("שחזור סיסמה");
        dialog.setContentView(R.layout.passdialog);
        btnSend = dialog.findViewById(R.id.btnSend);
        etEmail = dialog.findViewById(R.id.etEmail);
        etSubj = dialog.findViewById(R.id.etSubj);
        etPass2 = dialog.findViewById(R.id.etPass2);
        dialog.show();
        btnSend.setOnClickListener(v -> {
            String emailSend = etEmail.getText().toString();
            String emailSubject = "שינוי סיסמה";
            String newPassW = etSubj.getText().toString();
            String emailBody ="\n"+"הסיסמה החדשה היא:"+"\n"+ newPassW;
            if (!(etSubj.getText().toString().equals(etPass2.getText().toString()))){
                Toast.makeText(LoginActivity.this,"הסיסמאות לא תואמות",Toast.LENGTH_LONG).show();
            }
else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailSend});
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                intent.setType("message/rfc822");

                // Check if there's an activity to handle the intent

                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                dialog.dismiss();
            }
        });

    }


}
