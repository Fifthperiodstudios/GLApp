package com.fifthperiodstudios.glapp.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.EinLogger;
import com.fifthperiodstudios.glapp.Downloader.EinLoggerListener;
import com.fifthperiodstudios.glapp.GLAPPActivity;
import com.fifthperiodstudios.glapp.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SchuelerLoginFragment extends Fragment implements EinLoggerListener {
    public SchuelerLoginFragment() {

    }
    SharedPreferences sharedPreferences;
    private Button loginButton;
    private EditText usernameTextView;
    private EditText passwordTextView;
    private String mobilKey;
    private EinLogger einLogger;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.student_login_layout, container, false);
        Bundle args = getArguments();

        loginButton = (Button) rootView.findViewById(R.id.login_button);
        usernameTextView = (EditText) rootView.findViewById(R.id.username_student);
        passwordTextView = (EditText) rootView.findViewById(R.id.password_student);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);
        einLogger = new EinLogger(getActivity(),this);

        usernameTextView.setSingleLine(true);
        usernameTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        passwordTextView.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String username = usernameTextView.getText().toString();
                            String password = passwordTextView.getText().toString();
                            loginButton.setClickable(false);
                            loginButton.setAlpha(.5f);
                            progressBar.setVisibility(View.VISIBLE);
                            einLogger.logIn(username, password, "https://mobil.gymnasium-lohmar.org/XML/anmelden.php?username=" + username + "&passwort=" + password);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                loginButton.setClickable(false);
                loginButton.setAlpha(.5f);
                progressBar.setVisibility(View.VISIBLE);
                einLogger.logIn(username, password, "https://mobil.gymnasium-lohmar.org/XML/anmelden.php?username=" + username + "&passwort=" + password);
            }
        });

        return rootView;
    }

    @Override
    public void falscheDaten() {
        Toast.makeText(getContext(), "Benutzername oder Passwort falsch", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);
    }

    @Override
    public void keineInternetverbindung() {
        Toast.makeText(getContext(), "Kein Internet", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);    }

    @Override
    public void eingeloggt(String mobilKey) {
        this.mobilKey = mobilKey;
        sharedPreferences.edit().putString("mobilKey", mobilKey).commit();
        Intent intent = new Intent(getActivity(), GLAPPActivity.class);
        intent.putExtra("mobilKey", mobilKey);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void unvollstaendig() {
        Toast.makeText(getContext(), "Logindaten unvollständig", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);    }

    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), "Etwas ist schiefgelaufen :/", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);    }
}
