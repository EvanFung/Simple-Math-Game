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

public class QuestionsLog extends Activity {
    private DBhelper dbHelper;
    private TableLayout tbData;
    private TextView question;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionlog);
        tbData = (TableLayout) findViewById(R.id.tbData);
        initialDB();
    }
    public void initialDB() {
        dbHelper = new DBhelper(this,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from questions", null);
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
        fillInfo(true, "QuestionNo", "Question", "Answer", "YourAnswer");
        while (cursor.moveToNext()) {
            fillInfo(false,
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("question")),
                    cursor.getString(cursor.getColumnIndex("answer")),
                    cursor.getString(cursor.getColumnIndex("user")));
        }
    }

    public void fillInfo(boolean header, String QuestionNo, String Question, String Answer, String YourAnswer) {
        TableRow tr = new TableRow(this);
        if (header)
            tr.setBackgroundColor(0x33B5E5FF);
        TextView id = new TextView(this);
        id.setText(QuestionNo);
        id.setTextSize(20);
        id.setPadding(10, 10, 10, 10);
        TextView question = new TextView(this);
        question.setText(Question);
        question.setTextSize(20);
        question.setPadding(10, 10, 10, 10);
        TextView answer = new TextView(this);
        answer.setText(Answer);
        answer.setTextSize(20);
        answer.setPadding(10, 10, 10, 10);
        TextView user = new TextView(this);
        user.setText(YourAnswer);
        user.setTextSize(20);
        user.setPadding(10, 10, 10, 10);

        tr.addView(id);
        tr.addView(question);
        tr.addView(answer);
        tr.addView(user);
        tbData.addView(tr);
    }


}
