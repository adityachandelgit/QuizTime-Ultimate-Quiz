package com.retroid.quiz.time.ultimate.trivia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/*///////////// Category values ///////////////////////////////////////////////

Geo = 1, Entertainment = 2, History = 3, Art = 4, Science = 5, Sports = 6
Economics = 7, Religion = 8, People = 9; Tech =10; Food = 11, Music = 12, Politics = 13;

*///////////////////////////////////////////////////////////////////////////////


public class PlayingFragment extends Fragment {

    TextView tvOption3, tvOption2, tvOption1, tvOption4, quesTV, scoreTV, questionNoTV, difficultyTV, categoryTV, halfTV, doubleChoiceTV, skipQuesTV, flyingScoreTV;
    Handler handler = new Handler();
    int currentScore, questionsAttempted, mode, startingCursorPos, startingArrayPos, previousHighScore;
    int answeredCorrectly = 0;
    int questionNumbCounter = 0;
    int flyingScoreDP = 0;
    final static int questionsPerQuiz = 20;
    long scoreMultiplier = 0;
    static boolean playEffects, playBackGrndMusic;

    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    Thread thread;
    Cursor cursor;
    SharedPreferences preferences;

    static final String PLAYINGMODE = "playing_mode";

    Activity parentActivity;
    //Appnext appnext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playing, container, false);

        //appnext = new Appnext(parentActivity);
        //appnext.setAppID("f0d002db-475a-4226-a2b9-83e531e54625"); // Set your AppID

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(parentActivity.getFilesDir().getAbsolutePath() + "/retrospect.dat", null);
        cursor = db.query("quiz", null, null, null, null, null, null, null);

        Bundle bundle = getArguments();
        mode = bundle.getInt(PLAYINGMODE);

        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        previousHighScore = preferences.getInt("highest_score_all", 0);
        playEffects = preferences.getBoolean("soundeffects", true);
        playBackGrndMusic = preferences.getBoolean("playbackgroundmusic", true);

        if (playEffects) {
            if (HostingActivity.mediaPlayer != null) {
                if (HostingActivity.mediaPlayer.isPlaying()) {
                    HostingActivity.mediaPlayer.stop();
                    HostingActivity.stopBgForPlaying = true;
                    //HostingActivity.mediaPlayer.release();
                }
            }
        }

        currentScore = 0;
        questionsAttempted = 0;

        flyingScoreDP = getResources().getDimensionPixelSize(R.dimen.flyingScoreDimen);

        switch (mode) {
            case 0:
                startingCursorPos = preferences.getInt("cursor_position_all", 0);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_all", 0);
                break;
            case 1:
                startingCursorPos = preferences.getInt("cursor_position_geography", 7525); //7525
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_geography", HostingActivity.quizDb.size() - 1);
                break;
            case 2:
                startingCursorPos = preferences.getInt("cursor_position_entertainment", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_entertainment", HostingActivity.quizDb.size() - 1);
                break;
            case 3:
                startingCursorPos = preferences.getInt("cursor_position_history", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_history", HostingActivity.quizDb.size() - 1);
                break;
            case 4:
                startingCursorPos = preferences.getInt("cursor_position_art", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_art", HostingActivity.quizDb.size() - 1);
                break;
            case 5:
                startingCursorPos = preferences.getInt("cursor_position_science", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_science", HostingActivity.quizDb.size() - 1);
                break;
            case 6:
                startingCursorPos = preferences.getInt("cursor_position_sports", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_sports", HostingActivity.quizDb.size() - 1);
                break;
            case 7:
                startingCursorPos = preferences.getInt("cursor_position_economics", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_economics", HostingActivity.quizDb.size() - 1);
                break;
            case 8:
                startingCursorPos = preferences.getInt("cursor_position_religion", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_religion", HostingActivity.quizDb.size() - 1);
                break;
            case 9:
                startingCursorPos = preferences.getInt("cursor_position_people", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_people", HostingActivity.quizDb.size() - 1);
                break;
            case 10:
                startingCursorPos = preferences.getInt("cursor_position_technology", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_technology", HostingActivity.quizDb.size() - 1);
                break;
            case 11:
                startingCursorPos = preferences.getInt("cursor_position_food", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_food", HostingActivity.quizDb.size() - 1);
                break;
            case 12:
                startingCursorPos = preferences.getInt("cursor_position_music", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_music", HostingActivity.quizDb.size() - 1);
                break;
            case 13:
                startingCursorPos = preferences.getInt("cursor_position_politics", 7525);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_politics", HostingActivity.quizDb.size() - 1);
                break;
        }

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar2);
        progressBar.setIndeterminate(false);
        progressBar.setMax(667);

        tvOption1 = (TextView) v.findViewById(R.id.option1TV);
        tvOption2 = (TextView) v.findViewById(R.id.option2TV);
        tvOption3 = (TextView) v.findViewById(R.id.option3TV);
        tvOption4 = (TextView) v.findViewById(R.id.option4TV);
        quesTV = (TextView) v.findViewById(R.id.questionTV);

        flyingScoreTV = (TextView) v.findViewById(R.id.flyingScoreTV);

        scoreTV = (TextView) v.findViewById(R.id.scoreTV);
        questionNoTV = (TextView) v.findViewById(R.id.questionNoTV);
        difficultyTV = (TextView) v.findViewById(R.id.difficultyTV);
        categoryTV = (TextView) v.findViewById(R.id.category_textView);

        nextQuestion();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        SharedPreferences.Editor editor = preferences.edit();

        switch (mode) {
            case 0:
                editor.putInt("cursor_position_all", cursor.getPosition());
                editor.putInt("array_position_all", startingArrayPos);
                break;
            case 1:
                editor.putInt("cursor_position_geography", cursor.getPosition());
                editor.putInt("array_position_geography", startingArrayPos);
                break;
            case 2:
                editor.putInt("cursor_position_entertainment", cursor.getPosition());
                editor.putInt("array_position_entertainment", startingArrayPos);
                break;
            case 3:
                editor.putInt("cursor_position_history", cursor.getPosition());
                editor.putInt("array_position_history", startingArrayPos);
                break;
            case 4:
                editor.putInt("cursor_position_art", cursor.getPosition());
                editor.putInt("array_position_art", startingArrayPos);
                break;
            case 5:
                editor.putInt("cursor_position_science", cursor.getPosition());
                editor.putInt("array_position_science", startingArrayPos);
                break;
            case 6:
                editor.putInt("cursor_position_sports", cursor.getPosition());
                editor.putInt("array_position_sports", startingArrayPos);
                break;
            case 7:
                editor.putInt("cursor_position_economics", cursor.getPosition());
                editor.putInt("array_position_economics", startingArrayPos);
                break;
            case 8:
                editor.putInt("cursor_position_religion", cursor.getPosition());
                editor.putInt("array_position_religion", startingArrayPos);
                break;
            case 9:
                editor.putInt("cursor_position_people", cursor.getPosition());
                editor.putInt("array_position_people", startingArrayPos);
                break;
            case 10:
                editor.putInt("cursor_position_technology", cursor.getPosition());
                editor.putInt("array_position_technology", startingArrayPos);
                break;
            case 11:
                editor.putInt("cursor_position_food", cursor.getPosition());
                editor.putInt("array_position_food", startingArrayPos);
                break;
            case 12:
                editor.putInt("cursor_position_music", cursor.getPosition());
                editor.putInt("array_position_music", startingArrayPos);
                break;
            case 13:
                editor.putInt("cursor_position_politics", cursor.getPosition());
                editor.putInt("array_position_politics", startingArrayPos);
                break;
        }

        if (previousHighScore < currentScore) {
            editor.putInt("highest_score_all", currentScore);
        }

        editor.apply();

    }


    void nextQuestion() {

        questionNumbCounter++;
        flyingScoreTV.setText("");

        if (questionNumbCounter > questionsPerQuiz) {
            int marathonScore = preferences.getInt("highest_score_all", 0);
            String message;
            if (currentScore > marathonScore) {
                message = "Woo! You just beat the highest score!\n\nPrevious high score was: " + previousHighScore + "\n\nAnswered correctly: " + answeredCorrectly + " / " + questionsPerQuiz;
            } else {
                message = "Highest score is still: " + marathonScore + "\n\nCorrect Answers: " + answeredCorrectly + " / " + questionsPerQuiz + "\n\nKeep on trying!";
            }
            new AlertDialog.Builder(parentActivity).setTitle("Your Score: " + currentScore)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HostingActivity.stopBgForPlaying = false;
                            if (playEffects && playBackGrndMusic) {
                                HostingActivity.mediaPlayer = new MediaPlayer();
                                PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "backgroundMusic.mp3");
                            }
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                                fm.popBackStack();
                            }
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new DashboardFragment())
                                    .commit();
                        }
                    }).setCancelable(false).show();
            return;
        }

        if (mode == 0) {
            if (!cursor.isAfterLast()) {
                cursor.moveToNext();
            } else {
                cursor.moveToFirst();
            }
            /*if(startingArrayPos == HostingActivity.quizDb.size() - 1) {
                startingArrayPos = 0;
            } else {
                startingArrayPos++;
            }*/
        } else {
            if (!cursor.isBeforeFirst()) {
                cursor.moveToPrevious();
            } else {
                cursor.moveToLast();
            }
            /*if(startingArrayPos == 0) {
                startingArrayPos = HostingActivity.quizDb.size() - 1;
            } else {
                startingArrayPos--;
            }*/

            switch (mode) {
                case 1:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Geography")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Geography")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 2:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Entertainment")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Entertainment")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 3:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("History")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("History")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 4:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Art and Literature")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Art and Literature")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 5:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Science and Nature")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Science and Nature")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 6:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Sports and Games")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Sports and Games")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 7:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Economics")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Economics")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 8:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Religion")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Religion")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 9:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("People")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("People")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 10:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Technology")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Technology")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 11:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Food")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Food")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 12:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Music")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Music")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
                case 13:
                    while (true) {
                        if (!cursor.getString(6).equalsIgnoreCase("Politics")) {
                            if(cursor.isFirst()) {
                                cursor.moveToLast();
                            } else {
                                cursor.moveToPrevious();
                            }
                        } else {
                            break;
                        }
                    }
                /*while (true) {
                    if (!HostingActivity.quizDb.get(startingArrayPos).getCategory().equalsIgnoreCase("Politics")) {
                        startingArrayPos--;
                    } else {
                        break;
                    }
                }*/
                    break;
            }


        }


        questionsAttempted++;

        if (thread != null) {
            thread.interrupt();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        scoreMultiplier = 0;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                parentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDownTimer = new CountDownTimer(20000, 30) {
                            public void onTick(long millisUntilFinished) {
                                progressBar.setProgress((int) millisUntilFinished / 30);
                                scoreMultiplier = millisUntilFinished / 60;
                            }

                            public void onFinish() {

                            }
                        }.start();
                    }
                });
            }

        });
        thread.start();

        if (playEffects) {
            if (HostingActivity.mediaPlayer != null) {
                if (HostingActivity.mediaPlayer.isPlaying()) {
                    HostingActivity.mediaPlayer.stop();
                    HostingActivity.mediaPlayer.release();
                    HostingActivity.mediaPlayer = new MediaPlayer();
                    PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "countdown.mp3");
                } else {
                    HostingActivity.mediaPlayer = new MediaPlayer();
                    PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "countdown.mp3");
                }
            } else {
                HostingActivity.mediaPlayer = new MediaPlayer();
                PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "countdown.mp3");
            }

        }


        tvOption1.setVisibility(View.VISIBLE);
        tvOption2.setVisibility(View.VISIBLE);
        tvOption3.setVisibility(View.VISIBLE);
        tvOption4.setVisibility(View.VISIBLE);

        tvOption1.setClickable(true);
        tvOption2.setClickable(true);
        tvOption3.setClickable(true);
        tvOption4.setClickable(true);

        tvOption1.setBackgroundResource(R.drawable.clickable_button);
        tvOption2.setBackgroundResource(R.drawable.clickable_button);
        tvOption3.setBackgroundResource(R.drawable.clickable_button);
        tvOption4.setBackgroundResource(R.drawable.clickable_button);

        questionNoTV.setText("Question: " + questionsAttempted);
        difficultyTV.setText("Difficulty: " + String.valueOf(cursor.getInt(7)));
        //difficultyTV.setText("Difficulty: " + HostingActivity.quizDb.get(startingArrayPos).getDifficulty());

        categoryTV.setText("Category: " + cursor.getString(6));
        //categoryTV.setText("Category: " + HostingActivity.quizDb.get(startingArrayPos).getCategory());

        quesTV.setText(cursor.getString(1));
        //quesTV.setText(HostingActivity.quizDb.get(startingArrayPos).getQuestion());

        final ArrayList<String> options = new ArrayList<>();
        options.add(cursor.getString(2));
        options.add(cursor.getString(3));
        options.add(cursor.getString(4));
        options.add(cursor.getString(5));

        /*options.add(HostingActivity.quizDb.get(startingArrayPos).getCorrectAnswer());
        options.add(HostingActivity.quizDb.get(startingArrayPos).getWrongAnswer1());
        options.add(HostingActivity.quizDb.get(startingArrayPos).getWrongAnswer2());
        options.add(HostingActivity.quizDb.get(startingArrayPos).getWrongAnswer3());*/

        Collections.shuffle(options);
        tvOption1.setText(options.get(0));
        tvOption2.setText(options.get(1));
        tvOption3.setText(options.get(2));
        tvOption4.setText(options.get(3));

        //final String answer = HostingActivity.quizDb.get(startingArrayPos).getCorrectAnswer();
        final String answer = cursor.getString(2);

        final TranslateAnimation flyingTranslateAnim = new TranslateAnimation(0, 0, Animation.RELATIVE_TO_SELF, Animation.RELATIVE_TO_SELF - flyingScoreDP);
        final TranslateAnimation translateAnimation1 = new TranslateAnimation(1000, 0, 0, 0);
        final TranslateAnimation translateAnimation2 = new TranslateAnimation(1500, 0, 0, 0);
        final TranslateAnimation translateAnimation3 = new TranslateAnimation(2000, 0, 0, 0);
        final TranslateAnimation translateAnimation4 = new TranslateAnimation(2500, 0, 0, 0);
        final Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        translateAnimation1.setDuration(1250);
        translateAnimation2.setDuration(1250);
        translateAnimation3.setDuration(1250);
        translateAnimation4.setDuration(1250);
        flyingTranslateAnim.setDuration(2000);
        alphaAnimation.setDuration(500);

        tvOption1.startAnimation(translateAnimation1);
        tvOption2.startAnimation(translateAnimation2);
        tvOption3.startAnimation(translateAnimation3);
        tvOption4.startAnimation(translateAnimation4);

        tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOption1.setClickable(false);
                tvOption2.setClickable(false);
                tvOption3.setClickable(false);
                tvOption4.setClickable(false);
                if (tvOption1.getText().toString().equalsIgnoreCase(answer)) {
                    tvOption1.setBackgroundResource(R.drawable.button_correct_green);
                    if (playEffects) {
                        stopCountdownMusicAndPlayCorrectBeep();
                    }
                    flyingScoreTV.setVisibility(View.VISIBLE);
                    flyingScoreTV.setText("+" + scoreMultiplier);
                    flyingScoreTV.startAnimation(flyingTranslateAnim);
                    answeredCorrectly++;
                    currentScore += scoreMultiplier;
                    scoreTV.setText("Score: " + currentScore);
                    countDownTimer.cancel();
                    delay();
                } else {
                    tvOption1.setBackgroundResource(R.drawable.button_wrong_red);
                    if (playEffects) {
                        stopCountdownMusicAndWrongBeep();
                    }
                    countDownTimer.cancel();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tvOption2.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption2.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption3.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption3.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption4.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption4.setBackgroundResource(R.drawable.button_correct_green);
                            }
                            delay();
                        }
                    }, 1000);
                }
            }
        });

        tvOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOption1.setClickable(false);
                tvOption2.setClickable(false);
                tvOption3.setClickable(false);
                tvOption4.setClickable(false);
                if (tvOption2.getText().toString().equalsIgnoreCase(answer)) {
                    tvOption2.setBackgroundResource(R.drawable.button_correct_green);
                    if (playEffects) {
                        stopCountdownMusicAndPlayCorrectBeep();
                    }
                    flyingScoreTV.setVisibility(View.VISIBLE);
                    flyingScoreTV.setText("+" + scoreMultiplier);
                    flyingScoreTV.startAnimation(flyingTranslateAnim);
                    answeredCorrectly++;
                    currentScore += scoreMultiplier;
                    scoreTV.setText("Score: " + currentScore);
                    countDownTimer.cancel();
                    delay();
                } else {
                    tvOption2.setBackgroundResource(R.drawable.button_wrong_red);
                    if (playEffects) {
                        stopCountdownMusicAndWrongBeep();
                    }
                    countDownTimer.cancel();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tvOption1.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption1.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption3.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption3.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption4.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption4.setBackgroundResource(R.drawable.button_correct_green);
                            }
                            delay();
                        }
                    }, 1000);
                }
            }
        });

        tvOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOption1.setClickable(false);
                tvOption2.setClickable(false);
                tvOption3.setClickable(false);
                tvOption4.setClickable(false);
                if (tvOption3.getText().toString().equalsIgnoreCase(answer)) {
                    tvOption3.setBackgroundResource(R.drawable.button_correct_green);
                    if (playEffects) {
                        stopCountdownMusicAndPlayCorrectBeep();
                    }
                    flyingScoreTV.setVisibility(View.VISIBLE);
                    flyingScoreTV.setText("+" + scoreMultiplier);
                    flyingScoreTV.startAnimation(flyingTranslateAnim);
                    answeredCorrectly++;
                    currentScore += scoreMultiplier;
                    scoreTV.setText("Score: " + currentScore);
                    countDownTimer.cancel();
                    delay();
                } else {
                    tvOption3.setBackgroundResource(R.drawable.button_wrong_red);
                    if (playEffects) {
                        stopCountdownMusicAndWrongBeep();
                    }
                    countDownTimer.cancel();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tvOption1.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption1.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption2.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption2.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption4.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption4.setBackgroundResource(R.drawable.button_correct_green);
                            }
                            delay();
                        }
                    }, 1000);
                }
            }
        });

        tvOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOption1.setClickable(false);
                tvOption2.setClickable(false);
                tvOption3.setClickable(false);
                tvOption4.setClickable(false);
                if (tvOption4.getText().toString().equalsIgnoreCase(answer)) {
                    tvOption4.setBackgroundResource(R.drawable.button_correct_green);
                    if (playEffects) {
                        stopCountdownMusicAndPlayCorrectBeep();
                    }
                    flyingScoreTV.setVisibility(View.VISIBLE);
                    flyingScoreTV.setText("+" + scoreMultiplier);
                    flyingScoreTV.startAnimation(flyingTranslateAnim);
                    answeredCorrectly++;
                    currentScore += scoreMultiplier;
                    scoreTV.setText("Score: " + currentScore);
                    countDownTimer.cancel();
                    delay();
                } else {
                    if (playEffects) {
                        stopCountdownMusicAndWrongBeep();
                    }
                    tvOption4.setBackgroundResource(R.drawable.button_wrong_red);
                    countDownTimer.cancel();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (tvOption1.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption1.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption2.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption2.setBackgroundResource(R.drawable.button_correct_green);
                            } else if (tvOption3.getText().toString().equalsIgnoreCase(answer)) {
                                tvOption3.setBackgroundResource(R.drawable.button_correct_green);
                            }
                            delay();
                        }
                    }, 1000);
                }
            }
        });


    }

    void delay() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextQuestion();
            }
        }, 1750);
    }

    void stopCountdownMusicAndWrongBeep() {
        if (HostingActivity.mediaPlayer != null) {
            HostingActivity.mediaPlayer.stop();
            HostingActivity.mediaPlayer.release();
            HostingActivity.mediaPlayer = null;
            HostingActivity.mediaPlayer = new MediaPlayer();
            PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "wrongBeep.mp3");
        }
    }

    void stopCountdownMusicAndPlayCorrectBeep() {
        if (HostingActivity.mediaPlayer != null) {
            HostingActivity.mediaPlayer.stop();
            HostingActivity.mediaPlayer.release();
            HostingActivity.mediaPlayer = null;
            HostingActivity.mediaPlayer = new MediaPlayer();
            PlaySounds.playBackgroundMusic(parentActivity, HostingActivity.mediaPlayer, "correctbeep.mp3");
        }
    }


}