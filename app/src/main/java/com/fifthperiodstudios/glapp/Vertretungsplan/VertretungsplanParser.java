package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.util.Log;
import android.util.Xml;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                Date date = parseDateFromString(parser.getAttributeValue(null, "Datum"));
                vertretungsplan.getVertretungstage().add(readVertretungsplanTag(parser, vertretungsplan, date));
            } else {
                skip(parser);
            }
        }
        return vertretungsplan;
    }
int i = 0;
    private Date parseDateFromString(String date) {
        SimpleDateFormat dateparser = new SimpleDateFormat("EE, dd.MM.yyyy");
        Date datum;
        try {
            datum = dateparser.parse(date);
        } catch (ParseException e) {
            datum = new Date(0);
        }
        return datum;
    }

    private Vertretungsplan.VertretungsTag readVertretungsplanTag(XmlPullParser parser, Vertretungsplan vertretungsplan, Date date) throws IOException, XmlPullParserException {
        Vertretungsplan.VertretungsTag vertretungsTag = new Vertretungsplan.VertretungsTag();
        vertretungsTag.setDatum(date);
        parser.require(XmlPullParser.START_TAG, ns, "Vertretungstag");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Stunde")) {
                VertretungsplanStunde stunde = readVertretungsplanStunde(parser);
                stunde.setDatum(date);
                vertretungsTag.getStunden().add(stunde);
                vertretungsplan.getStunden().add(stunde);
            } else {
                skip(parser);
            }
        }
        return vertretungsTag;
    }

    private VertretungsplanStunde readVertretungsplanStunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        VertretungsplanStunde stunde = new VertretungsplanStunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            stunde.setStunde(Integer.valueOf(parser.getAttributeValue(null, "Std")));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));
            Fach fach = new Fach();
            fach.setFach(parser.getAttributeValue(null, "Fach"));
            stunde.setFach(fach);
            stunde.setRaumNeu(parser.getAttributeValue(null, "RaumNeu"));
            stunde.setBemerkung(parser.getAttributeValue(null, "Bemerkung"));
            stunde.setFLehrer(parser.getAttributeValue(null, "FLehrer"));
            stunde.setVLehrer(parser.getAttributeValue(null, "VLehrer"));
            stunde.setFachName(parser.getAttributeValue(null, "Fach"));
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
