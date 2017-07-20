/*
 *  Wilbert:
 *      # Changed query in the onCreate() method to include two new columns (completed and category).
 *      # Updated the onUpgrade() method so that it will actually destroy and recreate table if
 *          version upgrade occurs.
 */

package com.sargent.mark.todolist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "items.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " + Contract.TABLE_TODO.TABLE_NAME + " ("+
                Contract.TABLE_TODO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE + " DATE, " +
                Contract.TABLE_TODO.COLUMN_NAME_COMPLETED + " INTEGER, " +
                Contract.TABLE_TODO.COLUMN_NAME_CATEGORY + " TEXT ); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + Contract.TABLE_TODO.TABLE_NAME);
        onCreate(db);
    }
}
