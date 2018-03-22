package com.example.bening_2.alansdictionary;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bening_2.alansdictionary.adapter.KamusAdapter;
import com.example.bening_2.alansdictionary.database.DatabaseHelper;
import com.example.bening_2.alansdictionary.database.KamusHelper;
import com.example.bening_2.alansdictionary.model.Kamus;

import java.util.ArrayList;
import java.util.LinkedList;

public class DictionaryActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editQuery;
    private Button btnSearch;
    private ProgressDialog loading;
    private RecyclerView recyclerView;

    private RadioGroup kategori;
    private RadioButton radio_jenis_bahasa;



    KamusHelper kamusHelper;
    KamusAdapter kamusAdapter;

    private ArrayList<Kamus> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        editQuery = (EditText) findViewById(R.id.edit_query);
        btnSearch = (Button) findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.rv_search_result);
        kategori = (RadioGroup) findViewById(R.id.kategori);

        btnSearch.setOnClickListener(this);

        list = new ArrayList<>();

        kamusHelper = new KamusHelper(this);
        kamusAdapter = new KamusAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void radioButtonClicked() {
        int selecteddId = kategori.getCheckedRadioButtonId();
        radio_jenis_bahasa = (RadioButton) findViewById(selecteddId);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_search) {

            radioButtonClicked();
            final String formatTerjemahan = radio_jenis_bahasa.getText().toString().trim();
            Log.d("Translate Bahasa :", ""+formatTerjemahan);

            String input = editQuery.getText().toString().toLowerCase().trim();
            //Toast.makeText(DictionaryActivity.this, input, Toast.LENGTH_LONG).show();

            if(input.isEmpty() && formatTerjemahan.isEmpty()) return;

            if(formatTerjemahan.contentEquals("English - Indonesia")){
                searchWordEngInd(input);
            } else {
                searchWordIndEng(input);
            }

        }
    }

    private void showRecyclerList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        KamusAdapter kamusAdapter = new KamusAdapter(this);
        kamusAdapter.setListWordAdapter(list);
        recyclerView.setAdapter(kamusAdapter);
    }

    private void searchWordEngInd(String input){
        final String word = input;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, ArrayList<Kamus>> task = new AsyncTask<Void, Void, ArrayList<Kamus>>() {

            @Override
            protected void onPreExecute(){
                kamusHelper = new KamusHelper(DictionaryActivity.this);

                if (list.size() > 0){
                    list.clear();
                }

                Log.d("------------", "On Pre Execute");

                loading = new ProgressDialog(DictionaryActivity.this);
                loading.setTitle("Menterjemahkan");
                loading.setIndeterminate(false);
                loading.show();
            }

            @Override
            protected ArrayList<Kamus> doInBackground(Void... voids) {
                kamusHelper.openRead();
                Log.d("--------------", "Do In Background");
                return kamusHelper.queryEngInd(word);
            }

            @Override
            protected void onPostExecute(ArrayList<Kamus> kamuses) {
                super.onPreExecute();

                loading.dismiss();

                list.addAll(kamuses);
                Log.d("--------------", "On Post Execute");
                showRecyclerList();
            }
        };

        task.execute();

    }

    private void searchWordIndEng(String input) {
        final String word = input;

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, ArrayList<Kamus>> task = new AsyncTask<Void, Void, ArrayList<Kamus>>() {

            @Override
            protected void onPreExecute(){
                kamusHelper = new KamusHelper(DictionaryActivity.this);

                if (list.size() > 0){
                    list.clear();
                }

                Log.d("------------", "On Pre Execute");

                loading = new ProgressDialog(DictionaryActivity.this);
                loading.setTitle("Menterjemahkan");
                loading.setIndeterminate(false);
                loading.show();
            }

            @Override
            protected ArrayList<Kamus> doInBackground(Void... voids) {
                kamusHelper.openRead();
                Log.d("--------------", "Do In Background");
                return kamusHelper.queryIndEng(word);
            }

            @Override
            protected void onPostExecute(ArrayList<Kamus> kamuses) {
                super.onPreExecute();

                loading.dismiss();

                list.addAll(kamuses);
                Log.d("--------------", "On Post Execute");
                showRecyclerList();
            }
        };

        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (kamusHelper != null){
            kamusHelper.close();
        }
    }
}
