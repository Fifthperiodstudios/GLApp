package com.fifthperiodstudios.glapp.Login;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;

public interface LoginRepository {
    interface Server {
        void getMobilKey(String u, String p, MobilKeyCallback callback);
    }

    interface MobilKeyCallback {
        void onConnectionError();

        void onWrongCredentialsError();

        void onUsernameOrPassowordMissing();

        void onSuccess(String mobilKey);
    }

    void getMobilKey(String key, String u, String p, MobilKeyCallback callback);
}
