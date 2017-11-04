package com.red.justcode.learning;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URI;

/**
 * Created by manidhar on 11/4/2017.
 */

public class TrainingProvider extends ContentProvider {

    private TrainingDB mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new TrainingDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        if(URI_MATCHER.match(uri) == LOOKUP) {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(TrainingDB.LOOKUP_TABLE.NAME);
            cursor = builder.query(db, projection, selection, selectionArgs, null,
                    null, sortOrder);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        if(URI_MATCHER.match(uri) == LOOKUP) {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.insert(TrainingDB.LOOKUP_TABLE.NAME, null, contentValues);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        //currently no plan to delete training data
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        if(URI_MATCHER.match(uri) == LOOKUP) {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            count = db.update(TrainingDB.LOOKUP_TABLE.NAME, contentValues, selection, selectionArgs);
        }
        return count;
    }

    public static final class Contract {
        public static final String AUTHORITY = "com.red.justcode.training";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    }

    private static final int LOOKUP = 1;
    private static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(Contract.AUTHORITY, "lookup", LOOKUP);
    }
}
