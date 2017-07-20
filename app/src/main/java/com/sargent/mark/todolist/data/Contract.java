/*
 *  Wilbert:
 *      # Added two constants so that "completed" and "category" columns could be added to database
 */

package com.sargent.mark.todolist.data;

import android.provider.BaseColumns;



public class Contract {

    public static class TABLE_TODO implements BaseColumns{
        public static final String TABLE_NAME = "todoitems";

        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DUE_DATE = "duedate";
        public static final String COLUMN_NAME_COMPLETED = "completed";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
