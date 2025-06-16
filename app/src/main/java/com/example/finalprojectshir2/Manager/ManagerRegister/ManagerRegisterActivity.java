package com.example.finalprojectshir2.Manager.ManagerRegister;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.InternetConnectionReceiver;
import com.example.finalprojectshir2.Manager.ManagerHome.ManagerHomeActivity;
import com.example.finalprojectshir2.NetworkReceiver;
import com.example.finalprojectshir2.R;

public class ManagerRegisterActivity extends AppCompatActivity implements
        View.OnClickListener, ManagerRegisterPresenter.ManagerRegisterView {

    private Button signUpButtonm;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private ManagerRegisterPresenter presenter;
    private InternetConnectionReceiver internetConnectionReceiver;
    private NetworkReceiver networkReceiver;
    private IntentFilter intentConnectionFilter;//התראה שהחיבור לרשת משתנה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_register);

        // Initialize Presenter
        presenter = new ManagerRegisterPresenter(this);

        signUpButtonm = findViewById(R.id.signUpButtonm);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButtonm.setOnClickListener(this);

        // Initialize network receivers
        internetConnectionReceiver = new InternetConnectionReceiver();
        networkReceiver = new NetworkReceiver();
        intentConnectionFilter = new IntentFilter();
        intentConnectionFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(internetConnectionReceiver, intentConnectionFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(internetConnectionReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpButtonm) {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            presenter.registerManager(firstName, lastName, email, password);
        }
    }

    // ManagerRegisterView implementation יישום
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailError(String error) {
        emailEditText.setError(error);
    }

    @Override
    public void showPasswordError(String error) {
        passwordEditText.setError(error);
    }

    @Override
    public void setLoadingState(boolean isLoading) {
        signUpButtonm.setEnabled(!isLoading);
    }

    @Override
    public void onRegistrationSuccess(String email, String fullName) {
        Toast.makeText(ManagerRegisterActivity.this,
                "Registration successful!", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(ManagerRegisterActivity.this,
                ManagerHomeActivity.class);
        i.putExtra("MANAGER_EMAIL", email);
        i.putExtra("MANAGER_NAME", fullName);
        startActivity(i);
        finish();
    }
}