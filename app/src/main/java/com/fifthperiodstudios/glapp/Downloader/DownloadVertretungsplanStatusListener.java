package com.fifthperiodstudios.glapp.Downloader;

import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;

public interface DownloadVertretungsplanStatusListener {
    void keineInternetverbindung();
    void fertigHeruntergeladen(Vertretungsplan vertretungsplan);
    void andererFehler();
}
