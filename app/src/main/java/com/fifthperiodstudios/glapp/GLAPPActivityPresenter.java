package com.fifthperiodstudios.glapp;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class GLAPPActivityPresenter implements RepositoryInterface{

    private GLAPPActivityView view;
    private StundenplanRepository repository;
    private String mobilKey;

    public GLAPPActivityPresenter(GLAPPActivityView view, StundenplanRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void setMobilKey(String mobilKey){
        this.mobilKey = mobilKey;
    }

    public void loadStundenplan(boolean isOnline) {
        if(isOnline) {
            repository.getStundenplanFromInternet(mobilKey);
        }else{
            repository.getStundenplanFromStorage();
        }

    }

    @Override
    public void fetchingOnlineData() {

    }

    @Override
    public void fetchingStorageData() {

    }

    @Override
    public void fetchingOnlineDataComplete() {
        if(repository.getStundenplan() == null || repository.getStundenplan().getWochentage().isEmpty()){
            view.displayKeinenStundenplan();
        }else{
            view.displayFreshStundenplan(repository.getStundenplan());
        }

    }

    @Override
    public void fetchingStorageDataComplete() {
        if(repository.getStundenplan() == null || repository.getStundenplan().getWochentage().isEmpty()){
            view.displayKeinenStundenplan();
        }else{
            view.displayOldStundenplan(repository.getStundenplan());
        }
    }
}
