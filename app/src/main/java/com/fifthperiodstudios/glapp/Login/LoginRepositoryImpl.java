package com.fifthperiodstudios.glapp.Login;

import android.content.SharedPreferences;

import com.fifthperiodstudios.glapp.util.AppExecutors;

public class LoginRepositoryImpl implements LoginRepository {

    private LoginDataHandler loginDataHandler;

    public LoginRepositoryImpl(){
        AppExecutors appExecutors = new AppExecutors();
        loginDataHandler = new LoginDataHandler(appExecutors);
    }

    @Override
    public void getMobilKey(String key, String u, String p, final LoginRepository.MobilKeyCallback callback) {

        if (!key.equals("DEF")) {
            callback.onSuccess(key);
        }else {
            loginDataHandler.getMobilKey(u, p, new LoginRepository.MobilKeyCallback() {
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
}
