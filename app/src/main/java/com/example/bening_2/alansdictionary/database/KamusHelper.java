package com.example.bening_2.alansdictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.bening_2.alansdictionary.model.Kamus;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.TABLE_NAME_ENGLISH;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.TABLE_NAME_INDONESIA;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.tableColumns.KATA;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.tableColumns.TERJEMAHAN;

/**
 * Created by Bening_2 on 3/9/2018.
 */

public class KamusHelper {

    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public KamusHelper(Context context) {
        this.context = context;
    }

    public KamusHelper openWrite() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();

        return this;
    }

    public KamusHelper openRead() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getReadableDatabase();

        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public ArrayList<Kamus> queryEngInd(String input){
//        Cursor cursor = database.query(TABLE_NAME_ENGLISH, null, null,
//                null, null, null, _ID+ " ASC", null);

//        Cursor cursor = database.query(TABLE_NAME_ENGLISH, kolom, KATA + " =?", new String[]{input}, null, null, null
//        );
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME_ENGLISH+ " WHERE " + KATA + " LIKE '%" + input + "%'", null);

        cursor.moveToFirst();
        ArrayList<Kamus> arrayList = new ArrayList<>();
        Kamus kamus;
        Log.d("Cursor Count", ""+cursor.getCount());
        if(cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
//                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamus.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamus.setTerjemahan(cursor.getString(cursor.getColumnIndexOrThrow(TERJEMAHAN)));

                arrayList.add(kamus);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        Log.d("Size Array Query :", ""+arrayList.size());
        return arrayList;
    }

    public ArrayList<Kamus> queryIndEng(String input){
//        Cursor cursor = database.query(TABLE_NAME_INDONESIA, null, null,
//                null, null, null, _ID+ " ASC", null);

        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME_INDONESIA+ " WHERE " + KATA + " LIKE '%" + input + "%'", null);
        cursor.moveToFirst();
        ArrayList<Kamus> arrayList = new ArrayList<>();
        Kamus kamus;
        if(cursor.getCount() > 0) {
            do {
                kamus = new Kamus();
                kamus.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                kamus.setKata(cursor.getString(cursor.getColumnIndexOrThrow(KATA)));
                kamus.setTerjemahan(cursor.getString(cursor.getColumnIndexOrThrow(TERJEMAHAN)));

                arrayList.add(kamus);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    /*METODE UNTUK MEMULAI SESI TRANSACTION*/
    public void beginTransaction() {
        database.beginTransaction();
    }

    /*METODE KETIKA TRANSAKSI SUKSES*/
    public void setTransactionSuccess(){
        database.setTransactionSuccessful();
    }

    /*METODE UNTUK MENGAKHIRI SESI QUERY*/
    public void endTransaction() {
        database.endTransaction();
    }

    public void insertTransactionEngInd(Kamus kamus) {
        String sql = "INSERT INTO " + TABLE_NAME_ENGLISH + " (" + KATA + ", " + TERJEMAHAN
                +") VALUES (?,?)";
        SQLiteStatement sqLiteStatement = database.compileStatement(sql);
        sqLiteStatement.bindString(1, kamus.getKata());
        sqLiteStatement.bindString(2, kamus.getTerjemahan());
        sqLiteStatement.execute();
        sqLiteStatement.clearBindings();
    }

    public void insertTransactionIndEng(Kamus kamus) {
        String sql = "INSERT INTO " + TABLE_NAME_INDONESIA+ " (" + KATA + ", " + TERJEMAHAN
                +") VALUES (?,?)";
        SQLiteStatement sqLiteStatement = database.compileStatement(sql);
        sqLiteStatement.bindString(1, kamus.getKata());
        sqLiteStatement.bindString(2, kamus.getTerjemahan());
        sqLiteStatement.execute();
        sqLiteStatement.clearBindings();
    }


}
