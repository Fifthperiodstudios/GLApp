package com.fifthperiodstudios.glapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class StudentLoginFragment extends Fragment {
    public StudentLoginFragment() {

    }
    SharedPreferences sharedPreferences;
    private Button loginButton;
    private EditText usernameTextView;
    private EditText passwordTextView;
    private String mobilKey;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.student_login_layout, container, false);
        Bundle args = getArguments();

        loginButton = (Button) rootView.findViewById(R.id.login_button);
        usernameTextView = (EditText) rootView.findViewById(R.id.username_student);
        passwordTextView = (EditText) rootView.findViewById(R.id.password_student);
        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                boolean ready = checkFields(username, password);

                if(ready){
                    new DownloadKeyTask().execute("https://mobil.gymnasium-lohmar.org/XML/anmelden.php?username="+ username +"&passwort=" + password);
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Loginfelder leer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    public boolean checkFields (String u, String p){
        if(u.isEmpty() || p.isEmpty()) {
            return false;
        }
        return true;
    }

    private class DownloadKeyTask extends AsyncTask<String, Void, String> {

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
            if(result.isEmpty() || result.equals("0")) {
                Toast.makeText(getActivity().getApplicationContext(), "Benutzername oder Passwort falsch", Toast.LENGTH_SHORT).show();
            }else{
                sharedPreferences.edit().putString("mobilkey", mobilKey).commit();
                Intent intent = new Intent(getActivity(), GLAPPActivity.class);
                intent.putExtra("mobilkey", mobilKey);
                startActivity(intent);
                getActivity().finish();
            }
        }

        private String convertStreamToString(InputStream in) {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser

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
    }

}
