package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class StundenplanRepo implements StundenplanRepository {

    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";
    private Context context;
    private StundenplanParser.Stundenplan stundenplan;
    private RepositoryInterface presenter;

    public StundenplanRepo(Context context) {
        this.context = context;
        stundenplan = new StundenplanParser.Stundenplan();
    }

    public void setPresenter(RepositoryInterface presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getStundenplanFromInternet(String mobilKey) {
        new DownloadXmlTask().execute(URL + mobilKey);
    }

    @Override
    public void getStundenplanFromStorage() {
        try {
            presenter.fetchingStorageData();
            File directory = context.getFilesDir();
            File file = new File(directory, "Stundenplan.xml");
            StundenplanParser stundenplanParser = new StundenplanParser();
            stundenplan = (StundenplanParser.Stundenplan) stundenplanParser.parseStundenplan(new FileInputStream(file));
            setupStundenplan(stundenplan);
            presenter.fetchingStorageDataComplete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StundenplanParser.Stundenplan getStundenplan() {
        return stundenplan;
    }

    private void setupStundenplan(StundenplanParser.Stundenplan stundenplan) {
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("#1abc9c");
        colors.add("#3498db");
        colors.add("#2ecc71");
        colors.add("#9b59b6");
        colors.add("#34495e");
        colors.add("#16a085");
        colors.add("#f1c40f");
        colors.add("#e74c3c");
        colors.add("#95a5a6");
        colors.add("#B33771");
        colors.add("#c8d6e5");
        colors.add("#c8d6e5");
        colors.add("#c8d6e5");
        colors.add("#A64B48");
        colors.add("#A0A68B");//s
        colors.add("#454442");//s
        for (Fach f : stundenplan.getFächer()) {
            int i = (int) (Math.random() * colors.size());
            f.setColor(colors.get(i));
            colors.remove(i);
        }
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, StundenplanParser.Stundenplan> {

        protected StundenplanParser.Stundenplan doInBackground(String... urls) {
            presenter.fetchingOnlineData();
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return new StundenplanParser.Stundenplan();
            } catch (XmlPullParserException e) {
                return new StundenplanParser.Stundenplan();
            }

        }


        protected void onPostExecute(StundenplanParser.Stundenplan result) {
            super.onPostExecute(result);
            setupStundenplan(result);
            presenter.fetchingOnlineDataComplete();
        }

        private StundenplanParser.Stundenplan loadXmlFromNetwork(String urlString) throws
                XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser stundenplanParser = new StundenplanParser();
            try {
                stream = downloadUrl(urlString);

                FileOutputStream outputStream = new FileOutputStream(new File(context.getFilesDir(), "Stundenplan.xml"));
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();

                File directory = context.getFilesDir();
                File file = new File(directory, "Stundenplan.xml");

                try {
                    stundenplan = (StundenplanParser.Stundenplan) stundenplanParser.parseStundenplan(new FileInputStream(file));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return stundenplan;
        }

        // Given a string representation of a URL, sets up a connection and gets
// an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            InputStream in = urlConnection.getInputStream();
            return in;
        }
    }

}
