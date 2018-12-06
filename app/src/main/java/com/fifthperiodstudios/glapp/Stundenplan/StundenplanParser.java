package com.fifthperiodstudios.glapp.Stundenplan;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public Date parseDatum(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readDatum(parser);
        } finally {
            in.close();
        }
    }

    private Date parseDateFromString (String date){
        SimpleDateFormat dateparser = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date datum;
        try {
            datum = dateparser.parse(date);
        } catch (ParseException e) {
            datum = new Date (0);
        }
        return datum;
    }

    private Date readDatum (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Stundenplan");
        return parseDateFromString(parser.getAttributeValue(null, "Datum"));
    }

    private Stundenplan readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Stundenplan stundenplan = new Stundenplan();

        parser.require(XmlPullParser.START_TAG, ns, "Stundenplan");
        stundenplan.setDatum(parseDateFromString(parser.getAttributeValue(null, "Datum")));
        Log.d("TAG", stundenplan.getDatum().toString());
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
        return new Stundenplan.Wochentag(stunden);
    }

    private Stunde readStunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        Stunde stunde = new Stunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            stunde.setStunde(parser.getAttributeValue(null, "Std"));
            stunde.getFach().setKurs(parser.getAttributeValue(null, "Kurs"));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));
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
