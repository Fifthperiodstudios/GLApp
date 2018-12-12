package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Vertretungsplan implements Serializable {
    public ArrayList<Vertretungsplan.VertretungsTag> vertretungstage;
    public ArrayList<VertretungsplanStunde> stunden;

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

    @NonNull
    public String toString() {
        String s = "";
        s += "Anzahl Vertretungstage: " + vertretungstage.size() + "\n";
        for (int i = 0; i < vertretungstage.size(); i++) {
            s += "Vertretungstag " + i + ": \n";
            for (int j = 0; j < vertretungstage.get(i).getStunden().size(); j++) {
                s += "Stunde " + j + ": " + vertretungstage.get(i).getStunden().get(j).getFach().getFach() + "\n";
            }
        }
        return s;
    }
}
