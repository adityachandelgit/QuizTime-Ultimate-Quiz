package com.retroid.quiz.time.ultimate.trivia;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Handler;

/**
 * Created by Aditya on 11-03-2015.
 */
public class SettingsFragment extends Fragment {

    android.os.Handler handler = new android.os.Handler();
    Switch musicSwitch, effectsSwitch;
    Button btnUpdateQuestions;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean playBgMusic = preferences.getBoolean("playbackgroundmusic", false);
        boolean soundeffects = preferences.getBoolean("soundeffects", false);

        musicSwitch = (Switch) v.findViewById(R.id.backgroundMusicSwitch);
        if (playBgMusic) {
            musicSwitch.setChecked(true);
        } else {
            musicSwitch.setChecked(false);
        }
        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    if(HostingActivity.mediaPlayer != null) {
                        if (HostingActivity.mediaPlayer.isPlaying()) {
                            HostingActivity.mediaPlayer.stop();
                            //HostingActivity.mediaPlayer.release();
                        }
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("playbackgroundmusic", false);
                    editor.apply();
                } else {
                    HostingActivity.mediaPlayer = new MediaPlayer();
                    PlaySounds.playBackgroundMusic(getActivity(), HostingActivity.mediaPlayer, "backgroundMusic.mp3");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("playbackgroundmusic", true);
                    editor.apply();
                }
            }
        });


        effectsSwitch = (Switch) v.findViewById(R.id.soundEffectsSwitch);
        if (soundeffects) {
            effectsSwitch.setChecked(true);
        } else {
            effectsSwitch.setChecked(false);
        }
        effectsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("soundeffects", false);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("soundeffects", true);
                    editor.apply();
                }
            }
        });



        btnUpdateQuestions = (Button) v.findViewById(R.id.updateQuestionsButton);
        btnUpdateQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkAvailable()) {
                    new AlertDialog.Builder(getActivity()).setTitle("Internet Required!")
                            .setMessage("You need an active internet connection to update questions.")
                            .setPositiveButton("Turn on Wifi", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNeutralButton("Turn on 2g/3g", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setCancelable(false).show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(getActivity());
                    pd.setTitle("Please Wait!");
                    pd.setMessage("Downloading new questions...");
                    pd.setCancelable(false);
                    pd.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Questions Updated!", Toast.LENGTH_SHORT).show();
                        }
                    }, 4000);

                }
            }
        });


        return v;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
