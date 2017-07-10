package com.example.evan.simplemathgame;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import android.content.SharedPreferences;


import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;

import android.os.Handler;

import android.widget.Toast;
/**
 * Created by evan on 2016/7/9.
 */
public class PlayGame extends Activity implements OnClickListener{
    //shared preferences
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";

    //ui elements
    private TextView disquestion, answerTxt,scoreTxt;
    private ImageView response;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0,
            enterBtn, clearBtn;

    private int recLen = 0;
    private DBhelper dbHelper;
    String[] question = new String[10];
    int[] answer = new int[10];
    int[] user = new int[10];
    int i = 0;
    int right=0,wrong=0;
    int year,month,date;
    int hour,minute,second;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initiate shared prefs
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);


        setContentView(R.layout.activity_playgame);
        answerTxt = (TextView)findViewById(R.id.answer);
        response =  (ImageView)findViewById(R.id.response);
        disquestion=(TextView)findViewById(R.id.question);
        scoreTxt =  (TextView)findViewById(R.id.score);


        Time t=new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone資料。
        t.setToNow(); // get system time
        year = t.year;
        month = t.month;
        date = t.monthDay;
        hour = t.hour; // 0-23
        minute = t.minute;
        second = t.second;

        //新页面接受数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        question = bundle.getStringArray("question");
        answer = bundle.getIntArray("answer");
        disquestion.setText(question[i] + "=");
        dbHelper = new DBhelper(this,1);
        handler.postDelayed(runnable, 1000);


        //Set the tick/cross response image to be invisible initially
        response.setVisibility(View.INVISIBLE);

        //retrieve references to the number, clear, and enter buttons
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);

        //Listen for clicks on all of these buttons
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);


        if(savedInstanceState!=null){
            //saved instance state data
            int exScore = savedInstanceState.getInt("score");
            scoreTxt.setText("Score: "+exScore);
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            //txtView.setText("" + recLen);
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.enter) {
            String reply =answerTxt.getText().toString();
            if (reply.equals("")) {
                Toast.makeText(PlayGame.this, "Please enter your answer", Toast.LENGTH_SHORT).show();
            } else {
                user[i] = Integer.parseInt(reply);
                //get score
                int exScore = getScore();
                if (user[i] == answer[i]) {
                    right++;
                    scoreTxt.setText("Score: "+(exScore+1));
                    response.setImageResource(R.drawable.tick);
                    response.setVisibility(View.VISIBLE);
                    Toast.makeText(PlayGame.this, "Correct Answer", Toast.LENGTH_SHORT).show();
                } else {
                    wrong++;
                    //set high score
                    setHighScore();
                    //incorrect
                    scoreTxt.setText("Score: 0");
                    response.setImageResource(R.drawable.cross);
                    response.setVisibility(View.VISIBLE);
                    Toast.makeText(PlayGame.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                }
                i++;
                if(i==10){
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    for(int j=0;j<10;j++){
                        ContentValues values = new ContentValues();
                        values.clear();
                        values.put("question", question[j]);
                        values.put("answer", answer[j]);
                        values.put("user", user[j]);
                        db.insert("questions", null, values);
                    }
                    ContentValues values = new ContentValues();
                    values.clear();
                    values.put("date", year+"."+month+"."+date);
                    values.put("time", hour+":"+minute+":"+second);
                    values.put("duration", recLen);
                    values.put("correctCount", right);
                    db.insert("times", null, values);
                    db.close();
                    dbHelper.close();
                    // TODO Auto-generated method stub
                    enterBtn.setEnabled(false);
                    new AlertDialog.Builder(PlayGame.this).setTitle("Congratulation")//set dialog box title

                            .setMessage("Time："+recLen+" seconds \nCorrect "+right+" questions")//set display context

                            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//設置響應事件
                                    // TODO Auto-generated method stub
                                    finish();
                                }

                            }).show();
                }
                else{
                    disquestion.setText(question[i] + "=");
                    answerTxt.setText("");
                }
            }
        }else if(view.getId()==R.id.clear) {
            answerTxt.setText("");
        } else {
            //number button
            response.setVisibility(View.INVISIBLE);
            int enteredNum = Integer.parseInt(view.getTag().toString());
            answerTxt.append(""+enteredNum);
        }
    }

    private int getScore(){
        String scoreStr = scoreTxt.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    private void setHighScore(){
        //set high score

        int exScore = getScore();

        if(exScore>0){
            //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");

            String dateOutput = dateForm.format(new Date());
            //get existing scores
            String scores = gamePrefs.getString("highScores", "");

            if(scores.length()>0){
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();

                String[] exScores = scores.split("\\|");

                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }

                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);
                //sort
                Collections.sort(scoreStrings);
                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;//only want ten
                    if(s>0) scoreBuild.append("|");//pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();
            }
            else{
                //no existing scores
                scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
                scoreEdit.commit();
            }
        }
    }

    protected void onDestroy(){
        setHighScore();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save score
        int exScore = getScore();
        savedInstanceState.putInt("score", exScore);
        //superclass method
        super.onSaveInstanceState(savedInstanceState);
    }
}
