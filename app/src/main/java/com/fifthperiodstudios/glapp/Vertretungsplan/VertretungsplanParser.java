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
        vertretungsplan.setDatum(parser.getAttributeValue(null, "Timestamp"));
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Vertretungstag")) {
                String datum = parser.getAttributeValue(null, "Datum");
                vertretungsplan.getVertretungstage().add(readVertretungsplanTag(parser, vertretungsplan, datum));
            } else if(name.equals("Informationen")) {
                vertretungsplan.setInformationen(readInformationen(parser));
            }else{
                skip(parser);
            }
        }
        return vertretungsplan;
    }


    private Vertretungstag readVertretungsplanTag(XmlPullParser parser, Vertretungsplan vertretungsplan, String date) throws IOException, XmlPullParserException {
        Vertretungstag vertretungsTag = new Vertretungstag();
        vertretungsTag.setDatum(date);
        parser.require(XmlPullParser.START_TAG, ns, "Vertretungstag");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Stunde")) {
                Vertretungsstunde stunde = readVertretungsstunde(parser);
                stunde.setDatum(date);
                vertretungsTag.getStunden().add(stunde);
                vertretungsplan.getStunden().add(stunde);
            } else {
                skip(parser);
            }
        }
        return vertretungsTag;
    }

    private ArrayList<String> readInformationen(XmlPullParser parser) throws IOException, XmlPullParserException {
        ArrayList<String> informationen = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, "Informationen");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Info")) {
                informationen.add(parser.getAttributeValue(null, "text"));
            } else {
                skip(parser);
            }
        }
        return informationen;
    }

    private Vertretungsstunde readVertretungsstunde(XmlPullParser parser) throws IOException, XmlPullParserException {
        Vertretungsstunde stunde = new Vertretungsstunde();
        parser.require(XmlPullParser.START_TAG, ns, "Stunde");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            Fach fach = new Fach();
            stunde.setStunde(parser.getAttributeValue(null, "Std"));
            stunde.setRaum(parser.getAttributeValue(null, "Raum"));
            stunde.setRaumNeu(parser.getAttributeValue(null, "RaumNeu"));
            stunde.setBemerkung(parser.getAttributeValue(null, "Bemerkung"));
            stunde.setVertretungsLehrer(parser.getAttributeValue(null, "VLehrer"));

            fach.setLehrer(parser.getAttributeValue(null, "FLehrer"));
            fach.setFach(parser.getAttributeValue(null, "Fach"));
            stunde.setFach(fach);

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