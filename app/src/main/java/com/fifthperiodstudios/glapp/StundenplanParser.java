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

    public static class Wochentag implements Serializable{
        public final ArrayList<Stunde> stunden;

        public Wochentag(ArrayList<Stunde> stunden) {
            this.stunden = stunden;
        }

        public ArrayList<Stunde> getStunden(){
            return stunden;
        }
    }

    public List parse(InputStream in) throws XmlPullParserException, IOException {
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

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List wochenTage = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "Stundenplan");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Wochentag")) {
                wochenTage.add(readWochentag(parser));
            } else {
                skip(parser);
            }
        }
        return wochenTage;
    }

    private Wochentag readWochentag(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Wochentag");
        ArrayList<Stunde> stunden = new ArrayList<Stunde>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Stunde") && !parser.getAttributeValue(null, "Kurs").equals("")) {
                stunden.add(readStunde(parser));
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
            stunde.setKurs(parser.getAttributeValue(null, "Kurs"));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));
            stunde.setFach(parser.getAttributeValue(null, "Fach"));
            stunde.setKursart(parser.getAttributeValue(null, "Std"));
            stunde.setLehrer(parser.getAttributeValue(null, "Lehrer"));
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
