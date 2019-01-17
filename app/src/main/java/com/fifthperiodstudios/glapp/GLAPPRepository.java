package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Login.LoginRepository;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;

public interface GLAPPRepository {
    interface Local {
        void getStundenplan(GLAPPRepository.StundenplanCallback callback);

        void getKlausurplan(GLAPPRepository.KlausurplanCallback callback);

        void saveKlausurplanToDisk(Klausurplan klausurplan);

        void saveStundenplanToDisk(Stundenplan stundenplan);

        void saveFarbenToDisk(Farben farben);

        Farben loadFarben();
    }

    interface Server {
        void getStundenplan(String stundenplanDatum, GLAPPRepository.StundenplanCallback callback);

        void getVertretungsplan(GLAPPRepository.VertretungsplanCallback callback);

        void getKlausurplan(String klausurplanDatum, GLAPPRepository.KlausurplanCallback callback);
    }

    interface StundenplanCallback {
        void onStundenplanLoaded(Stundenplan stundenplan);

        void onStundenplanError();

        void onNoInternetConnection(Stundenplan stundenplan);

        void onNoNewStundenplan();
    }


    interface KlausurplanCallback {
        void onKlausurplanLoaded(Klausurplan klausurplan);

        void onKlausurplanError();

        void onNoInternetConnection(Klausurplan klausurplan);

        void onNoNewKlausurplan();
    }

    interface VertretungsplanCallback {
        void onVertretungsplanLoaded(Vertretungsplan vertretungsplan);

        void onVertretungsplanError();

        void onNoInternetConnection();

        void onNoNewVertretungsplan();
    }

    void getStundenplan(GLAPPRepository.StundenplanCallback callback);

    void getVertretungsplan(GLAPPRepository.VertretungsplanCallback callback);

    void getKlausurplan(GLAPPRepository.KlausurplanCallback callback);

    void loadFarben();

    void setFarben(Stundenplan stundenplan);

    void updateFarbenOnDiskAndCache(Farben farben);

    Farben getFarben();

    Stundenplan getStundenplan();
}
