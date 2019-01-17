package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stunde;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;
import com.fifthperiodstudios.glapp.util.AppExecutors;

import java.io.File;

public class GLAPPRepositoryImpl implements GLAPPRepository {
    private final String stundenplanDatum;
    private final String vertretungsplanDatum;
    private final String klausurplanDatum;
    private AppExecutors appExecutors;
    private ServerDataHandler serverDataHandler;
    private LocalDataHandler localDataHandler;

    private File stundenplanFile;
    private File klausurplanFile;

    private Stundenplan stundenplan;
    private Klausurplan klausurplan;
    private Vertretungsplan vertretungsplan;
    private Farben farben;


    public GLAPPRepositoryImpl(String mobilKey, File stundenplanFile, File klausurplanFile, File farbenFile, String stundenplanDatum, String vertretungsplanDatum, String klausurplanDatum) {
        appExecutors = new AppExecutors();

        this.stundenplanFile = stundenplanFile;
        this.klausurplanFile = klausurplanFile;

        this.stundenplanDatum = stundenplanDatum;
        this.vertretungsplanDatum = vertretungsplanDatum;
        this.klausurplanDatum = klausurplanDatum;

        serverDataHandler = new ServerDataHandler(mobilKey, appExecutors);
        localDataHandler = new LocalDataHandler(appExecutors, stundenplanFile, klausurplanFile, farbenFile);
    }


    @Override
    public void getStundenplan(final StundenplanCallback callback) {
        serverDataHandler.getStundenplan(stundenplanDatum, new StundenplanCallback() {
            @Override
            public void onStundenplanLoaded(Stundenplan stundenplan) {
                setStundenplan(stundenplan);
                callback.onStundenplanLoaded(stundenplan);
                localDataHandler.saveStundenplanToDisk(stundenplan);
            }

            @Override
            public void onStundenplanError() {
                callback.onStundenplanError();
            }

            @Override
            public void onNoInternetConnection(Stundenplan stundenplan) {
                localDataHandler.getStundenplan(new StundenplanCallback() {
                    @Override
                    public void onStundenplanLoaded(Stundenplan stundenplan) {
                        setStundenplan(stundenplan);
                        callback.onNoInternetConnection(stundenplan);
                    }

                    @Override
                    public void onStundenplanError() {
                        callback.onStundenplanError();
                    }

                    @Override
                    public void onNoInternetConnection(Stundenplan stundenplan) {
                        //Never called
                    }

                    @Override
                    public void onNoNewStundenplan() {

                    }
                });
            }

            @Override
            public void onNoNewStundenplan() {
                localDataHandler.getStundenplan(new StundenplanCallback() {
                    @Override
                    public void onStundenplanLoaded(Stundenplan stundenplan) {
                        setStundenplan(stundenplan);
                        callback.onNoInternetConnection(stundenplan);
                    }

                    @Override
                    public void onStundenplanError() {
                        callback.onStundenplanError();
                    }

                    @Override
                    public void onNoInternetConnection(Stundenplan stundenplan) {
                        //Never called
                    }

                    @Override
                    public void onNoNewStundenplan() {
                        //Never called
                    }
                });
            }
        });

    }

    @Override
    public void getVertretungsplan(final VertretungsplanCallback callback) {
        serverDataHandler.getVertretungsplan(new VertretungsplanCallback() {
            @Override
            public void onVertretungsplanLoaded(Vertretungsplan vertretungsplan) {
                setVertretungsplan(vertretungsplan);
                callback.onVertretungsplanLoaded(vertretungsplan);
            }

            @Override
            public void onVertretungsplanError() {
                callback.onVertretungsplanError();
            }

            @Override
            public void onNoInternetConnection() {
                callback.onNoInternetConnection();
            }

            @Override
            public void onNoNewVertretungsplan() {

            }
        });

    }

    @Override
    public void getKlausurplan(final KlausurplanCallback callback) {
        serverDataHandler.getKlausurplan(klausurplanDatum, new KlausurplanCallback() {
            @Override
            public void onKlausurplanLoaded(Klausurplan klausurplan) {
                setKlausurplan(klausurplan);
                callback.onKlausurplanLoaded(klausurplan);
                localDataHandler.saveKlausurplanToDisk(klausurplan);
            }

            @Override
            public void onKlausurplanError() {
                callback.onKlausurplanError();
            }

            @Override
            public void onNoInternetConnection(Klausurplan klausurplan) {
                localDataHandler.getKlausurplan(new KlausurplanCallback() {
                    @Override
                    public void onKlausurplanLoaded(Klausurplan klausurplan) {
                        setKlausurplan(klausurplan);
                        callback.onNoInternetConnection(klausurplan);
                    }

                    @Override
                    public void onKlausurplanError() {
                        callback.onKlausurplanError();
                    }

                    @Override
                    public void onNoInternetConnection(Klausurplan klausurplan) {
                       //Never called
                    }

                    @Override
                    public void onNoNewKlausurplan() {
                        //Never called
                    }
                });
            }

            @Override
            public void onNoNewKlausurplan() {
                localDataHandler.getKlausurplan(new KlausurplanCallback() {
                    @Override
                    public void onKlausurplanLoaded(Klausurplan klausurplan) {
                        setKlausurplan(klausurplan);
                        callback.onKlausurplanLoaded(klausurplan);
                    }

                    @Override
                    public void onKlausurplanError() {
                        callback.onKlausurplanError();
                    }

                    @Override
                    public void onNoInternetConnection(Klausurplan klausurplan) {
                       //Never called
                    }

                    @Override
                    public void onNoNewKlausurplan() {
                        //Never called
                    }
                });
            }
        });
    }

    @Override
    public void loadFarben() {
        farben = localDataHandler.loadFarben();
    }

    @Override
    public Farben getFarben() {
        return farben;
    }

    @Override
    public Stundenplan getStundenplan() {
        return stundenplan;
    }

    public void setStundenplan(Stundenplan stundenplan) {
        this.stundenplan = stundenplan;
    }


    public void setKlausurplan(Klausurplan klausurplan) {
        this.klausurplan = klausurplan;
    }


    public void setVertretungsplan(Vertretungsplan vertretungsplan) {
        this.vertretungsplan = vertretungsplan;
    }

    @Override
    public void setFarben(Stundenplan stundenplan) {
        for (int i = 0; i < stundenplan.getWochentage().size(); i++) {
            for (int j = 0; j < stundenplan.getWochentage().get(i).getStunden().size(); j++) {
                final Stunde stunde = stundenplan.getWochentage().get(i).getStunden().get(j);
                farben.getFarbeFach(stunde.getFach());
            }
        }
        localDataHandler.saveFarbenToDisk(farben);
    }

    @Override
    public void updateFarbenOnDiskAndCache(Farben farben) {
        this.farben = farben;
        localDataHandler.saveFarbenToDisk(farben);
    }
}
