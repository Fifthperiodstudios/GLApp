package com.fifthperiodstudios.glapp.Login;

import com.fifthperiodstudios.glapp.util.AppExecutors;

public class LoginRepositoryImpl implements LoginRepository {

    private LoginDataHandler loginDataHandler;

    public LoginRepositoryImpl() {
        AppExecutors appExecutors = new AppExecutors();
        loginDataHandler = new LoginDataHandler(appExecutors);
    }

    @Override
    public void getMobilKey(boolean istLehrer, String u, String p, final LoginRepository.MobilKeyCallback callback) {
        loginDataHandler.getMobilKey(istLehrer, u, p, new LoginRepository.MobilKeyCallback() {
            @Override
            public void onConnectionError() {
                callback.onConnectionError();
            }

            @Override
            public void onWrongCredentialsError() {
                callback.onWrongCredentialsError();
            }

            @Override
            public void onUsernameOrPassowordMissing() {
                callback.onUsernameOrPassowordMissing();
            }

            @Override
            public void onSuccess(String mobilKey) {
                callback.onSuccess(mobilKey);
            }
        });
    }
}
