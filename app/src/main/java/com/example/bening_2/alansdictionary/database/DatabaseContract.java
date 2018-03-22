package com.example.bening_2.alansdictionary.database;

import android.provider.BaseColumns;

/**
 * Created by Bening_2 on 3/9/2018.
 */

public class DatabaseContract {

    static String TABLE_NAME_ENGLISH = "english_indonesia";
    static String TABLE_NAME_INDONESIA = "indonesia_english";


    static final class tableColumns implements BaseColumns {
        static String KATA = "kata";
        static String TERJEMAHAN = "terjemahan";
    }

//    static final class IndonesiaEnglishColumns implements BaseColumns {
//        static String KATA = "kata";
//        static String TERJEMAHAN = "terjemahan";
//    }
}
