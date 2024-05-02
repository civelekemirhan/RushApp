package com.example.rushapp;

public interface LoginCallback {
    void onLoginSuccess(boolean isCustomer);
    void onLoginFailure();
}
