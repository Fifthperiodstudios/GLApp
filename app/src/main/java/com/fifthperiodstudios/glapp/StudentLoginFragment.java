package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
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

public class StudentLoginFragment extends Fragment implements Login{
    public StudentLoginFragment() {

    }

    SharedPreferences sharedPreferences;
    private Button loginButton;
    private EditText usernameTextView;
    private EditText passwordTextView;
    private LoginPresenter loginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.student_login_layout, container, false);

        loginPresenter = new LoginPresenter(this);

        loginButton = (Button) rootView.findViewById(R.id.login_button);
        usernameTextView = (EditText) rootView.findViewById(R.id.username_student);
        passwordTextView = (EditText) rootView.findViewById(R.id.password_student);

        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                loginPresenter.login(username, password, isOnline());
            }
        });

        EditText addCourseText = (EditText) rootView.findViewById(R.id.password_student);
        addCourseText.setOnKeyListener(new View.OnKeyListener()
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
                            loginPresenter.login(username, password, isOnline());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override public void loggedInSuccessfully(String mobilKey){
        sharedPreferences.edit().putString("mobilKey", mobilKey).commit();
        Intent intent = new Intent(getActivity(), GLAPPActivity.class);
        intent.putExtra("mobilKey", mobilKey);
        startActivity(intent);
        getActivity().finish();
    }

    @Override public void loggedInWithoutCredentials(){
        Toast.makeText(getActivity().getApplicationContext(),"Loginfelder leer", Toast.LENGTH_SHORT).show();
    }

    @Override public void loggedInWithWrongCredentials(){
        Toast.makeText(getActivity().getApplicationContext(),"Benutzername oder Passwort falsch", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loggedInWithoutInternetConnection() {
        Toast.makeText(getActivity().getApplicationContext(),"Kein Internet", Toast.LENGTH_SHORT).show();
    }
}
