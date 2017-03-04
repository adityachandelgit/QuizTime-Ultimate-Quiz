package com.retroid.quiz.time.ultimate.trivia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.appnext.ads.interstitial.Interstitial;
import com.appnext.core.callbacks.OnAdClosed;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class HostingActivity extends ActionBarActivity {

    public static ArrayList<QuizQuestions> quizDb = new ArrayList<>();
    static boolean playBackgroundMusic;
    static boolean stopBgForPlaying = false;
    SharedPreferences preferences;
    Interstitial interstitial_Ad;
    MyMediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosting);

        player = MyMediaPlayer.getInstance();
        if (player.sp == null) {
            player.sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            player.soundId[3] = player.sp.load(this, R.raw.bg_music, 1);
            player.soundId[0] = player.sp.load(this, R.raw.correct_beep, 1);
            player.soundId[1] = player.sp.load(this, R.raw.wrong_beep, 1);
            player.soundId[2] = player.sp.load(this, R.raw.countdown, 1);
        }

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        interstitial_Ad = new Interstitial(this, "12e98ef2-87cd-4894-a32d-2c64e96d5cc2");
        interstitial_Ad.loadAd();
        interstitial_Ad.setOnAdClosedCallback(new OnAdClosed() {
            @Override
            public void onAdClosed() {
                HostingActivity.this.finish();
                System.exit(0);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("firstrun", true)) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("playbackgroundmusic", true);
            editor.putBoolean("soundeffects", true);
            editor.putBoolean("firstrun", false);
            editor.apply();
        }
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        new MyAsyncTask().execute();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new DashboardFragment()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        player = MyMediaPlayer.getInstance();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (frag instanceof DashboardFragment) {
            if (playBackgroundMusic) {
                if (!stopBgForPlaying) {
                    PlayQuickSounds.playSound(this, R.raw.bg_music);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        if (playBackgroundMusic) {
            player.sp.autoPause();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (frag instanceof PlayingFragment) {
            new AlertDialog.Builder(HostingActivity.this).setTitle("End quiz?")
                    .setMessage("Are you sure you want to quit? Your scores won't be saved!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HostingActivity.stopBgForPlaying = false;
                            playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
                            if (playBackgroundMusic) {
                                if (PlayingFragment.playEffects) {
                                    player.sp.autoPause();
                                    PlayQuickSounds.playSound(HostingActivity.this, R.raw.bg_music);

                                }
                            } else {
                                player.sp.autoPause();
                            }

                            FragmentManager fm = HostingActivity.this.getSupportFragmentManager();
                            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();
        } else if (frag instanceof CategoriesFragment) {

            FragmentManager fm = HostingActivity.this.getSupportFragmentManager();
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();

        } else if (frag instanceof DashboardFragment) {
            new AlertDialog.Builder(HostingActivity.this).setTitle("Exit Quiz Time?")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            player.sp.release();
                            if (interstitial_Ad.isAdLoaded()) {
                                interstitial_Ad.showAd();
                            } else {
                                HostingActivity.this.finish();
                                System.exit(0);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();
        } else {
            super.onBackPressed();
        }
    }


    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd = new ProgressDialog(HostingActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Loading Files...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                File file = new File(getFilesDir().getAbsolutePath() + "/retrospect.dat");
                if (!file.exists()) {
                    AssetsCopy.copyFileFromAssets(HostingActivity.this, "retrospect.db", getFilesDir().getAbsolutePath() + "/retrospect.dat");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null) {
                pd.dismiss();
            }
        }
    }

}
