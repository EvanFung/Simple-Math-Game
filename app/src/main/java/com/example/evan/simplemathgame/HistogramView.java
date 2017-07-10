package com.example.evan.simplemathgame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class HistogramView extends View {
    private DBhelper dbHelper;
    ArrayList<Integer> listId = new ArrayList<Integer>();
    ArrayList<Integer> listDura = new ArrayList<Integer>();
    ArrayList<Integer> listCorrect = new ArrayList<Integer>();

    private Paint mPaint;
    private Rect mRect;

    private int mWidth;
    private int mHeight;

    private int mPaddingStart;
    private int mPaddingEnd;
    private int mPaddingTop;
    private int mPaddingBottom;

    private int mLeft;
    private int mTop;
    private int mRight;
    private int mBottom;

    private Context mContext;

    private ArrayList<Bar> mBarLists;

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initData();
    }

    public HistogramView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistogramView(Context context) {
        this(context, null);
    }

    public void iniDB() {
        dbHelper = new DBhelper(getContext(),1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // To get the cursor
        Cursor cursor = db.rawQuery("select * from times", null);
        while (cursor.moveToNext()) {

            //To get the id
            int id = cursor.getInt(0);

            String date=cursor.getString(1);

            String time=cursor.getString(2);

            int duration=cursor.getInt(3);

            int correctCount = cursor.getInt(4);
            listId.add(id);
            listCorrect.add(correctCount);
            listDura.add(duration);
        }

        cursor.close();
        db.close();
        dbHelper.close();
    }

    private void initData() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRect = new Rect();
        iniDB();

        // default data
        mBarLists = new ArrayList<Bar>();


        for(int i = 0;i<listCorrect.size();i++) {
            if (i%2==0) {
                mBarLists.add(new Bar(i, (listCorrect.get(i)/10f), Color.BLUE, "Game ID "+listId.get(i).toString(),listCorrect.get(i).toString()));
            } else {
                mBarLists.add(new Bar(i, (listCorrect.get(i)/10f), Color.CYAN, "Game ID "+listId.get(i).toString(),listCorrect.get(i).toString()));
            }

        }

    }

    public void setBarLists(ArrayList<Bar> barLists){
        mBarLists = barLists;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = getSizeFromMeasureSpec(widthMeasureSpec, 480);
        mHeight = getSizeFromMeasureSpec(heightMeasureSpec, 480);

        mPaddingStart = getPaddingStart();
        mPaddingEnd = getPaddingEnd();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();

        mLeft = mPaddingStart;
        mTop = mPaddingTop;
        mRight = mWidth - mPaddingEnd;
        mBottom = mHeight - mPaddingBottom;

        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // set background
        canvas.drawColor(Color.BLACK);
        mRect.set(mLeft, mTop, mRight, mBottom);
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(mRect, mPaint);
        //*/
        // set bottom text property
        mPaint.setTextSize(sp2Px(mContext, 11));
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        int fontHeight = (int) Math.ceil(fontMetricsInt.bottom - fontMetricsInt.top);

        int N = mBarLists.size();
        int UNIT_WIDTH = (mRight - mLeft) / (2 * N + 1);

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        // draw bar one by one
        for (int i = 0; i < N; i++) {
            Bar bar = mBarLists.get(i);

            // draw bar bottom text
            left = (int) (mLeft + (i * 2 + 0.5f) * UNIT_WIDTH);
            right = left + UNIT_WIDTH * 2;
            top = mBottom - fontHeight;
            bottom = mBottom;
            mRect.set(left, top, right, bottom);
            int baseLine = (mRect.top + mRect.bottom - fontMetricsInt.top - fontMetricsInt.bottom) / 2;
            mPaint.setColor(Color.BLACK);
            canvas.drawText(bar.bootomText, mRect.centerX(), baseLine, mPaint);

            // draw bar outline
            left = mLeft + (i * 2 + 1) * UNIT_WIDTH;
            right = left + UNIT_WIDTH;
            bottom = mBottom - fontHeight;
            top = bottom - (int) ((mBottom - mTop - fontHeight * 2) * bar.ratio);
            mRect.set(left, top, right, bottom);
            mPaint.setColor(bar.color);
            canvas.drawRect(mRect, mPaint);

            // draw bar top text
            left = (int) (mLeft + (i * 2 + 0.5f) * UNIT_WIDTH);
            right = left + UNIT_WIDTH * 2;
            bottom = top;
            top = top - fontHeight;
            mRect.set(left, top, right, bottom);
            baseLine = (mRect.top + mRect.bottom - fontMetricsInt.top - fontMetricsInt.bottom) / 2;
            mPaint.setColor(Color.BLACK);
            canvas.drawText(bar.topText, mRect.centerX(), baseLine, mPaint);
        }

        // draw line
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(mLeft, mBottom - fontHeight, mRight, mBottom - fontHeight, mPaint);
        // canvas.drawLine(mLeft, mTop + fontHeight, mRight, mTop + fontHeight, mPaint);

        super.onDraw(canvas);
    }

    public class Bar {
        public int id;
        public float ratio;
        public int color;
        public String bootomText;
        public String topText;


        public Bar(int id, float ratio, int color, String bootomText, String topText) {
            this.id = id;
            this.ratio = ratio;
            this.color = color;
            this.bootomText = bootomText;
            this.topText = topText;
        }
    }


    // tool class
    public  int getSizeFromMeasureSpec(int measureSpec, int defaultSize) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(mode == MeasureSpec.EXACTLY){
            result = size;
        } else {
            result = defaultSize;
            if(mode == MeasureSpec.AT_MOST){
                result = Math.min(defaultSize, size);
            }
        }
        return result;
    }
    public  float sp2Px(Context context, float sp){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        float px = metrics.scaledDensity;
        return sp * px;
    }
}
