package com.examen.aloglife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Girondins on 2017-03-03.
 */

public class DatabaseConnect extends SQLiteOpenHelper {
    private static final String TABLE_CHAR ="char";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BIRTH = "birth";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_STEPS = "steps";
    private static final String COLUMN_LASTLOGIN = "lastlog";
    private static final String COLUMN_MIDNIGHTHOURS = "midnight";
    private static final String COLUMN_TIMEZONE = "timezone";
    private static final String COLUMN_FIRSTDAY = "firstday";
    private static final String COLUMN_TIMESPENT = "timespent";
    private static final String COLUMN_COMMUNICATION = "communication";



    private static final String DATABASE_NAME = "aloglife.db";
    private static final int DATABASE_VERSION = 28;
    private static final String DATABASE_CREATE_CHAR = "CREATE TABLE " + TABLE_CHAR + "(" +
            COLUMN_ID + " text not null primary key, " +
            COLUMN_BIRTH + " text , " +
            COLUMN_LASTLOGIN + " text , " +
            COLUMN_TIMEZONE + " text , " +
            COLUMN_MIDNIGHTHOURS + " long , " +
            COLUMN_FIRSTDAY + " integer , " +
            COLUMN_TIMESPENT + " integer , " +
            COLUMN_COMMUNICATION + " integer , " +
            COLUMN_CALORIES + " integer , " +
            COLUMN_STEPS + " integer);";





    public DatabaseConnect(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_CHAR);

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAR);
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

    public Character createCharacter(String username, String dayofbirth, long midnightHours, String timezone){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConnect.COLUMN_ID,username);
        values.put(DatabaseConnect.COLUMN_BIRTH,dayofbirth);
        values.put(DatabaseConnect.COLUMN_MIDNIGHTHOURS,midnightHours);
        values.put(DatabaseConnect.COLUMN_TIMEZONE,timezone);
        values.put(DatabaseConnect.COLUMN_FIRSTDAY,0);
        values.put(DatabaseConnect.COLUMN_TIMESPENT,0);
        db.insert(DatabaseConnect.TABLE_CHAR,"",values);
        Log.d("Creating: ", username + " Born: " + dayofbirth + " Midnight: " + midnightHours);
        return new Character(username,dayofbirth,0,0,midnightHours,dayofbirth,timezone,0,0,0);
    }

    public Character getCharacter(String username){
        int cal,steps,birth,lastlogin,midnight,timezone,firstday,timeSpent,communication;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseConnect.TABLE_CHAR + " WHERE " + DatabaseConnect.COLUMN_ID + "= ?" , new String[]{username});
        cal = cursor.getColumnIndex(DatabaseConnect.COLUMN_CALORIES);
        birth = cursor.getColumnIndex(DatabaseConnect.COLUMN_BIRTH);
        steps = cursor.getColumnIndex(DatabaseConnect.COLUMN_STEPS);
        lastlogin = cursor.getColumnIndex(DatabaseConnect.COLUMN_LASTLOGIN);
        midnight = cursor.getColumnIndex(DatabaseConnect.COLUMN_MIDNIGHTHOURS);
        timezone = cursor.getColumnIndex(DatabaseConnect.COLUMN_TIMEZONE);
        firstday = cursor.getColumnIndex(DatabaseConnect.COLUMN_FIRSTDAY);
        timeSpent = cursor.getColumnIndex(DatabaseConnect.COLUMN_TIMESPENT);
        communication = cursor.getColumnIndex(DatabaseConnect.COLUMN_COMMUNICATION);

        for(int i=0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            Character userCharacter = new Character(username,cursor.getString(birth),cursor.getInt(cal),cursor.getInt(steps),cursor.getLong(midnight),cursor.getString(lastlogin),cursor.getString(timezone),cursor.getInt(firstday),cursor.getInt(timeSpent),cursor.getInt(communication));
            Log.d(" GETTING CHAR : ", username + " Born: " + userCharacter.getDayofbirth() + " Midnight: " + userCharacter.getBirthFromMidnight() + " STEPS:  " + userCharacter.getTotalSteps() + "TIME ZONE: " + userCharacter.getBirthTimeZone() + "FIRSTDAY : " + userCharacter.getFirstday() + " TIME SPENT: " + userCharacter.getTimeSpent());
            return userCharacter;
        }
        return null;
    }

    public void setLastLogin(String lastLogin, String username){
        Log.d("Setting Last LogIn: " , lastLogin);

        SQLiteDatabase db = getWritableDatabase();

        Cursor c = db.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                        DatabaseConnect.COLUMN_LASTLOGIN + "=?" + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String[]{lastLogin,username});

        c.moveToFirst();
        c.close();
      //  db.execSQL(statement);
    }

    public void setFirstday(String username){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                                DatabaseConnect.COLUMN_FIRSTDAY + "=" + 1 + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String []{username});
        c.moveToFirst();
        c.close();
    }

    public void updateTimeSpent(String username,int yTimeSpent){
        int dbTimeSpent = 0,upTimeSpent,cursorSpent;

        SQLiteDatabase readDb = getReadableDatabase();
        SQLiteDatabase writeDb = getWritableDatabase();

        Cursor readCursor = readDb.rawQuery("SELECT * FROM " + DatabaseConnect.TABLE_CHAR + " WHERE " + DatabaseConnect.COLUMN_ID + "= ?",new String[]{username});

        cursorSpent = readCursor.getColumnIndex(DatabaseConnect.COLUMN_TIMESPENT);

        for (int i=0; i<readCursor.getCount(); i++){
            readCursor.moveToPosition(i);
            dbTimeSpent = readCursor.getInt(cursorSpent);
        }


        upTimeSpent = dbTimeSpent + yTimeSpent;

        Log.d("UPDATING TIMESPENT: " , "UPDATED: " + upTimeSpent + " BEFORE " + dbTimeSpent + " AMOUNT " + yTimeSpent);

        Cursor timeSpentCursor =  writeDb.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                DatabaseConnect.COLUMN_TIMESPENT + "=" + upTimeSpent + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String[]{username});

        timeSpentCursor.moveToFirst();
        timeSpentCursor.close();

    }

    public void uploadToDatabase(String username,int yCals, int ySteps, int yComm){
        int cal,steps,comm,upCals,upSteps,upComm
                ,dbCals=0,dbSteps=0,dbComm=0;

        SQLiteDatabase readDb = getReadableDatabase();
        SQLiteDatabase writeDb = getWritableDatabase();
        Cursor readCursor = readDb.rawQuery("SELECT * FROM " + DatabaseConnect.TABLE_CHAR + " WHERE " + DatabaseConnect.COLUMN_ID + "= ?",new String[]{username});
        cal = readCursor.getColumnIndex(DatabaseConnect.COLUMN_CALORIES);
        steps = readCursor.getColumnIndex(DatabaseConnect.COLUMN_STEPS);
        comm = readCursor.getColumnIndex(DatabaseConnect.COLUMN_COMMUNICATION);

        for(int i=0; i<readCursor.getCount(); i++){
            readCursor.moveToPosition(i);
            dbCals = readCursor.getInt(cal);
            dbSteps = readCursor.getInt(steps);
            dbComm = readCursor.getInt(comm);
        }

        Log.d("DB Steps: ", dbSteps + "" );

        upCals = dbCals + yCals;
        upSteps = dbSteps + ySteps;
        upComm = dbComm + yComm;

        Log.d("TOTAL STEPS FROM DB: ", upSteps + " Comm: " + upComm);

        Cursor stepCurs = writeDb.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                DatabaseConnect.COLUMN_STEPS + "=" + upSteps + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String[]{username});

        Cursor calCurs = writeDb.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                DatabaseConnect.COLUMN_CALORIES + "=" + upCals + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String[]{username});

        Cursor commCurs = writeDb.rawQuery("UPDATE " + DatabaseConnect.TABLE_CHAR + " SET " +
                DatabaseConnect.COLUMN_COMMUNICATION + "=" + upComm + " WHERE " + DatabaseConnect.COLUMN_ID + "=?",new String[]{username});


        stepCurs.moveToFirst();
        calCurs.moveToFirst();
        commCurs.moveToFirst();
        stepCurs.close();
        calCurs.close();
        commCurs.close();

    }



    public void eraseCharachter(String username){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DatabaseConnect.TABLE_CHAR,DatabaseConnect.COLUMN_ID + "= ?",new String[]{username});
    }


}