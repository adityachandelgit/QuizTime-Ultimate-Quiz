package com.retroid.quiz.time.ultimate.trivia;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Aditya on 2/10/2017.
 */

public class PlayQuickSounds {

    public static void playSound(Context context, int whichSound) {
        final MyMediaPlayer player = MyMediaPlayer.getInstance();
        player.sp.autoPause();

        if (player.sp == null) {
            player.sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            player.soundId[0] = player.sp.load(context, R.raw.correct_beep, 1);
            player.soundId[1] = player.sp.load(context, R.raw.wrong_beep, 1);
            player.soundId[2] = player.sp.load(context, R.raw.countdown, 1);
            player.soundId[3] = player.sp.load(context, R.raw.bg_music, 1);
        }

        if (whichSound == R.raw.correct_beep) {
            player.sp.play(player.soundId[0], 1, 1, 0, 0, 1);
        } else if (whichSound == R.raw.wrong_beep) {
            player.sp.play(player.soundId[1], 1, 1, 0, 0, 1);
        } else if (whichSound == R.raw.countdown) {
            player.sp.play(player.soundId[2], 1, 1, 0, 0, 1);
        } else if (whichSound == R.raw.bg_music) {
            player.sp.play(player.soundId[3], 1, 1, 0, -1, 1);
        }
    }

}
