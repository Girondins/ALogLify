package com.examen.aloglife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Girondins on 2017-03-03.
 */

public class DatabaseConnect extends SQLiteOpenHelper {
    public static final String TABLE_CHAR ="char";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_BIRTH = "birth";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_STEPS = "steps";



    private static final String DATABASE_NAME = "aloglife.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE_PERSON = "CREATE TABLE " + TABLE_CHAR + "(" +
            COLUMN_ID + " text not null primary key, " +
            COLUMN_BIRTH + " text , " +
            COLUMN_CALORIES + " integer , " +
            COLUMN_STEPS + " integer);";





    public DatabaseConnect(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_PERSON);

    }

    /**
     * Metod som kallas ifall databasen redan existerar
     * @param db databas
     * @param oldVersion äldreversion
     * @param newVersion nya versionen
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseConnect.class.getName(),"Updating database from version" + oldVersion + "to"
                + newVersion + ", which will replace all old data");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CHAR);
        onCreate(db);

    }

    public boolean checkExistChar(String check){
        int id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseConnect.TABLE_CHAR , null);
        id = cursor.getColumnIndex(DatabaseConnect.COLUMN_ID);

        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            if(cursor.getString(id).equals(check)){
                return true;
            }
        }
        return false;
    }

/**
    public void addWorker(String name,double wage, int monthhour){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_NAME,name);
        values.put(UserDBHelper.COLUMN_WAGE,wage);
        values.put(UserDBHelper.COLUMN_CALCMONTH,monthhour);
        db.insert(UserDBHelper.TABLE_PERSON,"",values);
        Log.d("Adding Worker", name);
    }

    public void removeWorker(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PERSON,UserDBHelper.COLUMN_NAME + "= ?",new String[]{name});
    }

    public void editWorker(String name,double wage,int calcmonth){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        if(wage != -1){
            cv.put(UserDBHelper.COLUMN_WAGE,wage);
        }
        if(calcmonth != -1) {
            cv.put(UserDBHelper.COLUMN_CALCMONTH, calcmonth);
        }

        db.update(UserDBHelper.TABLE_PERSON,cv,UserDBHelper.COLUMN_NAME + " = ?",new String []{name});


    }

    public void addNewHour(String name, int hours, String date){
        SQLiteDatabase db = getWritableDatabase();
        String statement = "INSERT INTO " + UserDBHelper.TABLE_HOURS + " VALUES ('" +
                date + "'," + hours + "," +
                "(SELECT " + UserDBHelper.COLUMN_NAME + " from " + UserDBHelper.TABLE_PERSON
                + " WHERE " + UserDBHelper.COLUMN_NAME + " = '" + name + "'));";

        db.execSQL(statement);


    }

    public void removeHour(String name, String date){
        SQLiteDatabase db = getWritableDatabase();
        String statement = "DELETE FROM " + UserDBHelper.TABLE_HOURS + " WHERE "
                + UserDBHelper.COLUMN_WORKER + "='" + name + "' AND " +
                UserDBHelper.COLUMN_DATE + "='" + date + "';";

        db.execSQL(statement);


    }

    public void editHour(String name, String date, int hour){
        SQLiteDatabase db = getWritableDatabase();

        if(date != null) {
            String statement = "UPDATE " + UserDBHelper.TABLE_HOURS + " SET " +
                    UserDBHelper.COLUMN_DATE + "='" + date + "' WHERE " +
                    UserDBHelper.COLUMN_WORKER + "=' " + name + "';";
            db.execSQL(statement);
        }
        if(hour != -1){
            String statement = "UPDATE " + UserDBHelper.TABLE_HOURS + " SET " +
                    UserDBHelper.COLUMN_WORK + " ='" + hour + "' WHERE " +
                    UserDBHelper.COLUMN_WORKER + "=' " + name + "';";
            db.execSQL(statement);
        }

    }




    public LinkedList<Person> getUsers(){
        int wage,name,calcmonth;

        inputs = new LinkedList<Person>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDBHelper.TABLE_PERSON , null);

        wage = cursor.getColumnIndex(UserDBHelper.COLUMN_WAGE);
        name = cursor.getColumnIndex(UserDBHelper.COLUMN_NAME);
        calcmonth = cursor.getColumnIndex(UserDBHelper.COLUMN_CALCMONTH);

        Log.d("Rows", cursor.getCount() + "");

        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Person current = new Person(cursor.getString(name),cursor.getDouble(wage),cursor.getInt(calcmonth));
            inputs.add(getWorkersHours(current));
        }




        Log.d("Returning Users", inputs.size() + "");
        return inputs;

    }

    public Person getWorkersHours(Person person){
        int work,date;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDBHelper.TABLE_HOURS +
                " WHERE " + UserDBHelper.COLUMN_WORKER + "= ?",new String[]{person.getName()});


        work = cursor.getColumnIndex(UserDBHelper.COLUMN_WORK);
        date = cursor.getColumnIndex(UserDBHelper.COLUMN_DATE);


        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Log.d("HOURSDATE", cursor.getString(date) + " is " + person.getName() + " minutes " + cursor.getInt(work));
            person.addHours(new Hours(cursor.getInt(work),cursor.getString(date)));
        }

        return person;
    }


**/

}