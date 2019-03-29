package com.example.pigs.activities.exercise;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pigs.R;
import com.example.pigs.activities.authentication.LoginActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ExercisesActivity extends AppCompatActivity {

    private EditText editText;
    private DrawerLayout drawerLayout;
    private TextView textView;
    private View categoriesLayout;
    private Button backButton;
    private TextView selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Initiate variables
        editText = findViewById(R.id.editText);
        categoriesLayout = findViewById(R.id.categoriesLayout);
        textView = findViewById(R.id.exercise_textView);
        backButton = findViewById(R.id.backButton);
        selectedCategory = findViewById(R.id.selectedCategory);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Create drawer layout
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Handle navigation
                        switch(menuItem.getItemId()){
                            case R.id.nav_workouts:{
                                Intent i = new Intent(ExercisesActivity.this, CreateWorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
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

        // Handle text field inputs
        editText.addTextChangedListener(new TextWatcher() {
            // New async task to search for exercises by string
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fetch exercises by name with async task
                @SuppressLint("StaticFieldLeak")
                AsyncTask<Object, Void, String> getExercisesTask = new AsyncTask<Object, Void, String>() {

                    @Override
                    @SuppressLint("WrongThread")
                    protected String doInBackground(Object... params) {
                        String ex_name = editText.getText().toString();
                        return new ExerciseController().getExercises(ex_name);
                    }

                    @Override
                    protected void onPostExecute(String items) {
                        Gson gson = new Gson();
                        if(items != null) {
                            List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {}.getType());
                            textView.setText("");

                            for (Exercise element : list) {
                                textView.append(element.getName() + "\n");
                            }
                        }
                    }
                };

                getExercisesTask.execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String searchQuery = editText.getText().toString();
                if(searchQuery.length() == 0){
                    textView.setVisibility(TextView.GONE);
                    categoriesLayout.setVisibility(View.VISIBLE);
                }
                else{
                    textView.setVisibility(TextView.VISIBLE);
                    categoriesLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    // Handle category Selected
    public void categorySelected(View v){
        // Swap views
        editText.setVisibility(EditText.GONE);
        categoriesLayout.setVisibility(View.GONE);
        backButton.setVisibility(Button.VISIBLE);
        selectedCategory.setVisibility(TextView.VISIBLE);

        int id = v.getId();
        // Check which category was selected and display exercises accordingly
        if(id == R.id.backIMG || id == R.id.backBTN) selectedCategory.setText("Back");
        else if(id == R.id.armsIMG || id == R.id.armsBTN) selectedCategory.setText("Arms");
        else if(id == R.id.chestIMG || id == R.id.chestBTN) selectedCategory.setText("Chest");
        else if(id == R.id.legsIMG || id == R.id.legsBTN) selectedCategory.setText("Legs");
        else if(id == R.id.coreIMG || id == R.id.coreBTN) selectedCategory.setText("Core");
        else if(id == R.id.shouldersIMG || id == R.id.shouldersBTN) selectedCategory.setText("Shoulders");

        // Fetch exercises by category with  async task
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> task = new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                String ex_category = selectedCategory.getText().toString();
                return new ExerciseController().getExercisesByCategory(ex_category);
            }

            @Override
            protected void onPostExecute(String items) {
                Gson gson = new Gson();
                if(items != null) {
                    List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {}.getType());
                    textView.setText("");

                    for (Exercise element : list) {
                        textView.append(element.getName() + "\n");
                    }
                    textView.setVisibility(TextView.VISIBLE);
                }
            }
        };

        task.execute();
    }

    // Go back button handler
    public void goBack(View button){
        // Swap views
        editText.setVisibility(EditText.VISIBLE);
        categoriesLayout.setVisibility(View.VISIBLE);
        selectedCategory.setVisibility(TextView.GONE);
        backButton.setVisibility(Button.GONE);
        textView.setVisibility(TextView.GONE);
    }

    // Handler for create exercise button, initiates CreateExercisesActivity
    public void goToCreateExercise(View button){
        Intent i = new Intent(ExercisesActivity.this, CreateExerciseActivity.class);
        startActivity(i);
    }

    // Go to Login page button handler
    public void goToLogin(View button){
        Intent i = new Intent(ExercisesActivity.this, LoginActivity.class);
        startActivity(i);
    }

    // Menu button handler
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

