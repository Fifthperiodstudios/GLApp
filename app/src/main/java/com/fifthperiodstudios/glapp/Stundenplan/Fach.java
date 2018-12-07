package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;
import java.util.Objects;

public class Fach implements Serializable {
    private String kurs, kursart, lehrer, fach;

    public Fach (){
        kurs = "";
        kursart = "";
        lehrer = "";
        fach = "";
    }
    public String getFach() {
        return fach;
    }

    public void setFach(String fach) {
        this.fach = fach;
    }

    public String getKurs() {
        return kurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public String getKursart() {
        return kursart;
    }

    public void setKursart(String kursart) {
        this.kursart = kursart;
    }

    public String getLehrer() {
        return lehrer;
    }

    public void setLehrer(String lehrer) {
        this.lehrer = lehrer;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Fach)) {
            return false;
        }
        Fach fach = (Fach) other;
        return this.fach.equals(fach.getFach()) || this.kurs.equals(fach.getKurs());
    }

    @Override
    public int hashCode() {
        return kurs.hashCode();
    }

    public String getVollenName(){
        if(kursart.contains("LK")){
            return fach+"LK";
        }
        return fach;
    }
}
