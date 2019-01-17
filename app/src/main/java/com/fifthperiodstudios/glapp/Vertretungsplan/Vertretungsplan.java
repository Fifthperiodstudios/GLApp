package com.fifthperiodstudios.glapp.Vertretungsplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Vertretungsplan implements Serializable {
    private ArrayList<Vertretungstag> vertretungstage;
    private ArrayList<Vertretungsstunde> stunden;
    private ArrayList<String> informationen;

    private String datum;

    public Vertretungsplan() {
        this.vertretungstage = new ArrayList<>();
        this.stunden = new ArrayList<>();
        informationen = new ArrayList<>();

    }

    public void setVertretungstage(ArrayList<Vertretungstag> vertretungstage) {
        this.vertretungstage = vertretungstage;
    }

    public ArrayList<String> getInformationen() {
        return informationen;
    }

    public void setInformationen(ArrayList<String> informationen) {
        this.informationen = informationen;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public ArrayList<Vertretungstag> getVertretungstage() {
        return vertretungstage;
    }

    public ArrayList<Vertretungsstunde> getStunden() {
        return stunden;
    }

    public void setStunden(ArrayList<Vertretungsstunde> neueStunden) {
        stunden = neueStunden;
    }

    public String getDatum() {
        return datum;
    }
}
