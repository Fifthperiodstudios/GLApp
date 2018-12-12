package com.fifthperiodstudios.glapp.Vertretungsplan;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Beschreibung
 *
 * @author
 * @version 1.0 vom 09.11.2018
 */

public class VertretungsplanStunde implements Serializable {

    // Anfang Attribute
    private int Stunde;
    private String Raum;
    private Fach fach;
    private String RaumNeu;
    private String Bemerkung;
    private String FLehrer;
    private String VLehrer;
    private String fachName;
    private Date datum;

    public VertretungsplanStunde() {
        this.Stunde = 0;
        this.Raum = "";
        this.RaumNeu = "";
        this.Bemerkung = "";
        this.FLehrer = "";
        this.VLehrer = "";
        this.fachName = "";
    }

    // Anfang Methoden
    public int getStunde() {
        return Stunde;
    }

    public void setStunde(int StundeNeu) {
        Stunde = StundeNeu;
    }

    public String getRaum() {
        return Raum;
    }

    public void setRaum(String RaumNeu) {
        Raum = RaumNeu;
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach FachNeu) {
        this.fach = FachNeu;
    }

    public String getRaumNeu() {
        return RaumNeu;
    }

    public void setRaumNeu(String RaumNeuNeu) {
        RaumNeu = RaumNeuNeu;
    }

    public String getBemerkung() {
        return Bemerkung;
    }

    public void setBemerkung(String BemerkungNeu) {
        Bemerkung = BemerkungNeu;
    }

    public String getFLehrer() {
        return FLehrer;
    }

    public void setFLehrer(String FLehrerNeu) {
        FLehrer = FLehrerNeu;
    }

    public String getVLehrer() {
        return VLehrer;
    }

    public void setVLehrer(String VLehrerNeu) {
        VLehrer = VLehrerNeu;
    }

    public void setFachName(String fachNameNeu) {
        fachName = fachNameNeu;
    }

    public String getFachName() {
        return fachName;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getDatumAlsText(){
        SimpleDateFormat ft =
                new SimpleDateFormat ("EE");
        return ft.format(datum);
    }
    // Ende Methoden
} // end of VertretungsplanStunde

