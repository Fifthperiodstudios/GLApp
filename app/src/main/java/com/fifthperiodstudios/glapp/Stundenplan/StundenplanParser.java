package com.fifthperiodstudios.glapp.Stundenplan;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StundenplanParser {     // We don't use namespaces
    private static final String ns = null;


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
        stundenplan.setDatum(parser.getAttributeValue(null, "Timestamp"));
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

    private Stundenplan.Wochentag readWochentag(XmlPullParser parser, Stundenplan stundenplan) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Wochentag");
        ArrayList<Stunde> stunden = new ArrayList<Stunde>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Stunde") && !parser.getAttributeValue(null, "Fach").equals("")) {
                Stunde k = readStunde(parser);
                int z = stundenplan.getFaecher().indexOf(k.getFach());
                if (z != -1) {
                    k.setFach(stundenplan.getFaecher().get(z));
                } else {
                    stundenplan.getFaecher().add(k.getFach());
                }
                stunden.add(k);
            } else {
                skip(parser);
            }
        }
        return new Stundenplan.Wochentag(stunden);
    }

    private Stunde readStunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        Stunde stunde = new Stunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            stunde.setStunde(parser.getAttributeValue(null, "Std"));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));

            stunde.getFach().setKurs(parser.getAttributeValue(null, "Kurs"));
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
