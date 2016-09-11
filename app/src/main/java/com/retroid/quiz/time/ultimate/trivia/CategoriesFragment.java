package com.retroid.quiz.time.ultimate.trivia;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Aditya on 09-03-2015.
 */
public class CategoriesFragment extends Fragment {

    Button btnGeography, btnEntertainment, btnHistory, btnArt, btnScience, btnSports, btnEconomics, btnReligion;
    Button btnPeople, btnTech, btnFood, btnMusic, btnPolitics;

    Activity parentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        btnGeography = (Button) v.findViewById(R.id.cat_geography_button);
        btnGeography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(1);
            }
        });

        btnEntertainment = (Button) v.findViewById(R.id.cat_entertainment_button);
        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(2);
            }
        });

        btnHistory = (Button) v.findViewById(R.id.cat_history_button);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(3);
            }
        });

        btnArt = (Button) v.findViewById(R.id.cat_arts_button);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(4);
            }
        });

        btnScience = (Button) v.findViewById(R.id.cat_science_button);
        btnScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(5);
            }
        });

        btnSports = (Button) v.findViewById(R.id.cat_sports_button);
        btnSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(6);
            }
        });

        btnEconomics = (Button) v.findViewById(R.id.cat_economics_button);
        btnEconomics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(7);
            }
        });

        btnReligion = (Button) v.findViewById(R.id.cat_religion_button);
        btnReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(8);
            }
        });

        btnPeople = (Button) v.findViewById(R.id.cat_people_button);
        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(9);
            }
        });

        btnTech = (Button) v.findViewById(R.id.cat_technology_button);
        btnTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(10);
            }
        });

        btnFood = (Button) v.findViewById(R.id.cat_food_button);
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(11);
            }
        });

        btnMusic = (Button) v.findViewById(R.id.cat_music_button);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(12);
            }
        });

        btnPolitics = (Button) v.findViewById(R.id.cat_politics_button);
        btnPolitics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFragSetBundle(13);
            }
        });

        return v;
    }

    void createFragSetBundle(int mode) {
        Fragment fragment = new PlayingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PlayingFragment.PLAYINGMODE, mode);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
