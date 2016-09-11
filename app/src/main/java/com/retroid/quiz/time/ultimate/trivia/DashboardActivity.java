package com.retroid.quiz.time.ultimate.trivia;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.File;
import java.util.ArrayList;


public class DashboardActivity extends ActionBarActivity {

    Button btnPlayAll, btnChooseCategory, btnMainHighScore, btnMainSettings;
    ImageView imvRate, imvWFS, imvNoAds, imvWFC, imvShare;
    SharedPreferences preferences;
    public static MediaPlayer mediaPlayer;
    boolean playBackgroundMusic;
    public static ArrayList<QuizQuestions> quizDb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StartAppSDK.init(this, "102884510", "202541237", true);
        //StartAppAd.showSplash(this, savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        new MyAsyncTask().execute();

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

        TextView myTextView = (TextView) findViewById(R.id.logoTextview);
        Typeface typeFace = Typeface.createFromAsset(DashboardActivity.this.getAssets(), "diavlo.otf");
        myTextView.setTypeface(typeFace);

        btnPlayAll = (Button) findViewById(R.id.playAllButton);
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PlayingActivity.class);
                intent.putExtra(PlayingFragment.PLAYINGMODE, 0);
                startActivity(intent);
            }
        });

        btnChooseCategory = (Button) findViewById(R.id.categoryButton);
        btnChooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CategoriesActivity.class);
                startActivity(intent);
            }
        });

        btnMainHighScore = (Button) findViewById(R.id.mainScoreButton);
        btnMainHighScore.setText("High Score: " + preferences.getInt("highest_score_all", 0));

        btnMainSettings = (Button) findViewById(R.id.mainSettingsButton);
        btnMainSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, SettingActivity.class));
            }
        });


        imvRate = (ImageView) findViewById(R.id.imageViewRate);
        imvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Please give us 5 star rating!", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=" + DashboardActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName())));
                }
            }
        });

        imvWFS = (ImageView) findViewById(R.id.imageViewWFS);
        imvWFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Try our 'WFS: WhatsApp File Sender' app!", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=retrospect.aditya.whatzappfilecourierads");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=retrospect.aditya.whatzappfilecourierads")));
                }
            }
        });

        imvNoAds = (ImageView) findViewById(R.id.imageViewNoAds);
        imvNoAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Ads free version coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        imvWFC = (ImageView) findViewById(R.id.imageViewWFC);
        imvWFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Try our 'W-Clean: WhatsApp File Cleaner' app!", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=com.retrospectivecreations.wfc");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.retrospectivecreations.wfc")));
                }
            }
        });

        imvShare = (ImageView) findViewById(R.id.imageViewShare);
        imvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! Check out this super awesome quiz app called 'Quiz Time'!  " + "http://play.google.com/store/apps/details?id=" + DashboardActivity.this.getPackageName());
                try {
                    DashboardActivity.this.startActivity(Intent.createChooser(shareIntent, "Share 'Quiz Time' using..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DashboardActivity.this, "No messaging apps available.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        if (playBackgroundMusic) {
            mediaPlayer = new MediaPlayer();
            PlaySounds.playBackgroundMusic(this, mediaPlayer, "whichSound");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        playBackgroundMusic = preferences.getBoolean("playbackgroundmusic", true);
        if (playBackgroundMusic) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
            }
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd = new ProgressDialog(DashboardActivity.this);

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
                    AssetsCopy.copyFileFromAssets(DashboardActivity.this, "retrospect.db", getFilesDir().getAbsolutePath() + "/retrospect.dat");

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


}
