package com.example.finalprojectshir2.callbacks;

import com.example.finalprojectshir2.models.KinderGarten;
import com.example.finalprojectshir2.models.User;

public interface KinderGartenCallback {
    public  void onError(String massage);
    public void onSuccess (KinderGarten garden);
}
