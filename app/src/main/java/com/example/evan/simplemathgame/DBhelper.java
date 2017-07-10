package com.example.evan.simplemathgame;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by evan on 2016/7/9.
 */
public class DBhelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;//version number
    private static final String DBNAME = "note.db";//database name
    public static final String CreateQuestions = "CREATE TABLE questions ("
            +"id integer primary key autoincrement, "
            +"question text, "+"answer int, user int)";
    public static final String CreateTimes = "CREATE TABLE times ("
            +"id integer primary key autoincrement, "
            +"date text, "+"time text, duration int,correctCount int)";

    public DBhelper(Context context, int version) {
        super(context, DBNAME, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //bulid database
        db.execSQL(CreateQuestions);
        db.execSQL(CreateTimes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS phone");
        //onCreate(db);
    }
}
