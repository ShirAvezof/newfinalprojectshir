package com.example.finalprojectshir2.Manager.ManagerProfile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectshir2.Manager.ManagerHome.ManagerKindergartenProfile;
import com.example.finalprojectshir2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import android.widget.Toast;


public class ManagerProfileActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavManager;
    private TextView tvManagerName;
    Dialog dialog;
    private Button editProfileButton;
    private EditText editManagerName;
    private Button btnConfirm, btnCancel;
    private String kindergartenId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager_profile);
        bottomNavManager = findViewById(R.id.bottomNavManager);
        bottomNavManager.setOnNavigationItemSelectedListener(this);

        tvManagerName = findViewById(R.id.tvManagerName);
        editProfileButton = findViewById(R.id.editProfileButton);
        loadManagerDetails(); // קריאה לפונקציה שתביא את הנתונים
        // יצירת הדיאלוג לעריכת פרופיל
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit_manager);

        editManagerName = dialog.findViewById(R.id.editManagerName);

        btnConfirm = dialog.findViewById(R.id.btnConfirm);
        btnCancel = dialog.findViewById(R.id.btnCancel);

        kindergartenId = getIntent().getStringExtra("kindergarten_id");


        Button btnEdit = findViewById(R.id.editProfileButton);
        btnEdit.setOnClickListener(v -> {
            editManagerName.setText(tvManagerName.getText());

            dialog.show();
        });

// אישור עריכה
        btnConfirm.setOnClickListener(v -> {
            String newName = editManagerName.getText().toString();

            updateManagerProfile(newName);
            dialog.dismiss();
        });

// ביטול
        btnCancel.setOnClickListener(v -> dialog.dismiss());


    }
    private void updateManagerProfile(String name) {
        String managerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("managers").document(managerId)
                .update("managerName", name)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "הפרופיל עודכן בהצלחה", Toast.LENGTH_SHORT).show();
                    tvManagerName.setText(name);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "שגיאה בעדכון פרופיל", Toast.LENGTH_SHORT).show();
                });
    }


    private void loadManagerDetails() {
        // מזהה המנהל מתוך FirebaseAuth
        String managerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("managers").document(managerId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("managerName");
                        String email = documentSnapshot.getString("managerEmail");

                        tvManagerName.setText(name);

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManagerProfileActivity.this, "שגיאה בטעינת פרטי המנהל", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_profile) {
//            Intent i = new Intent(this, ManagerProfileActivity.class);
            return true;
        }
        else if (item.getItemId() == R.id.nav_ganHome){
            if (kindergartenId == null) {
                Toast.makeText(this, "לא התקבל מזהה גן", Toast.LENGTH_SHORT).show();
                return false;
            }

            Intent i = new Intent(this, ManagerKindergartenProfile.class);
            i.putExtra("kindergarten_id", kindergartenId);
            startActivity(i);
            return true;
        }

        return false;
    }
}