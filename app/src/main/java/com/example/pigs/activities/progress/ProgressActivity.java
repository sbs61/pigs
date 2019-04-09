package com.example.pigs.activities.progress;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.example.pigs.R;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.activities.workout.WorkoutActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.ProgressController;
import com.example.pigs.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = "ProgressActivity";
    private ExerciseController exerciseController;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DrawerLayout drawerLayout;
    private Spinner dropdown;
    private ArrayAdapter<String> adapter;
    private List<String> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        exerciseList = new ArrayList<String>();
        dropdown = (Spinner)findViewById(R.id.dropdown2);
        getExercises();
        // Create drawer layout
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

                        // Handle navigation
                        switch(menuItem.getItemId()){
                            case R.id.nav_exercises:{
                                Intent i = new Intent(ProgressActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(ProgressActivity.this, WorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(ProgressActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });

        // Date Picker
        mDisplayDate = findViewById(R.id.progressDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ProgressActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // Handler for date picker
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = df.parse(year + "-" + month + "-" + day);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mDisplayDate.setText(df.format(date));
            }
        };
    }

    // Add progress button handler
    public void addProgress(View button){
        // Async task to add progress to database
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, Boolean> progressPostTask = new AsyncTask<Object, Void, Boolean>() {
            @Override
            @SuppressLint("WrongThread")
            protected Boolean doInBackground(Object... params) {
                // Get info from input
                String exercise = dropdown.getSelectedItem().toString();
                EditText w = (EditText) findViewById(R.id.weights);
                double weights = Double.parseDouble(w.getText().toString());
                EditText r = (EditText) findViewById(R.id.reps);
                int reps = Integer.parseInt(r.getText().toString());
                EditText s = (EditText) findViewById(R.id.sets);
                int sets = Integer.parseInt(s.getText().toString());
                String date = mDisplayDate.getText().toString();

                ExerciseController ec = new ExerciseController();
                String jsonEx = ec.getExercises(exercise);
                Gson gson = new Gson();
                Exercise[] ex = gson.fromJson(jsonEx, Exercise[].class);

                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                int userId = Integer.parseInt(prefs.getString("userId", null));
                return new ProgressController().createProgress(ex[0].getId(), userId, reps, sets, weights, date);
            }

            @Override
            protected void onPostExecute(Boolean items) {
                EditText w = (EditText) findViewById(R.id.weights);
                EditText r = (EditText) findViewById(R.id.reps);
                EditText s = (EditText) findViewById(R.id.sets);
                w.setText("");
                r.setText("");
                s.setText("");
                Toast.makeText(getApplicationContext(), "Progress Added!", Toast.LENGTH_LONG).show();
            }
        };

        progressPostTask.execute();
    }

    public void getExercises(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> getExercisesTask = new AsyncTask<Object, Void, String>() {
            @Override
            @SuppressLint("WrongThread")
            protected String doInBackground(Object... params) {
                return new ExerciseController().getAllExercises();
            }

            @Override
            protected void onPostExecute(String items) {
                Gson gson = new Gson();
                if(items != null) {
                    List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {}.getType());

                    for (Exercise element : list) {
                        // Create a List from String Array elements
                        exerciseList.add(element.getName());

                        System.out.println(exerciseList);
                    }
                    exerciseList.add("Select an exercise");

                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, exerciseList) {
                        @Override
                        public int getCount() {
                            return super.getCount()-1; // you dont display last item. It is used as hint.
                        }
                    };

                    dropdown.setAdapter(adapter);
                    dropdown.setSelection(adapter.getCount());
                }
            }
        };

        getExercisesTask.execute();
    }

    // View progress button handler, go to GraphActivity
    public void goToViewProgress(View button){
        Intent i = new Intent(ProgressActivity.this, GraphActivity.class);
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