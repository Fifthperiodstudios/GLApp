package com.fifthperiodstudios.glapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StundenplanParser {     // We don't use namespaces
    private static final String ns = null;

    public static class Wochentag implements Serializable {
        public final ArrayList<Stunde> stunden;

        public Wochentag(ArrayList<Stunde> stunden) {
            this.stunden = stunden;
        }

        public ArrayList<Stunde> getStunden() {
            return stunden;
        }
    }

    public static class Stundenplan implements Serializable {
        public final ArrayList<Wochentag> wochentage;
        public final ArrayList<Fach> fächer;

        public Stundenplan() {
            this.wochentage = new ArrayList<>();
            this.fächer = new ArrayList<>();
        }

        public ArrayList<Fach> getFächer() {
            return fächer;
        }

        public ArrayList<Wochentag> getWochentage() {
            return wochentage;
        }

    }

    public String parseKey (InputStream in) throws IOException {
        try {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } finally {
            in.close();
        }
    }


    public Stundenplan parseStundenplan(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Stundenplan readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Stundenplan stundenplan = new Stundenplan();

        parser.require(XmlPullParser.START_TAG, ns, "Stundenplan");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Wochentag")) {
                stundenplan.getWochentage().add(readWochentag(parser, stundenplan));
            } else {
                skip(parser);
            }
        }
        return stundenplan;
    }

    private Wochentag readWochentag(XmlPullParser parser, Stundenplan stundenplan) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Wochentag");
        ArrayList<Stunde> stunden = new ArrayList<Stunde>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Stunde") && !parser.getAttributeValue(null, "Kurs").equals("")) {
                Stunde k = readStunde(parser);
                int z = stundenplan.getFächer().indexOf(k.getFach());
                if (z != -1) {
                    k.setFach(stundenplan.getFächer().get(z));
                } else {
                    stundenplan.getFächer().add(k.getFach());
                }
                stunden.add(k);
            } else {
                skip(parser);
            }
        }
        return new Wochentag(stunden);
    }

    private Stunde readStunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        Stunde stunde = new Stunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            stunde.setStunde(parser.getAttributeValue(null, "Std"));
            stunde.getFach().setKurs(parser.getAttributeValue(null, "Kurs"));
            stunde.getFach().setRaum(parser.getAttributeValue(null, "Raum"));
            stunde.getFach().setKursart(parser.getAttributeValue(null, "Kursart"));
            stunde.getFach().setLehrer(parser.getAttributeValue(null, "Lehrer"));
            stunde.getFach().setFach(parser.getAttributeValue(null, "Fach"));
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, ns, "Stunde");
        return stunde;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }

    }

}
