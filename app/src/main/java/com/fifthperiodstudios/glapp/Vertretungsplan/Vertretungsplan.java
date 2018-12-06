package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.support.annotation.NonNull;

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

    @NonNull
    public String toString(){
        String s = "";
        s += "Anzahl Vertretungstage: " + vertretungstage.size() + "\n";
        for (int i = 0; i < vertretungstage.size(); i++) {
            s += "Vertretungstag " + i + ": \n";
            for (int j = 0; j < vertretungstage.get(i).getStunden().size(); j++) {
                s += "Stunde " + j + ": " + vertretungstage.get(i).getStunden().get(j).getFach().getFach() + "\n";
            }
        }
        return  s;
    }
}
