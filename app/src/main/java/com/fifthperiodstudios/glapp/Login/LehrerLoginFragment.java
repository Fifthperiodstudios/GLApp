package com.fifthperiodstudios.glapp.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.GLAPPActivity;
import com.fifthperiodstudios.glapp.R;

public class LehrerLoginFragment extends Fragment implements LoginView {
    public LehrerLoginFragment() {

    }

    SharedPreferences sharedPreferences;
    private Button loginButton;
    private EditText usernameTextView;
    private EditText passwordTextView;
    private LoginPresenter loginPresenter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.lehrer_login_layout, container, false);

        loginButton = (Button) rootView.findViewById(R.id.login_button);
        usernameTextView = (EditText) rootView.findViewById(R.id.username_student);
        passwordTextView = (EditText) rootView.findViewById(R.id.password_student);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);

        usernameTextView.setSingleLine(true);
        usernameTextView.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        passwordTextView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String username = usernameTextView.getText().toString();
                            String password = passwordTextView.getText().toString();
                            loginButton.setClickable(false);
                            loginButton.setAlpha(.5f);
                            progressBar.setVisibility(View.VISIBLE);
                            String key = sharedPreferences.getString("mobilKey", "DEF");
                            loginPresenter.loginLehrer(key, username, password);
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
                String key = sharedPreferences.getString("mobilKey", "DEF");
                loginPresenter.loginLehrer(key, username, password);
                hideKeyboard(getActivity());
            }
        });

        return rootView;
    }

    @Override
    public void falscheDaten() {
        Toast.makeText(getContext(), R.string.logindata_wrong, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);
    }

    @Override
    public void keineInternetverbindung() {
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);
    }

    @Override
    public void eingeloggt(String mobilKey) {
        sharedPreferences.edit().putString("mobilKey", mobilKey).commit();
        Intent intent = new Intent(getActivity(), GLAPPActivity.class);
        intent.putExtra("mobilKey", mobilKey);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void unvollstaendig() {
        Toast.makeText(getContext(), R.string.logindata_incomplete, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);
    }

    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), R.string.some_error, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        loginButton.setClickable(true);
        loginButton.setAlpha(1f);
    }

    @Override
    public void setPresenter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public static LehrerLoginFragment newInstance() {
        return new LehrerLoginFragment();
    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
