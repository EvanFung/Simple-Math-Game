package com.example.evan.simplemathgame;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GamesLog extends Activity {
    private DBhelper dbHelper;
    private TableLayout tbData;
    //TextView game;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelog);
        tbData = (TableLayout) findViewById(R.id.tbData);
        initialDB();
    }
    public void initialDB() {
        dbHelper = new DBhelper(this,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from times", null);
        try {
            fillTable(cursor);
            /* close the Database connection */
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void fillTable(Cursor cursor) throws SQLiteException {
        tbData.removeAllViews();
        fillInfo(true, "ID", "Date", "Time", "Duration", "Correct Count");
        while (cursor.moveToNext()) {
            fillInfo(false,
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("time")),
                    cursor.getString(cursor.getColumnIndex("duration")),
                    cursor.getString(cursor.getColumnIndex("correctCount")));
        }
    }

    public void fillInfo(boolean header, String ID, String Date, String Time, String Duration, String Count) {
        TableRow tr = new TableRow(this);
        if (header)
            tr.setBackgroundColor(0x33B5E5FF);
        TextView id = new TextView(this);
        id.setText(ID);
        id.setTextSize(20);
        id.setPadding(10, 10, 10, 10);
        TextView date = new TextView(this);
        date.setText(Date);
        date.setTextSize(20);
        date.setPadding(10, 10, 10, 10);
        TextView time = new TextView(this);
        time.setText(Time);
        time.setTextSize(20);
        time.setPadding(10, 10, 10, 10);
        TextView duration = new TextView(this);
        duration.setText(Duration);
        duration.setTextSize(20);
        duration.setPadding(10, 10, 10, 10);
        TextView correctCount = new TextView(this);
        correctCount.setText(Count);
        correctCount.setTextSize(20);
        correctCount.setPadding(10, 10, 10, 10);

        tr.addView(id);
        tr.addView(date);
        tr.addView(time);
        tr.addView(duration);
        tr.addView(correctCount);
        tbData.addView(tr);
    }
}
