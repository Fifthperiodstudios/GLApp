package com.fifthperiodstudios.glapp.Klausurplan;
import android.util.Xml;


import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
/**
 * Created by ro_te on 07.12.2018.
 */

public class KlausurenplanParser {private static final String ns = null;

    public Klausurplan parseKlausurplan(InputStream in) throws XmlPullParserException, IOException {
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

    private Klausurplan readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        Klausurplan klausurplan = new Klausurplan();

        parser.require(XmlPullParser.START_TAG, ns, "Klausurplan");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Klausur")) {
                klausurplan.getKlausuren().add(readKlausur(parser));
            } else {
                skip(parser);
            }
        }
        return klausurplan;
    }



    private Klausur readKlausur(XmlPullParser parser) throws IOException, XmlPullParserException {
        Klausur klausur = new Klausur();
        parser.require(XmlPullParser.START_TAG, ns, "Klausur");
        String tag = parser.getName();
        if (tag.equals("Stunde")) {
            klausur.setDatum(parser.getAttributeValue(null, "Datum"));
            klausur.setIndividuell(Integer.valueOf(parser.getAttributeValue(null, "individuell")));
            klausur.setBezeichnung(parser.getAttributeValue(null, "bezeichnung"));
            klausur.setLehrkraft(parser.getAttributeValue(null, "lehrer"));
            klausur.setFach(parser.getAttributeValue(null, "fach"));
            klausur.setRaum(Integer.valueOf(parser.getAttributeValue(null, "raum")));
            klausur.setEnde(parser.getAttributeValue(null, "bisStd"));
            klausur.setStart(parser.getAttributeValue(null, "vonStd"));
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, ns, "Stunde");
        return klausur;
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
