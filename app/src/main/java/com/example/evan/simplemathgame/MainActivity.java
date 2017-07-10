package com.example.evan.simplemathgame;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    private final int SUCCESS = 1;
    String[] question = new String[10];
    int[] answer = new int[10];
    Button playBtn,helpBtn,highBtn,game,ques,chart;
    String path="http://itdmoodle.hung0530.com/ptms/questions_ws.php";
    String jsonstring;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    /**
                     * 获取信息成功后，对该信息进行JSON解析，得到所需要的信息，然后在textView上展示出来。
                     */
                    JSONAnalysis(msg.obj.toString());
                    Toast.makeText(MainActivity.this, "The Game is started", Toast.LENGTH_SHORT)
                            .show();
                    break;


                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playBtn=(Button)findViewById(R.id.play_btn);
        helpBtn = (Button)findViewById(R.id.help_btn);
        highBtn = (Button) findViewById(R.id.high_btn);
        game = (Button) findViewById(R.id.gamelog_btn);
        ques = (Button) findViewById(R.id.questionlog_btn);
        chart = (Button) findViewById(R.id.chart_btn);

        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        jsonstring=HttpUtils.getJsonContent(path);
                        //System.out.println(jsonstring);
                        Message msg = new Message();
                        msg.obj = jsonstring;
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);
                    };
                }.start();

            }
        });

        highBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent highIntent = new Intent(MainActivity.this, HighScores.class);
                startActivity(highIntent);
            }
        });

        helpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpIntent = new Intent(MainActivity.this, HowToPlay.class);
                startActivity(helpIntent);
            }
        });

        ques.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuestionsLog.class);
                startActivity(i);
            }
        });
        game.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GamesLog.class);
                startActivity(i);
            }
        });

        chart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Chart.class);
                startActivity(i);
            }
        });

    }
    /**
     * JSON解析方法
     */
    protected void JSONAnalysis(String string) {
        try{
            JSONObject jsonObject = new JSONObject(string);      //return a json object


            JSONArray jsonArray = jsonObject.getJSONArray("Questions");//裡面有一個數組數據，可以用getJSONArray獲取裡面有一個數組

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i); // 得到每個OBJECT
                question[i] = item.getString("question");     // 獲得對象對應的值
                answer[i] = item.getInt("answer");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //textView.setText(weatherResult);
        Intent i = new Intent(MainActivity.this, PlayGame.class);

        //用Bundle携带數據
        Bundle bundle=new Bundle();
        bundle.putStringArray("question", question);
        bundle.putIntArray("answer", answer);
        i.putExtras(bundle);

        startActivity(i);
    }

}
