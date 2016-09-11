package com.retroid.quiz.time.ultimate.trivia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new MyAsyncTask().execute();

    }


    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd = new ProgressDialog(SplashActivity.this);

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String oosInternalPath = getFilesDir().getAbsolutePath() + "/retrospect.tmp";
                AssetsCopy.copyFileFromAssets(SplashActivity.this, "retrospect.dat", oosInternalPath);
                File copiedOosFile = new File(oosInternalPath);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(copiedOosFile));
                HostingActivity.quizDb = (ArrayList<QuizQuestions>) ois.readObject();
                copiedOosFile.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                startActivity(new Intent(SplashActivity.this, HostingActivity.class));
        }
    }

}
