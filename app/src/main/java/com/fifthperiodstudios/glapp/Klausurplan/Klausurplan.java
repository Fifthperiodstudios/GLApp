package com.fifthperiodstudios.glapp.Klausurplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Klausurplan implements Serializable {

    public ArrayList<Klausur> klausuren;

    private String datum;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public Klausurplan() {
        this.klausuren = new ArrayList<>();
    }
    public ArrayList<Klausur> getKlausuren() {
        return klausuren;
    }

}
