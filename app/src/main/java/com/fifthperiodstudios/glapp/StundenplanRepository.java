package com.fifthperiodstudios.glapp;

interface StundenplanRepository {
     void getStundenplanFromInternet(String mobilKey);
     void getStundenplanFromStorage();
     StundenplanParser.Stundenplan getStundenplan();
}
