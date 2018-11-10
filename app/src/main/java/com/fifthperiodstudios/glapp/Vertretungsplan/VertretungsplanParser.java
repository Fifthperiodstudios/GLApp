package com.fifthperiodstudios.glapp.Vertretungsplan;

import java.io.FileInputStream;

/**
 * Created by ro_te on 09.11.2018.
 */

public class VertretungsplanParser {
    public static Vertretungsplan parseVertretungsplan (FileInputStream in) {
        return new Vertretungsplan();
    }
}
