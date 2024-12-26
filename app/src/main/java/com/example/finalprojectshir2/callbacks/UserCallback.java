package com.example.finalprojectshir2.callbacks;

import com.example.finalprojectshir2.models.User;

public interface UserCallback {
    public void onError(String message);
    public void onSuccess(User user);
}
