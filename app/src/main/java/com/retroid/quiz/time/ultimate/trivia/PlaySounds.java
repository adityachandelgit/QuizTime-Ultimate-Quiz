package com.retroid.quiz.time.ultimate.trivia;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

/**
 * Created by Aditya on 11-03-2015.
 */
public class PlaySounds {

    public static void playBackgroundMusic(Context context, MediaPlayer mediaPlayer, String whichSound) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = context.getAssets().openFd(whichSound);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            if(whichSound.equalsIgnoreCase("wrongBeep.mp3") || whichSound.equalsIgnoreCase("correctbeep.mp3") || whichSound.equalsIgnoreCase("countdown.mp3")) {
                mediaPlayer.setLooping(false);
            } else {
                mediaPlayer.setLooping(true);
            }

            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
