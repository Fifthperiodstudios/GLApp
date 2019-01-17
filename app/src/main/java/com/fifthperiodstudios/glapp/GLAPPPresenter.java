package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

public interface GLAPPPresenter {
    void downloadStundenplan();
    void downloadKlausurplan();
    void downloadVertretungsplan();
    void loadFarben();
    void updateFarben(Farben farben);
    Farben getFarben();
    Stundenplan getStundenplan();
}
