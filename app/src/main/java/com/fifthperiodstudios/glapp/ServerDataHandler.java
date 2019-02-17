package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Klausurplan.KlausurenplanParser;
import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanParser;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanParser;
import com.fifthperiodstudios.glapp.util.AppExecutors;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class ServerDataHandler implements GLAPPRepository.Server {
    private final String STUNDENPLAN_URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";
    private final String KLAUSURPLAN_URL = "https://mobil.gymnasium-lohmar.org/XML/klausur.php?mobilKey=";
    private final String VERTRETUNGSPLAN_URL = "https://mobil.gymnasium-lohmar.org/XML/vplan.php?mobilKey=";
    private String mobilKey;

    private AppExecutors appExecutors;

    public ServerDataHandler(String mobilKey, AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.mobilKey = mobilKey;
    }

    @Override
    public void getStundenplan(final String stundenplanDatum, final GLAPPRepository.StundenplanCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String stundenplanAsString;
                    StundenplanParser stundenplanParser = new StundenplanParser();

                    if(stundenplanDatum != null && !stundenplanDatum.equals("DEF")){
                        stundenplanAsString  = loadStundenplanAsStringFromNetwork(STUNDENPLAN_URL + mobilKey + "&timestamp=" + stundenplanDatum);
                        if(stundenplanAsString != null && !stundenplanAsString.equals("0")) {
                            InputStream newstream = null;
                            try {
                                newstream = new ByteArrayInputStream(stundenplanAsString.getBytes("UTF-8"));
                            } catch (final UnsupportedEncodingException e) {
                                appExecutors.getMainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onStundenplanError();
                                        e.printStackTrace();
                                    }
                                });
                            }
                            final Stundenplan stundenplan = stundenplanParser.parseStundenplan(newstream);

                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onStundenplanLoaded(stundenplan);
                                }
                            });
                        }else if(stundenplanAsString != null && stundenplanAsString.equals("0")){
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onNoNewStundenplan();
                                }
                            });
                        }else {
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onStundenplanError();
                                }
                            });
                        }
                    }else {
                        final Stundenplan stundenplan = loadStundenplanFromNetwork(STUNDENPLAN_URL + mobilKey);
                        if(stundenplan != null) {
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onStundenplanLoaded(stundenplan);
                                }
                            });
                        }
                    }
                } catch (final IOException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            callback.onNoInternetConnection(null);
                        }
                    });
                } catch (final XmlPullParserException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            callback.onStundenplanError();
                        }
                    });
                }
            }
        };

        appExecutors.getBackgroundThread().execute(runnable);
    }

    @Override
    public void getVertretungsplan(final GLAPPRepository.VertretungsplanCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final Vertretungsplan vertretungsplan = loadVertretungsplanFromNetwork(VERTRETUNGSPLAN_URL + mobilKey);
                    if(vertretungsplan != null) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onVertretungsplanLoaded(vertretungsplan);
                            }
                        });
                    }else {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onVertretungsplanError();
                            }
                        });
                    }
                } catch (final IOException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            callback.onNoInternetConnection();
                        }
                    });
                } catch (final XmlPullParserException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            callback.onVertretungsplanError();
                        }
                    });
                }
            }
        };

        appExecutors.getBackgroundThread().execute(runnable);
    }

    @Override
    public void getKlausurplan(final String klausurplanDatum, final GLAPPRepository.KlausurplanCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final Klausurplan klausurplan = loadKlausurplanFromNetwork(KLAUSURPLAN_URL + mobilKey);
                    if(klausurplan != null) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanLoaded(klausurplan);
                            }
                        });
                    }else {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanError();
                            }
                        });
                    }
                } catch (final IOException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onNoInternetConnection(null);
                        }
                    });
                } catch (final XmlPullParserException e) {
                    appExecutors.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            callback.onKlausurplanError();
                        }
                    });
                }
            }
        };

        appExecutors.getBackgroundThread().execute(runnable);
    }


    private String loadStundenplanAsStringFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        // Instantiate the parser
        String stundenplan;

        try {
            stream = downloadUrl(urlString);
            stundenplan = convertStreamToString(stream);
            //stundenplan = parser.parseStundenplan(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return stundenplan;
    }

        private Stundenplan loadStundenplanFromNetwork(String urlString) throws IOException, XmlPullParserException {
            InputStream stream = null;
            StundenplanParser stundenplanParser = new StundenplanParser();
            Stundenplan stundenplan;

            try {
                stream = downloadUrl(urlString);
                stundenplan = stundenplanParser.parseStundenplan(stream);
                //stundenplan = parser.parseStundenplan(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return stundenplan;
        }

    private Vertretungsplan loadVertretungsplanFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        // Instantiate the parser
        Vertretungsplan vertretungsplan;
        VertretungsplanParser parser = new VertretungsplanParser();
        try {
            stream = downloadUrl(urlString);
            vertretungsplan = parser.parseVertretungsplan(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return vertretungsplan;
    }

    private Klausurplan loadKlausurplanFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        // Instantiate the parser
        Klausurplan klausurplan;
        KlausurenplanParser parser = new KlausurenplanParser();
        try {
            stream = downloadUrl(urlString);
            klausurplan = parser.parseKlausurplan(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return klausurplan;
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        InputStream in = urlConnection.getInputStream();
        return in;
    }

    private String convertStreamToString(InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
