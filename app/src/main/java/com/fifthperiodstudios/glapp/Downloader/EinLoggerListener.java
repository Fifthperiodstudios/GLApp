package com.fifthperiodstudios.glapp.Downloader;

public interface EinLoggerListener {
    void falscheDaten ();
    void keineInternetverbindung ();
    void eingeloggt (String mobilKey);
    void unvollstaendig ();
    void andererFehler ();
}
