package com.fifthperiodstudios.glapp;

public interface Login {
    void loggedInSuccessfully(String mobilKey);
    void loggedInWithoutCredentials();
    void loggedInWithWrongCredentials();
    void loggedInWithoutInternetConnection();
}
