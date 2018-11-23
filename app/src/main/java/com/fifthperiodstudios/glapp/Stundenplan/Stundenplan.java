package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Stundenplan implements Serializable {
    public ArrayList<Wochentag> wochentage;
    public ArrayList<Fach> fächer;
    public Date datum;

    public void setDatum (Date datum) {
        this.datum = datum;
    }

    public Date getDatum () {
        return datum;
    }

    public boolean isNewer (Date date) {
        return datum.before(date);
    }

    public Stundenplan() {
        this.wochentage = new ArrayList<>();
        this.fächer = new ArrayList<>();
    }

    public ArrayList<Fach> getFächer() {
        return fächer;
    }

    public ArrayList<Wochentag> getWochentage() {
        return wochentage;
    }


    public static class Wochentag implements Serializable {
        public ArrayList<Stunde> stunden;

        public Wochentag(ArrayList<Stunde> stunden) {
            this.stunden = stunden;
        }

        public ArrayList<Stunde> getStunden() {
            return stunden;
        }
    }

}

