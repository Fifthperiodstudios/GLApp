package com.fifthperiodstudios.glapp.Vertretungsplan;

import java.io.Serializable;

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 09.11.2018
 * @author 
 */

public class VertretungsplanStunde implements Serializable {
  
  // Anfang Attribute
  private int Stunde;
  private String Klasse;
  private String Raum;
  private String Fach;
  private String RaumNeu;
  private String Bemerkung;
  private String FLehrer;
  private String VLehrer;
  // Ende Attribute
  
  public VertretungsplanStunde() {
    this.Stunde = 0;
    this.Klasse = "";
    this.Raum = "";
    this.Fach = "";
    this.RaumNeu = "";
    this.Bemerkung = "";
    this.FLehrer = "";
    this.VLehrer = "";
  }

  // Anfang Methoden
  public int getStunde() {
    return Stunde;
  }

  public void setStunde(int StundeNeu) {
    Stunde = StundeNeu;
  }

  public String getKlasse() {
    return Klasse;
  }

  public void setKlasse(String KlasseNeu) {
    Klasse = KlasseNeu;
  }

  public String getRaum() {
    return Raum;
  }

  public void setRaum(String RaumNeu) {
    Raum = RaumNeu;
  }

  public String getFach() {
    return Fach;
  }

  public void setFach(String FachNeu) {
    Fach = FachNeu;
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

  // Ende Methoden
} // end of VertretungsplanStunde

