package com.example.pigs.activities.exercise;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pigs.MainActivity;
import com.example.pigs.R;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ExercisesActivity extends AppCompatActivity {

    private EditText editText;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);


        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch(menuItem.getItemId()){
                            case R.id.nav_shedule:{
                                Intent i = new Intent(ExercisesActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(ExercisesActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });

        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AsyncTask task = new FetchExercisesTask();
                task.execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
                String searchQuery = editText.getText().toString();
                //todo leita úr gagnagrunninum af exercises með valueinu og setja results í listann, gæti þurft að cleara listann í hvert skipti?
            }
        });
    }

    private class FetchExercisesTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            EditText name = (EditText) findViewById(R.id.editText);
            String ex_name = name.getText().toString();
            return new ExerciseController().getExercises(ex_name);
        }

        @Override
        protected void onPostExecute(String items) {
            TextView textView = (TextView) findViewById(R.id.exercise_textView);
            Gson gson = new Gson();
            //Exercise ex = gson.fromJson(items, Exercise.class);
            if(items != null) {
                List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {
                }.getType());
                textView.setText("");

                for (Exercise element : list) {
                    textView.append(element.getName() + "\n");
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

