/*
 *  Wilbert:
 *      # Created this class in order to abstract away all database functionality into an easy to
 *          use MyDatabase object
 *      # This class contains the code for opening and closing a database
 *      # This class contains the code for performing select queries
 *          (either for all items or items of a certain category)
 *      # This class has methods that allow easy control of a database query cursor
 *      # This class has methods for CRUD operations
 */

package com.sargent.mark.todolist.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class MyDatabase {

    private Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private boolean mCursorPositionIsValid;

    public MyDatabase(Context context){
        mContext = context;
        mDBHelper = new DBHelper(context);
        mDatabase = null;
        mCursor = null;
        mCursorPositionIsValid = false;
    }

    ////////////////////////////////////////////////
    //
    //  Database Open and Close Methods
    //
    ////////////////////////////////////////////////

    public boolean open(){
        if(mDatabase != null){
            close();
        }

        mCursorPositionIsValid = false;

        try {
            mDatabase = mDBHelper.getWritableDatabase();
        }
        catch(SQLiteException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean close(){
        if(mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }

        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }

        mCursorPositionIsValid = false;
        return true;
    }



    ////////////////////////////////////////////////
    //
    //  Database Query Methods
    //
    ////////////////////////////////////////////////

    public void selectAll(){
        mCursor = mDatabase.query(
            Contract.TABLE_TODO.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE
        );

        mCursorPositionIsValid = false;
    }

    public void selectAllOfCategory(String category){
        mCursor = mDatabase.query(
                Contract.TABLE_TODO.TABLE_NAME,
                null,
                Contract.TABLE_TODO.COLUMN_NAME_CATEGORY + " = '" + category + "'",
                null,
                null,
                null,
                Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE
        );

        mCursorPositionIsValid = false;
    }

    ////////////////////////////////////////////////
    //
    //  Cursor Control Methods
    //
    ////////////////////////////////////////////////

    public int getNumCursorRows(){
        if(mCursor == null){
            return 0;
        }

        return mCursor.getCount();
    }

    public boolean moveCursorToRow(int row){
        if(row < 0 || row >= mCursor.getCount()){
            return false;
        }

        mCursor.moveToPosition(row);
        mCursorPositionIsValid = true;
        return true;
    }


    ////////////////////////////////////////////////
    //
    //  Get Data from Cursor Row
    //
    ////////////////////////////////////////////////

    public String getDescription(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION));
    }

    public String getDate(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE));
    }

    public boolean getCompleted(){
        return mCursor.getInt(mCursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_COMPLETED)) == 0 ? false : true;
    }

    public String getCategory(){
        return mCursor.getString(mCursor.getColumnIndex(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY));
    }

    public int getId(){
        return mCursor.getInt(mCursor.getColumnIndex(Contract.TABLE_TODO._ID));
    }


    ////////////////////////////////////////////////
    //
    //  Change Data
    //
    ////////////////////////////////////////////////

    public boolean addItem(ToDoItem item){

        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION, item.getDescription());
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE, item.getDueDate());
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_COMPLETED, item.isCompleted() ? 1 : 0);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY, item.getCategory());

        return mDatabase.insert(Contract.TABLE_TODO.TABLE_NAME, null, cv) != -1;
    }

    public boolean updateItem(int id, ToDoItem updatedItem){

        ContentValues cv = new ContentValues();
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DESCRIPTION, updatedItem.getDescription());
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_DUE_DATE, updatedItem.getDueDate());
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_COMPLETED, updatedItem.isCompleted() ? 1 : 0);
        cv.put(Contract.TABLE_TODO.COLUMN_NAME_CATEGORY, updatedItem.getCategory());

        return mDatabase.update(Contract.TABLE_TODO.TABLE_NAME, cv, Contract.TABLE_TODO._ID + "=" + id, null) > 0;
    }

    public boolean deleteItem(int id){
        return mDatabase.delete(Contract.TABLE_TODO.TABLE_NAME, Contract.TABLE_TODO._ID + "=" + id, null) > 0;
    }
}
