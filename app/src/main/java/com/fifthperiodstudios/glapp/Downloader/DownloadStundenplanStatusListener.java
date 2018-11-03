package com.fifthperiodstudios.glapp.Downloader;

import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

public interface DownloadStundenplanStatusListener {
    void keineInternetverbindung (Stundenplan stundenplan);
    void fertigHeruntergeladen (Stundenplan stundenplan);
    void andererFehler ();
}
