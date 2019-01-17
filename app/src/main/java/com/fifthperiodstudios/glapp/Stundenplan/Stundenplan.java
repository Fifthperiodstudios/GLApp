package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Stundenplan implements Serializable {
    private ArrayList<Wochentag> wochentage;
    private ArrayList<Fach> faecher;
    private String datum;

    public Stundenplan() {
        this.wochentage = new ArrayList<>();
        this.faecher = new ArrayList<>();
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDatum() {
        return datum;
    }

    public ArrayList<Fach> getFaecher() {
        return faecher;
    }

    public ArrayList<Wochentag> getWochentage() {
        return wochentage;
    }

    public static class Wochentag implements Serializable {
        private ArrayList<Stunde> stunden;

        public Wochentag(ArrayList<Stunde> stunden) {
            this.stunden = stunden;
        }

        public Wochentag() {
            stunden = new ArrayList<>();
        }

        public ArrayList<Stunde> getStunden() {
            return stunden;
        }
    }
}