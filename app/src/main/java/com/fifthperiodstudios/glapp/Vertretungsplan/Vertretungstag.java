package com.fifthperiodstudios.glapp.Vertretungsplan;

import java.util.ArrayList;


public class Vertretungstag {
    private ArrayList<Vertretungsstunde> stunden;
    private String datum;

    public Vertretungstag(ArrayList<Vertretungsstunde> vertretungsplanStunde) {
        this.stunden = vertretungsplanStunde;
    }

    public Vertretungstag() {
        stunden = new ArrayList<>();
    }

    public ArrayList<Vertretungsstunde> getStunden() {
        return stunden;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setStunden(ArrayList<Vertretungsstunde> stunden) {
        this.stunden = stunden;
    }
}
