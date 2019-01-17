package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Farben implements Serializable {
    private HashMap<Fach, String> farbenFaecher;
    private ArrayList<String> standardFarben;
    private ArrayList<Integer> ints;

    public ArrayList<String> getStandardFarben() {
        return standardFarben;
    }

    public String getFarbeFach(Fach fach) {
        String color = farbenFaecher.get(fach);
        if (color != null) {
            return color;
        } else {
            int zufall;
            do{
                zufall = (int) (Math.random() * (standardFarben.size()-1));
            }while(ints.contains(zufall));
            ints.add(zufall);
            String farbe = standardFarben.get(zufall);
            farbenFaecher.put(fach, farbe);
            return farbe;
        }
    }

    public HashMap<Fach, String> getFarbenFaecher() {
        return farbenFaecher;
    }

    public Farben() {
        standardFarben = new ArrayList<String>();
        farbenFaecher = new HashMap<>();
        ints = new ArrayList<>();
        setupFarben(standardFarben);
    }

    public Fach getFach(Fach f){
        Fach[] faecher;
        faecher = farbenFaecher.keySet().toArray(new Fach[0]);
        for (Fach fach :
                faecher) {
            if(fach.equals(f)){
                return fach;
            }
        }
        return f;
    }

    public void setupFarben(ArrayList<String> colors) {
        colors.add("#1abc9c");
        colors.add("#16a085");
        colors.add("#f1c40f");
        colors.add("#f39c12");

        colors.add("#2ecc71");
        colors.add("#27ae60");
        colors.add("#e67e22");
        colors.add("#d35400");

        colors.add("#3498db");
        colors.add("#2980b9");
        colors.add("#e74c3c");
        colors.add("#c0392b");

        colors.add("#9b59b6");
        colors.add("#8e44ad");
        colors.add("#ecf0f1");
        colors.add("#bdc3c7");

        colors.add("#34495e");
        colors.add("#2c3e50");
        colors.add("#95a5a6");
        colors.add("#7f8c8d");

        colors.add("#E91E63");
        colors.add("#9C27B0");
        colors.add("#607D8B");
        colors.add("#795548");
    }

}