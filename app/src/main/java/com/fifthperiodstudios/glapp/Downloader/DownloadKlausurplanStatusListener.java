package com.fifthperiodstudios.glapp.Downloader;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;

public interface DownloadKlausurplanStatusListener {
    void keineInternetverbindung(Klausurplan klausurplan);
    void fertigHeruntergeladen(Klausurplan klausurplan);
    void andererFehler();
}
