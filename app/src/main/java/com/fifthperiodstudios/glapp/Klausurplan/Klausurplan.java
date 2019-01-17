package com.fifthperiodstudios.glapp.Klausurplan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
