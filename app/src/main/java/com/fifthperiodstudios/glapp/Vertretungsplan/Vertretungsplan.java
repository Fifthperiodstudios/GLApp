package com.fifthperiodstudios.glapp.Vertretungsplan;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;
import com.fifthperiodstudios.glapp.Stundenplan.Stunde;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

import java.io.Serializable;
import java.util.ArrayList;

public class Vertretungsplan implements Serializable{
    public ArrayList<Vertretungsplan.VertretungsTag> vertretungstage;
    public ArrayList<VertretungsplanStunde> stunde;

    public Vertretungsplan() {
        this.vertretungstage = new ArrayList<>();
        this.stunde = new ArrayList<>();
    }

    public ArrayList<Vertretungsplan.VertretungsTag> getVertretungstage() {
        return vertretungstage;
    }
    public ArrayList<VertretungsplanStunde> getStunde() {
        return VertretungsTag.getStunden();
    }


    public static class VertretungsTag implements Serializable {
        public static ArrayList<VertretungsplanStunde> stunden;

        public VertretungsTag(ArrayList<VertretungsplanStunde> vertretungsplanStunde) {
            this.stunden = vertretungsplanStunde;
        }

        public static ArrayList<VertretungsplanStunde> getStunden() {
            return stunden;
        }
    }


}
