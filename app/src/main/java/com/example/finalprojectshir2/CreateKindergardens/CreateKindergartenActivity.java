package com.example.finalprojectshir2.CreateKindergardens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Parent.Register.RegisterPresenter;
import com.example.finalprojectshir2.R;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateKindergartenActivity extends AppCompatActivity {
    private EditText kindergartenNameEditText, ownerNameEditText, addressEditText, phoneEditText, aboutEditText, hoursEditText;
    private Button submitButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private CreateKindergartenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_kindergarten);
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        presenter = new CreateKindergartenPresenter(this);

        kindergartenNameEditText = findViewById(R.id.kindergartenNameEditText);
        ownerNameEditText = findViewById(R.id.ownerNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        aboutEditText = findViewById(R.id.aboutEditText);
        hoursEditText = findViewById(R.id.hoursEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kindergartenName = kindergartenNameEditText.getText().toString().trim();
                String ownerName = ownerNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                // create instance of kindergarten
                KinderGarten gan = new KinderGarten(kindergartenName, ownerName, address, phone);


                presenter.submitKinderGarten();
            }
        });

    }
}