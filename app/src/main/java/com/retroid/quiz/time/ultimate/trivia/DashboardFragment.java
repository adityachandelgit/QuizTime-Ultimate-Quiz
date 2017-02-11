package com.retroid.quiz.time.ultimate.trivia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DashboardFragment extends Fragment {

    Button btnPlayAll, btnChooseCategory, btnMainHighScore, btnMainSettings;
    ImageView imvRate, imvWFS, imvNoAds, imvWFC, imvShare;
    SharedPreferences preferences;
    Activity parentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        TextView myTextView = (TextView) v.findViewById(R.id.logoTextview);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "diavlo.otf");
        myTextView.setTypeface(typeFace);

        btnPlayAll = (Button) v.findViewById(R.id.playAllButton);
        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PlayingFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PlayingFragment.PLAYING_MODE, 0);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        });

        btnChooseCategory = (Button) v.findViewById(R.id.categoryButton);
        btnChooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CategoriesFragment())
                        .commit();
            }
        });

        btnMainHighScore = (Button) v.findViewById(R.id.mainScoreButton);
        btnMainHighScore.setText("High Score: " + preferences.getInt("highest_score_all", 0));

        btnMainSettings = (Button) v.findViewById(R.id.mainSettingsButton);
        btnMainSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .addToBackStack(null).commit();
            }
        });


        imvRate = (ImageView) v.findViewById(R.id.imageViewRate);
        imvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parentActivity).setTitle("Please Rate!")
                        .setMessage("Would you like to give my app a 5 star rating on playstore? This will really help me a lot. Thank you!")
                        .setPositiveButton("Yes Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("market://details?id=" + parentActivity.getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + parentActivity.getPackageName())));
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });


        imvWFS = (ImageView) v.findViewById(R.id.imageViewWFS);
        imvWFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(parentActivity).setTitle("My Other Apps!")
                        .setMessage("Would you like to download and try my 'WFS: WhatsApp File Sender' app available on playstore? :)")
                        .setPositiveButton("Yes Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("market://details?id=retrospect.aditya.whatzappfilecourierads");
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=retrospect.aditya.whatzappfilecourierads")));
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        imvNoAds = (ImageView) v.findViewById(R.id.imageViewNoAds);
        imvNoAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(parentActivity, "Ads free version coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        /*imvWFC = (ImageView) v.findViewById(R.id.imageViewWFC);
        imvWFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(parentActivity).setTitle("My Other Apps!")
                        .setMessage("Would you like to download and try my 'W-Clean: WhatsApp File Cleaner' app available on playstore? :)")
                        .setPositiveButton("Yes Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri uri = Uri.parse("market://details?id=com.retrospectivecreations.wfc");
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.retrospectivecreations.wfc")));
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });*/

        imvShare = (ImageView) v.findViewById(R.id.imageViewShare);
        imvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey there! Check out this super awesome quiz app called 'Quiz Time'!  " + "http://play.google.com/store/apps/details?id=" + parentActivity.getPackageName());
                try {
                    parentActivity.startActivity(Intent.createChooser(shareIntent, "Share 'Quiz Time' with friends!"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(parentActivity, "No messaging apps available.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

    }


}








