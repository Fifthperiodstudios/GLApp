package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VertretungsplanParser {     // We don't use namespaces
    private static final String ns = null;

    public Vertretungsplan parseVertretungsplan(InputStream in) throws XmlPullParserException, IOException {
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

    private Vertretungsplan readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Vertretungsplan vertretungsplan = new Vertretungsplan();

        parser.require(XmlPullParser.START_TAG, ns, "Vertretungsplan");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Vertretungstag")) {
                vertretungsplan.getVertretungstage().add(readVertretungstag(parser, vertretungsplan));
            } else {
                skip(parser);
            }
        }
        return vertretungsplan;
    }

    private Vertretungsplan.VertretungsTag readVertretungstag(XmlPullParser parser, Vertretungsplan vertretungsplan) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Vertretungstag");
        ArrayList<VertretungsplanStunde> stunden = new ArrayList<VertretungsplanStunde>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Stunde") && !parser.getAttributeValue(null, "Kurs").equals("")) {
                VertretungsplanStunde k = readVertretungsplanStunde(parser);
                stunden.add(k);
            } else {
                skip(parser);
            }
        }
        return new Vertretungsplan.VertretungsTag(stunden);
    }

    private VertretungsplanStunde readVertretungsplanStunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        VertretungsplanStunde stunde = new VertretungsplanStunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            stunde.setStunde(Integer.valueOf(parser.getAttributeValue(null, "Std")));
            stunde.setKlasse(parser.getAttributeValue(null, "Klasse"));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));
            stunde.setFach(parser.getAttributeValue(null, "Fach"));
            stunde.setRaumNeu(parser.getAttributeValue(null, "RaumNeu"));
            stunde.setBemerkung(parser.getAttributeValue(null, "Bemerkung"));
            stunde.setFLehrer(parser.getAttributeValue(null, "FLehrer"));
            stunde.setVLehrer(parser.getAttributeValue(null, "VLehrer"));
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
