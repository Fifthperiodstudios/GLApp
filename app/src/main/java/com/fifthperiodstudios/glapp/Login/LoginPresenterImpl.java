package com.fifthperiodstudios.glapp.Login;

public class LoginPresenterImpl implements LoginPresenter {
    private LoginRepository loginRepository;
    private final LoginView mSchuelerLoginView;
    private final LoginView mLehrerLoginView;

    public LoginPresenterImpl(LoginRepository loginRepository, LoginView mSchuelerLoginView, LoginView mLehrerLoginView) {
        this.loginRepository = loginRepository;

        this.mSchuelerLoginView = mSchuelerLoginView;
        this.mLehrerLoginView = mLehrerLoginView;
    }

    @Override
    public void loginSchueler(String key, String u, String p) {
        loginRepository.getMobilKey(key, u, p, new LoginRepository.MobilKeyCallback() {
            @Override
            public void onConnectionError() {
                mSchuelerLoginView.falscheDaten();
            }

            @Override
            public void onWrongCredentialsError() {
                mSchuelerLoginView.falscheDaten();
            }

            @Override
            public void onUsernameOrPassowordMissing() {
                mSchuelerLoginView.unvollstaendig();
            }

            @Override
            public void onSuccess(String mobilKey) {
                mSchuelerLoginView.eingeloggt(mobilKey);
            }
        });
    }

    @Override
    public void loginLehrer(String key, String u, String p) {
        loginRepository.getMobilKey(key, u, p, new LoginRepository.MobilKeyCallback() {
            @Override
            public void onConnectionError() {
                mLehrerLoginView.falscheDaten();
            }

            @Override
            public void onWrongCredentialsError() {
                mLehrerLoginView.falscheDaten();
            }

            @Override
            public void onUsernameOrPassowordMissing() {
                mLehrerLoginView.unvollstaendig();
            }

            @Override
            public void onSuccess(String mobilKey) {
                mLehrerLoginView.eingeloggt(mobilKey);
            }
        });
    }
}
