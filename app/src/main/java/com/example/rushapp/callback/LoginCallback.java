package com.example.rushapp.callback;

public interface LoginCallback {
    void onLoginSuccess(boolean isCustomer);
    void onLoginFailure();
}
