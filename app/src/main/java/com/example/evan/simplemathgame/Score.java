package com.example.evan.simplemathgame;

/**
 * Created by evan on 2016/7/9.
 */
public class Score implements Comparable<Score> {
    private String scoreDate;
    public int scoreNum;

    public Score(String scoreDate,int num) {
        this.scoreDate = scoreDate;
        this.scoreNum = num;
    }

    @Override
    public int compareTo(Score sc) {
        //return 0 if equal
        //1 if passed greater than this
        //-1 if this greater than passed
        return sc.scoreNum>scoreNum? 1 : sc.scoreNum<scoreNum? -1 : 0;
    }

    public String getScoreText()
    {
        return scoreDate+" - "+scoreNum;
    }
}
