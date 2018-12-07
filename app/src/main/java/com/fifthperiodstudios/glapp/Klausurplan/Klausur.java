package com.fifthperiodstudios.glapp.Klausurplan;

/**
 * Created by ro_te on 07.12.2018.
 */

public class Klausur {

    // Anfang Attribute
    private String Datum;
    private String Start;
    private String Ende;
    private int Raum;
    private String Fach;
    private String Lehrkraft;
    private int Individuell;
    private String Bezeichnung;
    // Ende Attribute

    // Anfang Methoden
    public String getDatum() {
        return Datum;
    }
    public int getIndividuell() {
        return Individuell;
    }
    public String getBezeichnung() {
        return Bezeichnung;
    }

    public void setBezeichnung(String BezeichnungNeu) {
        Bezeichnung = BezeichnungNeu;
    }
    public void setIndividuell(int IndividuellNeu) {
        Individuell = IndividuellNeu;
    }
    public void setDatum(String DatumNeu) {
        Datum = DatumNeu;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String StartNeu) {
        Start = StartNeu;
    }

    public String getEnde() {
        return Ende;
    }

    public void setEnde(String EndeNeu) {
        Ende = EndeNeu;
    }

    public int getRaum() {
        return Raum;
    }

    public void setRaum(int RaumNeu) {
        Raum = RaumNeu;
    }

    public String getFach() {
        return Fach;
    }

    public void setFach(String FachNeu) {
        Fach = FachNeu;
    }

    public String getLehrkraft() {
        return Lehrkraft;
    }

    public void setLehrkraft(String LehrkraftNeu) {
        Lehrkraft = LehrkraftNeu;
    }

    // Ende Methoden
} // end of Klausur

