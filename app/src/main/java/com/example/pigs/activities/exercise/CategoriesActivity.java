package com.example.pigs.activities.exercise;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pigs.R;


public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButtonArms;
    private ImageButton imageButtonBack;
    private ImageButton imageButtonChest;
    private ImageButton imageButtonCore;
    private ImageButton imageButtonLegs;
    private ImageButton imageButtonShoulders;

    private Button buttonArms;
    private Button buttonBack;
    private Button buttonChest;
    private Button buttonCore;
    private Button buttonLegs;
    private Button buttonShoulders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        imageButtonArms = (ImageButton) findViewById(R.id.imageButtonArms);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBack);
        imageButtonChest = (ImageButton) findViewById(R.id.imageButtonChest);
        imageButtonCore = (ImageButton) findViewById(R.id.imageButtonCore);
        imageButtonLegs = (ImageButton) findViewById(R.id.imageButtonLegs);
        imageButtonShoulders = (ImageButton) findViewById(R.id.imageButtonLegs);

        buttonArms = (Button) findViewById(R.id.buttonArms);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonChest = (Button) findViewById(R.id.buttonChest);
        buttonCore = (Button) findViewById(R.id.buttonCore);
        buttonLegs = (Button) findViewById(R.id.buttonLegs);
        buttonShoulders = (Button) findViewById(R.id.buttonShoulders);

        imageButtonArms.setOnClickListener(this);
        imageButtonBack.setOnClickListener(this);
        imageButtonChest.setOnClickListener(this);
        imageButtonCore.setOnClickListener(this);
        imageButtonLegs.setOnClickListener(this);
        imageButtonShoulders.setOnClickListener(this);

        buttonArms.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonChest.setOnClickListener(this);
        buttonCore.setOnClickListener(this);
        buttonLegs.setOnClickListener(this);
        buttonShoulders.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        setContentView(R.layout.activity_category);
        TextView textView = (TextView)findViewById(R.id.textViewTitle);
        switch (v.getId()) {
            case R.id.buttonArms:
            case R.id.imageButtonArms:
                textView.setText("Arms");
            case R.id.buttonBack:
            case R.id.imageButtonBack:
                textView.setText("Back");
            case R.id.buttonChest:
            case R.id.imageButtonChest:
                textView.setText("Chest");
            case R.id.buttonCore:
            case R.id.imageButtonCore:
                textView.setText("Core");
            case R.id.buttonLegs:
            case R.id.imageButtonLegs:
                textView.setText("Legs");
            case R.id.buttonShoulders:
            case R.id.imageButtonShoulders:
                textView.setText("Shoulders");

        }
    }
}
