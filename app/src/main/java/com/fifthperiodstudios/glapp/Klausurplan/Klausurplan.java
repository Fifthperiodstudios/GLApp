package com.fifthperiodstudios.glapp.Klausurplan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Klausurplan implements Serializable {

    public ArrayList<Klausur> klausuren;

    private Date datum;

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public void setDatum(String datum) {
        SimpleDateFormat dateparser = new SimpleDateFormat("dd.MM.yyyy kk:mm:ss");
        try {
            this.datum = dateparser.parse(datum);
        } catch (ParseException e) {
            this.datum = new Date(0);
        }
    }

    public Date getDatum() {
        return datum;
    }

    public Klausurplan() {
        this.klausuren = new ArrayList<>();
    }
    public ArrayList<Klausur> getKlausuren() {
        return klausuren;
    }

}
