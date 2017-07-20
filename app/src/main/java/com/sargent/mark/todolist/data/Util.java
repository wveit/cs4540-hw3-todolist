/*
 *  Wilbert:
 *      # Created this class to hold miscelaneous methods and an array that were used in various
 *          classes but didn't really fit into those classes
 */

package com.sargent.mark.todolist.data;

public class Util {

    public static String formatDate(int year, int month, int day) {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static int extractYear(String formattedDate){
        return Integer.parseInt(formattedDate.substring(0, 4));
    }

    public static int extractMonth(String formattedDate){
        return Integer.parseInt(formattedDate.substring(5, 7));
    }

    public static int extractDay(String formattedDate){
        return Integer.parseInt(formattedDate.substring(8));
    }

    public static String printDB(MyDatabase db){
        int size = db.getNumCursorRows();
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < size; i++){
            db.moveCursorToRow(i);
            builder.append(db.getDescription());
            builder.append(", ");
            builder.append(db.getCompleted());
            builder.append(" --- ");
        }
        return builder.toString();
    }

    public static final String[] CATEGORIES = {"FAMILY", "WORK", "FUN", "HEALTH"};

    public static int indexOfCategory(String category){
        for(int i = 0; i < CATEGORIES.length; i++){
            if(CATEGORIES[i].equals(category)) {
                return i;
            }
        }
        return -1;
    }
}
