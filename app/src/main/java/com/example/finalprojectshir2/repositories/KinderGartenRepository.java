package com.example.finalprojectshir2.repositories;

import com.example.finalprojectshir2.Parent.Register.RegisterActivity;
import com.example.finalprojectshir2.callbacks.KinderGartenCallback;
import com.example.finalprojectshir2.models.KinderGarten;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class KinderGartenRepository {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public KinderGartenRepository(){
      this.auth = FirebaseAuth.getInstance();
      this.database = FirebaseFirestore.getInstance();

    }
    public void addKinderGarden(KinderGarten kinderGarten, KinderGartenCallback callback){

    }

}
