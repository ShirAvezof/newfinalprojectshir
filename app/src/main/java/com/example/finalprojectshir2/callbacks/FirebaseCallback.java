package com.example.finalprojectshir2.callbacks;

public interface FirebaseCallback<T> {

    public void onSuccess(T t);
    public void onError(String error);


}
