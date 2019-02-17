package com.fifthperiodstudios.glapp.Login;

public interface LoginRepository {
    interface Server {
        void getMobilKey(boolean istLehrer, String u, String p, MobilKeyCallback callback);
    }

    interface MobilKeyCallback {
        void onConnectionError();

        void onWrongCredentialsError();

        void onUsernameOrPassowordMissing();

        void onSuccess(String mobilKey);
    }

    void getMobilKey(boolean istLehrer, String u, String p, MobilKeyCallback callback);
}
