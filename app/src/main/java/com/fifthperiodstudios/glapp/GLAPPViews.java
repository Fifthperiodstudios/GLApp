package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;

public interface GLAPPViews {
    interface KlausurplanView {
        void keineInternetverbindung(Klausurplan klausurplan, Farben farben);

        void fertigHeruntergeladen(Klausurplan klausurplan, Farben farben);

        void andererFehler();

        void setPresenter(GLAPPPresenter presenter);

        void updateFarben(Farben farben);

    }

    interface StundenplanView {
        void keineInternetverbindung(Stundenplan stundenplan, Farben farben);

        void fertigHeruntergeladen(Stundenplan stundenplan, Farben farben);

        void andererFehler();

        void setPresenter(GLAPPPresenter presenter);

        void updateFarben(Farben farben);
    }

    interface VertretungsplanView {
        void keineInternetverbindung();

        void fertigHeruntergeladen(Vertretungsplan vertretungsplan, Farben farben);

        void andererFehler();

        void setPresenter(GLAPPPresenter presenter);

        void updateFarben(Farben farben);
    }

}

