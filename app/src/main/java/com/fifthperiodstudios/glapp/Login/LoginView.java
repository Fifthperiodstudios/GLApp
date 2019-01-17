package com.fifthperiodstudios.glapp.Login;

public interface LoginView {
    void falscheDaten();

    void keineInternetverbindung();

    void eingeloggt(String mobilKey);

    void unvollstaendig();

    void andererFehler();

    void setPresenter(LoginPresenter loginPresenter);
}
