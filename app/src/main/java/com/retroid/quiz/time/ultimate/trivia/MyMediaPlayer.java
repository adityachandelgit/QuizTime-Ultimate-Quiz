package com.retroid.quiz.time.ultimate.trivia;

import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * Created by Aditya on 2/10/2017.
 */

public class MyMediaPlayer {
    private static volatile MyMediaPlayer instance = null;
    MediaPlayer mp;
    SoundPool sp;
    int[] soundId = new int[4];

    private MyMediaPlayer() {
    }

    public static MyMediaPlayer getInstance() {
        if (instance == null) {
            synchronized (MyMediaPlayer.class) {
                if (instance == null) {
                    instance = new MyMediaPlayer();
                }
            }
        }
        return instance;
    }
}