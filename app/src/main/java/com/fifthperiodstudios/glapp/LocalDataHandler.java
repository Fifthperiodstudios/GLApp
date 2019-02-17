package com.fifthperiodstudios.glapp;

import android.os.Environment;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.util.AppExecutors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LocalDataHandler implements GLAPPRepository.Local {

    private AppExecutors appExecutors;
    private File stundenplanFile;
    private File klausurplanFile;
    private File farbenFile;
    private Stundenplan stundenplan;

    public LocalDataHandler(AppExecutors appExecutors, File stundenplanFile, File klausurplanFile, File farbenFile) {
        this.appExecutors = appExecutors;
        this.stundenplanFile = stundenplanFile;
        this.klausurplanFile = klausurplanFile;
        this.farbenFile = farbenFile;
    }

    @Override
    public void getStundenplan(final GLAPPRepository.StundenplanCallback callback) {
        if (isExternalStorageReadable()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        FileInputStream fis = new FileInputStream(stundenplanFile);
                        ObjectInputStream is = new ObjectInputStream(fis);
                        stundenplan = (Stundenplan) is.readObject();
                        is.close();
                        fis.close();

                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onStundenplanLoaded(stundenplan);
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onStundenplanError();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onStundenplanError();
                            }
                        });
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onStundenplanError();
                            }
                        });
                    }
                }
            };

            appExecutors.getBackgroundThread().execute(runnable);
        }
    }

    @Override
    public void getKlausurplan(final GLAPPRepository.KlausurplanCallback callback) {
        if (isExternalStorageReadable()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        FileInputStream fis = new FileInputStream(klausurplanFile);
                        ObjectInputStream is = new ObjectInputStream(fis);
                        final Klausurplan klausurplan = (Klausurplan) is.readObject();
                        is.close();
                        fis.close();

                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanLoaded(klausurplan);
                            }
                        });
                    } catch (FileNotFoundException e) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanError();
                            }
                        });
                    } catch (IOException e) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanError();
                            }
                        });
                    } catch (ClassNotFoundException e) {
                        appExecutors.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onKlausurplanError();
                            }
                        });
                    }
                }
            };

            appExecutors.getBackgroundThread().execute(runnable);
        }
    }

    @Override
    public void saveKlausurplanToDisk(final Klausurplan klausurplan) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isExternalStorageWritable()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(klausurplanFile);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(klausurplan);
                        os.close();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        appExecutors.getBackgroundThread().execute(runnable);
    }

    @Override
    public void saveStundenplanToDisk(final Stundenplan stundenplan) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isExternalStorageWritable()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(stundenplanFile);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(stundenplan);
                        os.close();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        appExecutors.getBackgroundThread().execute(runnable);
    }

    @Override
    public void saveFarbenToDisk(final Farben farben) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isExternalStorageWritable()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(farbenFile);
                        ObjectOutputStream os = new ObjectOutputStream(fos);
                        os.writeObject(farben);
                        os.close();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        appExecutors.getBackgroundThread().execute(runnable);
    }

    public Farben loadFarben() {
        Farben farben;
        if (isExternalStorageReadable()) {
            try {
                FileInputStream fis = new FileInputStream(farbenFile);
                ObjectInputStream is = new ObjectInputStream(fis);
                farben = (Farben) is.readObject();
                is.close();
                fis.close();
            } catch (FileNotFoundException e) {
                farben = new Farben();
            } catch (IOException e) {
                farben = new Farben();
            } catch (ClassNotFoundException e) {
                farben = new Farben();
            }
            return farben;
        }
        return new Farben();
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
