package com.example.bening_2.alansdictionary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bening_2.alansdictionary.database.KamusHelper;
import com.example.bening_2.alansdictionary.model.Kamus;
import com.example.bening_2.alansdictionary.preference.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new LoadData().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadData extends AsyncTask<Void, Integer, Void> {

        final String TAG = LoadData.class.getSimpleName();

        KamusHelper kamusHelper;
        AppPreference appPreference;
        double progress;
        double maxProgress = 100;

        @Override
        protected void onPreExecute() {
            kamusHelper = new KamusHelper(MainActivity.this);
            appPreference = new AppPreference(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            /*Prefence first Run*/
            Boolean firstRun= appPreference.getFirstRun();

            if(firstRun) {
                /*LOAD ROW DATA DARI FILE TEXT KEDALAM ARRAY KAMUS*/
                ArrayList<Kamus> kamusesEng = preLoadDataEng();
                ArrayList<Kamus> kamusesInd = preLoadDataInd();

                kamusHelper.openWrite();

                progress = 30;
                publishProgress((int) progress);
                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / kamusesEng.size();

                /*MULAI QUERY INSERT TRANSACTIONAL*/
                kamusHelper.beginTransaction();

                try {
                    for(Kamus kamus: kamusesEng) {
                        kamusHelper.insertTransactionEngInd(kamus);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                    kamusHelper.setTransactionSuccess();
                    Log.d(TAG, "ENG_IN OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Do doInBackGroung: Exception");
                }
                kamusHelper.endTransaction();

                kamusHelper.beginTransaction();

                try {
                    for(Kamus kamus: kamusesInd) {
                        kamusHelper.insertTransactionIndEng(kamus);
                        progress += progressDiff;
                        publishProgress((int) progress);
                    }
                    kamusHelper.setTransactionSuccess();
                    Log.d(TAG, "IN_ENG OK");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Do doInBackGroung: Exception");
                }

                kamusHelper.endTransaction();

                kamusHelper.close();

                /*SET APP FIRST RUN FALSE, AGAR TIDAK LOAD DATA LAGI KETIKA APP DIJALANKAN*/
                appPreference.setFirstRun(false);

                publishProgress((int) maxProgress);
            } else {
                try{
                    synchronized (this) {
                        this.wait(2000);
                        publishProgress(50);

                        this.wait(2000);
                        publishProgress((int) maxProgress);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        /*UPDATE PROSES*/
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(i);
//            Toast.makeText(MainActivity.this, "LOAD DATA OK", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public ArrayList<Kamus> preLoadDataEng(){
        ArrayList<Kamus> kamusesEng = new ArrayList<>();
        String line = null;
        BufferedReader bufferedReader;
        try{
            Resources res = getResources();
            InputStream barisKamusEng = res.openRawResource(R.raw.english_indonesia);

            bufferedReader = new BufferedReader(new InputStreamReader(barisKamusEng));
            int count = 0;
            do {
                line = bufferedReader.readLine();
                String[] splitString = line.split("\t");
                Log.d(splitString[0]+"-----", splitString[1]);

                Kamus kamus;
                kamus = new Kamus(splitString[0], splitString[1]);
                kamusesEng.add(kamus);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusesEng;
    }

    public ArrayList<Kamus> preLoadDataInd(){
        ArrayList<Kamus> kamusesInd = new ArrayList<>();
        String line = null;
        BufferedReader bufferedReader;
        try{
            Resources res = getResources();
            InputStream barisKamusInd = res.openRawResource(R.raw.indonesia_english);

            bufferedReader = new BufferedReader(new InputStreamReader(barisKamusInd));
            int count = 0;
            do {
                line = bufferedReader.readLine();
                String[] splitString = line.split("\t");

                Kamus kamus;
                kamus = new Kamus(splitString[0], splitString[1]);
                kamusesInd.add(kamus);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusesInd;
    }
}
