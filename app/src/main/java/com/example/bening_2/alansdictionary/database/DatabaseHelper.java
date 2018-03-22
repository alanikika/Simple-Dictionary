package com.example.bening_2.alansdictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.TABLE_NAME_ENGLISH;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.TABLE_NAME_INDONESIA;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.tableColumns.KATA;
import static com.example.bening_2.alansdictionary.database.DatabaseContract.tableColumns.TERJEMAHAN;

/**
 * Created by Bening_2 on 3/9/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME = "kamus_db";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_INDONESIA_ENGLISH = "CREATE TABLE "+ TABLE_NAME_INDONESIA +
            " ("+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KATA + " TEXT NOT NULL, " +
            TERJEMAHAN + " TEXT NOT NULL);";

    public static String CREATE_TABLE_ENGLISH_INDONESIA = "CREATE TABLE "+ TABLE_NAME_ENGLISH +
            " ("+ _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KATA + " TEXT NOT NULL, " +
            TERJEMAHAN + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_INDONESIA_ENGLISH);
        db.execSQL(CREATE_TABLE_ENGLISH_INDONESIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_ENGLISH);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_INDONESIA);
        onCreate(db);
    }
}
