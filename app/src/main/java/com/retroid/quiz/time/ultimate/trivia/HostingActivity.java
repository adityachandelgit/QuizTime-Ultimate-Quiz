package com.retroid.quiz.time.ultimate.trivia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.appnext.appnextsdk.Appnext;
import com.appnext.appnextsdk.NoAdsInterface;
import com.appnext.appnextsdk.OnAdLoadInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class HostingActivity extends ActionBarActivity {

    public static MediaPlayer mediaPlayer;
    SharedPreferences preferences;

    static boolean playBackgroundMusic;
    public static ArrayList<QuizQuestions> quizDb = new ArrayList<>();

    //public static Appnext appnext;
    InterstitialAd mInterstitialAd;
    //private StartAppAd startAppAd;
    static boolean stopBgForPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "102884510", "203730536", true);
        //StartAppAd.showSplash(this, savedInstanceState);
        setContentView(R.layout.activity_hosting);

        /*startAppAd = new StartAppAd(this);
        startAppAd.loadAd();
        startAppAd.loadAd(new AdEventListener() {
            @Override
            public void onReceiveAd(Ad ad) {
                startAppAd.showAd();
            }

            @Override
            public void onFailedToReceiveAd(Ad ad) {
            }
        });*/

        /*AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7768394173357020/4230171792");
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                HostingActivity.this.finish();
                System.exit(0);
            }
        });

        /*appnext = new Appnext(this);
        appnext.setAppID("f0d002db-475a-4226-a2b9-83e531e54625"); // Set your AppID
        appnext.showBubble(); // show the interstitial

        appnext.setAdLoadInterface(new OnAdLoadInterface() {
            @Override
            public void adLoaded() {
                Log.v("appnext", "on ad load");
            }
        });

        appnext.setNoAdsInterface(new NoAdsInterface() {
            @Override
            public void noAds() {
                Log.v("appnext", "no ads");
            }
        });*/

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


        Log.d("Aditya", "" + preferences.getBoolean("soundeffects", false));

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        if (playBackgroundMusic) {
            mediaPlayer = new MediaPlayer();
            if (!stopBgForPlaying) {
                PlaySounds.playBackgroundMusic(this, mediaPlayer, "backgroundMusic.mp3");
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        if (playBackgroundMusic) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
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
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = new MediaPlayer();
                                    PlaySounds.playBackgroundMusic(HostingActivity.this, mediaPlayer, "backgroundMusic.mp3");
                                }
                            } else {
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    Log.d("ADITYA", "Inside frag instanceof PlayingFragment");
                                    mediaPlayer.stop();
                                }
                            }

                            FragmentManager fm = HostingActivity.this.getSupportFragmentManager();
                            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            //appnext.showBubble();
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
            /*if (appnext.isBubbleVisible()) {
                appnext.hideBubble();
            } else {*/
            new AlertDialog.Builder(HostingActivity.this).setTitle("Exit Quiz Time?")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                HostingActivity.this.finish();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();
        }

        //    }
        else {
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
                /*String oosInternalPath = getFilesDir().getAbsolutePath() + "/retrospect.tmp";
                AssetsCopy.copyFileFromAssets(HostingActivity.this, "retrospect.dat", oosInternalPath);
                File copiedOosFile = new File(oosInternalPath);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(copiedOosFile));
                quizDb = (ArrayList<QuizQuestions>) ois.readObject();
                copiedOosFile.delete();*/

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




    /*public void playBackgroundMusic() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = this.getAssets().openFd("backgroundMusic.mp3");
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            mediaPlayer.prepare();
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
