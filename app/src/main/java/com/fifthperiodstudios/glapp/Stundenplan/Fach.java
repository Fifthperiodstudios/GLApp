package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;

public class Fach implements Serializable {
    private String color, kurs, raum, kursart, lehrer, fach;

    public Fach (){
        color = "#ffffff";
        kurs = "";
        raum = "";
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKurs() {
        return kurs;
    }

    public void setKurs(String kurs) {
        this.kurs = kurs;
    }

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
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
        if (this.kurs.equals(fach.getKurs())) {
            return true;
        }
        return false;
    }
}
