package com.red.justcode.learning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by manidhar on 11/4/2017.
 */

public class TrainingDB extends SQLiteOpenHelper{

    public static class LOOKUP_TABLE {
        public static final String NAME = "lookup";
        public static final Uri CONTENT_URI = Uri.parse(TrainingProvider.Contract.CONTENT_URI + "/" + NAME);
        public static final String COLUMN_O_STATE = "o_state";
        public static final String COLUMN_x_STATE = "x_state";
        public static final String COLUMN_PROBABILITY = "probability";
    }

    public TrainingDB(Context context) {
        super(context,"learningDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TrainingDB", "MANI: onCreate");
        String CREATE_LOOKUP_TABLE = "CREATE TABLE "+LOOKUP_TABLE.NAME+" (id INTEGER PRIMARY KEY, "
                +LOOKUP_TABLE.COLUMN_O_STATE+" REAL, "+ LOOKUP_TABLE.COLUMN_x_STATE
                +" REAL, "+LOOKUP_TABLE.COLUMN_PROBABILITY+" REAL)";
        sqLiteDatabase.execSQL(CREATE_LOOKUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //nothing for first version
    }
}
