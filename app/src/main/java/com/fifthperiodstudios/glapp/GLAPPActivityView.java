package com.fifthperiodstudios.glapp;

interface GLAPPActivityView {
    void displayFreshStundenplan(StundenplanParser.Stundenplan stundenplan);
    void displayOldStundenplan(StundenplanParser.Stundenplan stundenplan);
    void displayKeinenStundenplan();
}
