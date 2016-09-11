package com.retroid.quiz.time.ultimate.trivia;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Aditya on 12-03-2015.
 */
public class CategoriesActivity extends ActionBarActivity {

    Button btnGeography, btnEntertainment, btnHistory, btnArt, btnScience, btnSports, btnEconomics, btnReligion;
    Button btnPeople, btnTech, btnFood, btnMusic, btnPolitics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        btnGeography = (Button) findViewById(R.id.cat_geography_button);
        btnGeography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(1);
            }
        });

        btnEntertainment = (Button) findViewById(R.id.cat_entertainment_button);
        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(2);
            }
        });

        btnHistory = (Button) findViewById(R.id.cat_history_button);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(3);
            }
        });

        btnArt = (Button) findViewById(R.id.cat_arts_button);
        btnArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(4);
            }
        });

        btnScience = (Button) findViewById(R.id.cat_science_button);
        btnScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(5);
            }
        });

        btnSports = (Button) findViewById(R.id.cat_sports_button);
        btnSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(6);
            }
        });

        btnEconomics = (Button) findViewById(R.id.cat_economics_button);
        btnEconomics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(7);
            }
        });

        btnReligion = (Button) findViewById(R.id.cat_religion_button);
        btnReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(8);
            }
        });

        btnPeople = (Button) findViewById(R.id.cat_people_button);
        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(9);
            }
        });

        btnTech = (Button) findViewById(R.id.cat_technology_button);
        btnTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(10);
            }
        });

        btnFood = (Button) findViewById(R.id.cat_food_button);
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(11);
            }
        });

        btnMusic = (Button) findViewById(R.id.cat_music_button);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(12);
            }
        });

        btnPolitics = (Button) findViewById(R.id.cat_politics_button);
        btnPolitics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeIntentAndStart(13);
            }
        });


    }

    void makeIntentAndStart(int categoryId) {
        Intent intent = new Intent(CategoriesActivity.this, PlayingActivity.class);
        intent.putExtra(PlayingFragment.PLAYINGMODE, categoryId);
        startActivity(intent);
    }


}
