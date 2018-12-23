package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Stundenplan implements Serializable {
    private ArrayList<Wochentag> wochentage;
    private ArrayList<Fach> fächer;
    private Date datum;

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setDatum(String datum) {
        SimpleDateFormat dateparser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            this.datum = dateparser.parse(datum);
        } catch (ParseException e) {
            this.datum = new Date(0);
        }
    }

    public Date getDatum() {
        return datum;
    }

    public boolean isNewer(Date date) {
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

