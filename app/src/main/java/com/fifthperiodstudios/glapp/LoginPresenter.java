package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginPresenter {
    private Login login;

    public LoginPresenter(Login login) {
        this.login = login;
    }

    private boolean checkLoginFields(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            return false;
        }
        return true;
    }

    public void login(String username, String password, boolean isOnline) {
        boolean check = checkLoginFields(username, password);
        if(check){
            if(isOnline){
                new DownloadMobilKey().execute("https://mobil.gymnasium-lohmar.org/XML/anmelden.php?username="+ username +"&passwort=" + password);
            }else {
                login.loggedInWithoutInternetConnection();
            }
        }else {
            login.loggedInWithoutCredentials();
        }
    }

    public class DownloadMobilKey  extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "";
            } catch (XmlPullParserException e) {
                return "";
            }
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.isEmpty() || result.equals("0")) {
                login.loggedInWithWrongCredentials();
            } else {
                login.loggedInSuccessfully(result);
            }
        }

        private String convertStreamToString(InputStream in) {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            String mobilKey;
            try {
                stream = downloadUrl(urlString);
                mobilKey = convertStreamToString(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return mobilKey;
        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            InputStream in = urlConnection.getInputStream();

            return in;
        }
    }

}
