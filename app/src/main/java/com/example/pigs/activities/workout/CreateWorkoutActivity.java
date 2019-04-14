package com.example.pigs.activities.workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.pigs.R;
import com.example.pigs.activities.authentication.LoginActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.progress.ProgressActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.WorkoutController;
import com.example.pigs.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CreateWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "CreateWorkoutActivity";

    private DrawerLayout drawerLayout;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private Spinner dropdown;
    private ListView listView;
    private EditText workoutName;
    private EditText workoutCategory;
    private List<String> workoutExercises;
    private List<String> exerciseList;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate", "onCreate called in CreateWorkoutActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        workoutName  = findViewById(R.id.workoutName);
        workoutCategory = findViewById(R.id.workoutCategory);

        workoutExercises = new ArrayList<String>();
        exerciseList = new ArrayList<String>();
        dropdown = (Spinner)findViewById(R.id.dropdown);
        listView = (ListView)findViewById(R.id.exerciseList);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        getExercises();

        // Create an ArrayAdapter from List
        arrayAdapter = new ArrayAdapter<String>
                (getApplicationContext(), R.layout.activity_list_item_1, workoutExercises);


        // DataBind ListView with items from ArrayAdapter
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

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
                                Intent i = new Intent(CreateWorkoutActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(CreateWorkoutActivity.this, WorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(CreateWorkoutActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(CreateWorkoutActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });

        // Date Picker
        mDisplayDate = findViewById(R.id.workoutDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateWorkoutActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

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

    public void addExercise(View button){
        if(!dropdown.getSelectedItem().toString().equals("Select an exercise")) {
            String ex = dropdown.getSelectedItem().toString();
            workoutExercises.add(ex);
            arrayAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "Select an exercise", Toast.LENGTH_LONG).show();
        }
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

    // Create a new workout method
    public void createWorkoutPost(View button){
        if(!workoutName.getText().toString().equals("") && !workoutCategory.getText().toString().equals("")
        && workoutExercises.size() != 0) {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Object, Void, Boolean> createWorkoutTask = new AsyncTask<Object, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Object... params) {
                    String name = workoutName.getText().toString();
                    String category = workoutCategory.getText().toString();
                    String date = mDisplayDate.getText().toString();

                    workoutName.setText("");
                    workoutCategory.setText("");

                    SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                    int userId = Integer.parseInt(prefs.getString("userId", null));
                    return new WorkoutController().createWorkout(userId, name, category, workoutExercises, date);
                }

                @Override
                protected void onPostExecute(Boolean items) {
                    System.out.println(items);
                    workoutExercises.clear();
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Workout Created!", Toast.LENGTH_LONG).show();
                }
            };

            createWorkoutTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please fill out each field correctly", Toast.LENGTH_LONG).show();
        }
    }

    public void logout(View textView){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent i = new Intent(CreateWorkoutActivity.this, LoginActivity.class);
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
