package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Vertretungsplan implements Serializable {
    private ArrayList<Vertretungsplan.VertretungsTag> vertretungstage;
    private ArrayList<VertretungsplanStunde> stunden;
    private Date datum;

    public Vertretungsplan() {
        this.vertretungstage = new ArrayList<>();
        this.stunden = new ArrayList<>();
    }

    public ArrayList<Vertretungsplan.VertretungsTag> getVertretungstage() {
        return vertretungstage;
    }

    public ArrayList<VertretungsplanStunde> getStunden() {
        return stunden;
    }

    public void setStunden(ArrayList<VertretungsplanStunde> neueStunden) {
        stunden = neueStunden;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        SimpleDateFormat dateparser = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            this.datum = dateparser.parse(datum);
        } catch (ParseException e) {
            this.datum = new Date(0);
        }
    }

    public static class VertretungsTag implements Serializable {
        private ArrayList<VertretungsplanStunde> stunden;
        private Date datum;

        public VertretungsTag(ArrayList<VertretungsplanStunde> vertretungsplanStunde) {
            this.stunden = vertretungsplanStunde;
        }

        public VertretungsTag() {
            stunden = new ArrayList<>();
        }

        public ArrayList<VertretungsplanStunde> getStunden() {
            return stunden;
        }

        public Date getDatum() {
            return datum;
        }

        public void setDatum(Date datum) {
            this.datum = datum;
        }
    }
}
