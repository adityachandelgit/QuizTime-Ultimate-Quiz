package com.retroid.quiz.time.ultimate.trivia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    public static final int DB_END = 7525;
    public static final int QUESTION_INDEX_CURSOR = 6;
    final static int questionsPerQuiz = 20;
    static final String PLAYING_MODE = "playing_mode";
    static boolean playEffects, playBackGrndMusic;
    TextView tvOption3, tvOption2, tvOption1, tvOption4, quesTV, scoreTV, questionNoTV, difficultyTV, categoryTV, halfTV, doubleChoiceTV, skipQuesTV, flyingScoreTV;
    Handler handler = new Handler();
    int currentScore, questionsAttempted, mode, startingCursorPos, startingArrayPos, previousHighScore;
    int answeredCorrectly = 0;
    int questionNumbCounter = 0;
    int flyingScoreDP = 0;
    long scoreMultiplier = 0;
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    Thread thread;
    Cursor cursor;
    SharedPreferences preferences;
    Activity parentActivity;
    MyMediaPlayer player = MyMediaPlayer.getInstance();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playing, container, false);

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(parentActivity.getFilesDir().getAbsolutePath() + "/retrospect.dat", null);
        cursor = db.query("quiz", null, null, null, null, null, null, null);

        Bundle bundle = getArguments();
        mode = bundle.getInt(PLAYING_MODE);

        preferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        previousHighScore = preferences.getInt(PREFS.HIGHEST_SCORE_ALL, 0);
        playEffects = preferences.getBoolean(PREFS.SOUND_EFFECTS, true);
        playBackGrndMusic = preferences.getBoolean(PREFS.PLAY_BG_MUSIC, true);

        if (playEffects) {
            player.sp.autoPause();
        }

        currentScore = 0;
        questionsAttempted = 0;
        flyingScoreDP = getResources().getDimensionPixelSize(R.dimen.flyingScoreDimen);

        switch (mode) {
            case CATEGORY.ALL:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.ALL, 0);
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_all", 0);
                break;
            case CATEGORY.GEO:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.GEO, DB_END); //7525
                cursor.moveToPosition(startingCursorPos);
                //startingArrayPos = preferences.getInt("array_position_geography", HostingActivity.quizDb.size() - 1);
                break;
            case CATEGORY.ENTERTAINMENT:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.ENTERTAINMENT, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.HISTORY:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.HISTORY, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.ART:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.ART, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.SCIENCE:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.SCIENCE, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.SPORTS:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.SPORTS, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.ECONOMICS:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.ECONOMICS, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.RELIGION:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.RELIGION, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.PEOPLE:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.PEOPLE, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.TECH:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.TECH, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.FOOD:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.FOOD, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.MUSIC:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.MUSIC, DB_END);
                cursor.moveToPosition(startingCursorPos);
                break;
            case CATEGORY.POLITICS:
                startingCursorPos = preferences.getInt(CURSOR_POSITION.POLITICS, DB_END);
                cursor.moveToPosition(startingCursorPos);
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
            case CATEGORY.ALL:
                editor.putInt(CURSOR_POSITION.ALL, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.ALL, startingArrayPos);
                break;
            case CATEGORY.GEO:
                editor.putInt(CURSOR_POSITION.GEO, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.GEO, startingArrayPos);
                break;
            case CATEGORY.ENTERTAINMENT:
                editor.putInt(CURSOR_POSITION.ENTERTAINMENT, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.ENTERTAINMENT, startingArrayPos);
                break;
            case CATEGORY.HISTORY:
                editor.putInt(CURSOR_POSITION.HISTORY, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.HISTORY, startingArrayPos);
                break;
            case CATEGORY.ART:
                editor.putInt(CURSOR_POSITION.ART, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.ART, startingArrayPos);
                break;
            case CATEGORY.SCIENCE:
                editor.putInt(CURSOR_POSITION.SCIENCE, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.SCIENCE, startingArrayPos);
                break;
            case CATEGORY.SPORTS:
                editor.putInt(CURSOR_POSITION.SPORTS, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.SPORTS, startingArrayPos);
                break;
            case CATEGORY.ECONOMICS:
                editor.putInt(CURSOR_POSITION.ECONOMICS, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.ECONOMICS, startingArrayPos);
                break;
            case CATEGORY.RELIGION:
                editor.putInt(CURSOR_POSITION.RELIGION, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.RELIGION, startingArrayPos);
                break;
            case CATEGORY.PEOPLE:
                editor.putInt(CURSOR_POSITION.PEOPLE, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.PEOPLE, startingArrayPos);
                break;
            case CATEGORY.TECH:
                editor.putInt(CURSOR_POSITION.TECH, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.TECH, startingArrayPos);
                break;
            case CATEGORY.FOOD:
                editor.putInt(CURSOR_POSITION.FOOD, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.FOOD, startingArrayPos);
                break;
            case CATEGORY.MUSIC:
                editor.putInt(CURSOR_POSITION.MUSIC, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.MUSIC, startingArrayPos);
                break;
            case CATEGORY.POLITICS:
                editor.putInt(CURSOR_POSITION.POLITICS, cursor.getPosition());
                editor.putInt(CURSOR_POSITION.POLITICS, startingArrayPos);
                break;
        }

        if (previousHighScore < currentScore) {
            editor.putInt(PREFS.HIGHEST_SCORE_ALL, currentScore);
        }

        editor.apply();

    }

    void nextQuestion() {

        questionNumbCounter++;
        flyingScoreTV.setText("");

        if (questionNumbCounter > questionsPerQuiz) {
            int marathonScore = preferences.getInt(PREFS.HIGHEST_SCORE_ALL, 0);
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
                                if (player.mp.isPlaying()) {
                                    player.mp.stop();
                                    player.mp.release();
                                    PlayQuickSounds.playSound(parentActivity, R.raw.bg_music);
                                }
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
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Geography");
                    break;
                case 2:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Entertainment");
                    break;
                case 3:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "History");
                    break;
                case 4:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Art and Literature");
                    break;
                case 5:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Science and Nature");
                    break;
                case 6:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Sports and Games");
                    break;
                case 7:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Economics");
                    break;
                case 8:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Religion");
                    break;
                case 9:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "People");
                    break;
                case 10:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Technology");
                    break;
                case 11:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Food");
                    break;
                case 12:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Music");
                    break;
                case 13:
                    getNextQuestion(QUESTION_INDEX_CURSOR, "Politics");
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
            PlayQuickSounds.playSound(parentActivity, R.raw.countdown);
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
                HandleAnswerTextViews(answer, flyingTranslateAnim, tvOption1,
                        new TextView[]{tvOption2, tvOption3, tvOption4});
            }
        });

        tvOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleAnswerTextViews(answer, flyingTranslateAnim, tvOption2,
                        new TextView[]{tvOption1, tvOption3, tvOption4});
            }
        });

        tvOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleAnswerTextViews(answer, flyingTranslateAnim, tvOption3,
                        new TextView[]{tvOption1, tvOption2, tvOption4});
            }
        });

        tvOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleAnswerTextViews(answer, flyingTranslateAnim, tvOption4,
                        new TextView[]{tvOption1, tvOption2, tvOption3});
            }
        });
    }

    private void HandleAnswerTextViews(final String answer,
                                       TranslateAnimation flyingTranslateAnim,
                                       TextView selectedTv,
                                       final TextView[] otherTvs) {
        selectedTv.setClickable(false);
        otherTvs[0].setClickable(false);
        otherTvs[1].setClickable(false);
        otherTvs[2].setClickable(false);
        if (selectedTv.getText().toString().equalsIgnoreCase(answer)) {
            selectedTv.setBackgroundResource(R.drawable.button_correct_green);
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
            selectedTv.setBackgroundResource(R.drawable.button_wrong_red);
            if (playEffects) {
                stopCountdownMusicAndWrongBeep();
            }
            countDownTimer.cancel();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (otherTvs[0].getText().toString().equalsIgnoreCase(answer)) {
                        otherTvs[0].setBackgroundResource(R.drawable.button_correct_green);
                    } else if (otherTvs[1].getText().toString().equalsIgnoreCase(answer)) {
                        otherTvs[1].setBackgroundResource(R.drawable.button_correct_green);
                    } else if (otherTvs[2].getText().toString().equalsIgnoreCase(answer)) {
                        otherTvs[2].setBackgroundResource(R.drawable.button_correct_green);
                    }
                    delay();
                }
            }, 1000);
        }
    }

    private void getNextQuestion(int cursorIndex, String category) {
        while (true) {
            if (!cursor.getString(cursorIndex).equalsIgnoreCase(category)) {
                if (cursor.isFirst()) {
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
        PlayQuickSounds.playSound(parentActivity, R.raw.wrong_beep);
    }

    void stopCountdownMusicAndPlayCorrectBeep() {
        PlayQuickSounds.playSound(parentActivity, R.raw.correct_beep);
    }

    static final class PREFS {
        static final String HIGHEST_SCORE_ALL = "highest_score_all";
        static final String SOUND_EFFECTS = "soundeffects";
        static final String PLAY_BG_MUSIC = "playbackgroundmusic";
    }

    static final class CATEGORY {
        static final int ALL = 0;
        static final int GEO = 1;
        static final int ENTERTAINMENT = 2;
        static final int HISTORY = 3;
        static final int ART = 4;
        static final int SCIENCE = 5;
        static final int SPORTS = 6;
        static final int ECONOMICS = 7;
        static final int RELIGION = 8;
        static final int PEOPLE = 9;
        static final int TECH = 10;
        static final int FOOD = 11;
        static final int MUSIC = 12;
        static final int POLITICS = 13;
    }

    static final class CURSOR_POSITION {
        static final String ALL = "cursor_position_all";
        static final String GEO = "cursor_position_geography";
        static final String ENTERTAINMENT = "cursor_position_entertainment";
        static final String HISTORY = "cursor_position_history";
        static final String ART = "cursor_position_art";
        static final String SCIENCE = "cursor_position_science";
        static final String SPORTS = "cursor_position_sports";
        static final String ECONOMICS = "cursor_position_economics";
        static final String RELIGION = "cursor_position_religion";
        static final String PEOPLE = "cursor_position_people";
        static final String TECH = "cursor_position_technology";
        static final String FOOD = "cursor_position_food";
        static final String MUSIC = "cursor_position_music";
        static final String POLITICS = "cursor_position_politics";
    }


}