package com.fifthperiodstudios.glapp.Login;

import com.fifthperiodstudios.glapp.util.AppExecutors;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginDataHandler implements LoginRepository.Server {

    private final AppExecutors appExecutors;
    private final String LOGIN_URL = "https://mobil.gymnasium-lohmar.org/XML/anmelden.php?username=";

    public LoginDataHandler(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
    }

    @Override
    public void getMobilKey(final boolean istLehrer, final String u, final String p, final LoginRepository.MobilKeyCallback callback) {
        if(!checkFields(u,p)){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        final String mobilKey = loadMobilKeyFromNetwork(LOGIN_URL+u+"&passwort="+p+"&lehrer="+(istLehrer ? "1" : "0"));

                        if(mobilKey != null && !mobilKey.equals("0")){
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(mobilKey);
                                }
                            });
                        }else{
                            appExecutors.getMainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onWrongCredentialsError();
                                }
                            });
                        }
                    } catch (IOException e) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onWrongCredentialsError();
                            }
                        });
                    }
                }
            };
            appExecutors.getBackgroundThread().execute(runnable);
        }else {
            callback.onUsernameOrPassowordMissing();
        }
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        InputStream in = urlConnection.getInputStream();

        return in;
    }

    public boolean checkFields (String u, String p){
        if(u.isEmpty() || p.isEmpty()) {
            return true;
        }
        return false;
    }

    private String convertStreamToString(InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String loadMobilKeyFromNetwork(String urlString) throws IOException {
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
}
