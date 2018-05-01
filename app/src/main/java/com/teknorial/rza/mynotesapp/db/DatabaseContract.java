package com.teknorial.rza.mynotesapp.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NOTE = "note";

    static final class NoteColumns implements BaseColumns {

        //Note Table
        static String TITLE = "title";
        //Note description
        static String DESCRIPTION = "description";
        //Note date
        static String DATE = "date";


    }


}
