package com.fifthperiodstudios.glapp;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;

public class GLAPPPresenterImpl implements GLAPPPresenter {

    private GLAPPViews.KlausurplanView klausurplanView;
    private GLAPPViews.StundenplanView stundenplanView;
    private GLAPPViews.VertretungsplanView vertretungsplanView;
    private GLAPPRepositoryImpl glappRepository;

    public GLAPPPresenterImpl (GLAPPRepositoryImpl glappRepository,
                               GLAPPViews.StundenplanView stundenplanView,
                               GLAPPViews.VertretungsplanView vertretungsplanView,
                               GLAPPViews.KlausurplanView klausurplanView){

        this.glappRepository = glappRepository;
        this.klausurplanView = klausurplanView;
        this.stundenplanView = stundenplanView;
        this.vertretungsplanView = vertretungsplanView;

        stundenplanView.setPresenter(this);
        vertretungsplanView.setPresenter(this);
        klausurplanView.setPresenter(this);
    }

    @Override
    public void downloadStundenplan() {
        glappRepository.getStundenplan(new GLAPPRepository.StundenplanCallback() {
            @Override
            public void onStundenplanLoaded(Stundenplan stundenplan) {
                glappRepository.setFarben(stundenplan);
                stundenplanView.fertigHeruntergeladen(stundenplan, glappRepository.getFarben());
            }

            @Override
            public void onStundenplanError() {
                stundenplanView.andererFehler();
            }

            @Override
            public void onNoInternetConnection(Stundenplan stundenplan) {
                stundenplanView.keineInternetverbindung(stundenplan, glappRepository.getFarben());
            }

            @Override
            public void onNoNewStundenplan() {

            }
        });
    }

    @Override
    public void downloadKlausurplan() {
        glappRepository.getKlausurplan(new GLAPPRepository.KlausurplanCallback() {

            @Override
            public void onKlausurplanLoaded(Klausurplan klausurplan) {
                klausurplanView.fertigHeruntergeladen(klausurplan, glappRepository.getFarben());
            }

            @Override
            public void onKlausurplanError() {
                klausurplanView.andererFehler();
            }

            @Override
            public void onNoInternetConnection(Klausurplan klausurplan) {
                klausurplanView.keineInternetverbindung(klausurplan, glappRepository.getFarben());
            }

            @Override
            public void onNoNewKlausurplan() {

            }
        });
    }

    @Override
    public void downloadVertretungsplan() {
        glappRepository.getVertretungsplan(new GLAPPRepository.VertretungsplanCallback() {
            @Override
            public void onVertretungsplanLoaded(Vertretungsplan vertretungsplan) {
                vertretungsplanView.fertigHeruntergeladen(vertretungsplan, glappRepository.getFarben());
            }

            @Override
            public void onVertretungsplanError() {
                vertretungsplanView.andererFehler();
            }

            @Override
            public void onNoInternetConnection() {
                vertretungsplanView.keineInternetverbindung();
            }

            @Override
            public void onNoNewVertretungsplan() {

            }
        });
    }

    @Override
    public void loadFarben() {
        glappRepository.loadFarben();
    }

    @Override
    public void updateFarben(Farben farben) {
        glappRepository.updateFarbenOnDiskAndCache(farben);
        stundenplanView.updateFarben(farben);
        klausurplanView.updateFarben(farben);
        vertretungsplanView.updateFarben(farben);
    }

    @Override
    public Farben getFarben() {
        return glappRepository.getFarben();
    }

    @Override
    public Stundenplan getStundenplan() {
        return glappRepository.getStundenplan();
    }

}
