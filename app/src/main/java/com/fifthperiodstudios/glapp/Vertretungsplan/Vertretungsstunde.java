package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.util.Log;

import com.fifthperiodstudios.glapp.R;
import com.fifthperiodstudios.glapp.Stundenplan.Fach;

import org.simpleframework.xml.Attribute;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Vertretungsstunde implements Serializable {

    private String datum;

    private String stunde;

    private String raum;

    private String raumNeu;

    private String bemerkung;

    private String vertretungsLehrer;

    private Fach fach;

    public Vertretungsstunde() {
        fach = new Fach();
    }
    // Anfang Methoden
    public String getStunde() {
        return stunde;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    public String getRaumNeu() {
        return raumNeu;
    }

    public void setRaumNeu(String raumNeu) {
        this.raumNeu = raumNeu;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkungNeu) {
        bemerkung = bemerkungNeu.replace('?', 'ä');
    }

    public String getFLehrer() {
        return fach.getLehrer();
    }

    public void setFachLehrer(String fLehrerNeu) {
        fach.setLehrer(fLehrerNeu);
    }

    public String getVLehrer() {
        return vertretungsLehrer;
    }

    public void setVertretungsLehrer(String vLehrerNeu) {
        vertretungsLehrer = vLehrerNeu;
    }

    public Fach getFach() {
        return fach;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDatumAlsText(){
        SimpleDateFormat ft = new SimpleDateFormat("EE, dd.MM.yyyy");
        try {
            Date date = ft.parse(datum);
            ft.applyPattern("EE");
            return ft.format(date);
        } catch (ParseException e) {
            return "Etwas ist schiefgelaufen";
        }
    }

    public void setStunde(String stunde) {
        this.stunde = stunde;
    }

    public void setFach(Fach fach) {
        this.fach = fach;
    }
}

