package com.fifthperiodstudios.glapp.Stundenplan;

import java.io.Serializable;

public class Stunde implements Serializable{
    private String stunde;
    private String raum;

    public String getRaum() {
        return raum;
    }

    public void setRaum(String raum) {
        this.raum = raum;
    }

    private Fach fach;

    public Stunde (){
        fach = new Fach();
    }

    public Fach getFach() {
        return fach;
    }

    public void setFach(Fach f){
        fach = f;
    }

    public String getStunde() {
        return stunde;
    }

    public void setStunde(String stunde) {
        this.stunde = stunde;
    }
}
