package com.fifthperiodstudios.glapp.Klausurplan;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ro_te on 07.12.2018.
 */

public class Klausur implements Serializable {

    // Anfang Attribute
    private String datum;
    private String start;
    private String ende;
    private String raum;
    private Fach fach;
    private String lehrkraft;
    private int individuell;
    private String bezeichnung;

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnde() {
        return ende;
    }

    public void setEnde(String ende) {
        this.ende = ende;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach fach) {
        this.fach = fach;
    }

    public String getLehrkraft() {
        return lehrkraft;
    }

    public void setLehrkraft(String lehrkraft) {
        this.lehrkraft = lehrkraft;
    }

    public int getIndividuell() {
        return individuell;
    }

    public void setIndividuell(int individuell) {
        this.individuell = individuell;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}

