package com.example.pigs.activities.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import com.example.pigs.R;
import com.example.pigs.activities.authentication.LoginActivity;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.activities.workout.WorkoutActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.ProgressController;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Progress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphActivity extends AppCompatActivity {
    private Context context;
    private DrawerLayout drawerLayout;
    private GraphView graph;
    private GridLabelRenderer gridLabel;
    private long selectedExerciseId = 1;
    private long maxDate = 0;
    private long minDate;
    private double maxWeights = 0;
    private double minWeights = 10000;
    private int userId;
    Set<Long> exerciseIds = new LinkedHashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        context = this;

        // Initialize the graph
        graph = (GraphView) findViewById(R.id.graph);
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setPadding(32);

        // Initialize logged in userId
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        userId = Integer.parseInt(prefs.getString("userId", null));

        // Initialize spinner
        Spinner exercise = (Spinner)findViewById(R.id.selectedExercise);

        // Get execises for the spinner that user has added progress for
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, List<Exercise>> getProgressExercises = new AsyncTask<Object, Void, List<Exercise>>() {
            @Override
            @SuppressLint("WrongThread")
            protected List<Exercise> doInBackground(Object... params) {
                return new ProgressController().getProgressExercises(userId);
            }

            @Override
            protected void onPostExecute(List<Exercise> names) {
                if(names != null) {
                    ArrayAdapter<Exercise> adapter = new ArrayAdapter<Exercise>(context, android.R.layout.simple_spinner_dropdown_item, names) {};

                    // Set adapter for spinner
                    exercise.setAdapter(adapter);
                    exercise.setSelection(0);
                    exercise.setVisibility(View.VISIBLE);

                    // Handle when selected exercise is changed
                    exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            Exercise selectedEx  = (Exercise) parentView.getSelectedItem();
                            selectedExerciseId = selectedEx.getId(); // set selected exercise
                            drawGraph(); // Draw the graph again after each change
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });
                }
            }
        };

        getProgressExercises.execute();

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

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
                                Intent i = new Intent(GraphActivity.this, ExercisesActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_workouts:{
                                Intent i = new Intent(GraphActivity.this, WorkoutActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_schedule:{
                                Intent i = new Intent(GraphActivity.this, ScheduleActivity.class);
                                startActivity(i);
                                break;
                            }
                            case R.id.nav_progress:{
                                Intent i = new Intent(GraphActivity.this, ProgressActivity.class);
                                startActivity(i);
                                break;
                            }
                        }
                        return true;
                    }
                });
    }

    public void logout(View textView){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent i = new Intent(GraphActivity.this, LoginActivity.class);
        startActivity(i);
    }

    // Draw graph for selected exercise
    public void drawGraph() {
        // initialize  min and max weights
        minWeights = 1000000;
        maxWeights = 0;
        graph.removeAllSeries(); // start with a fresh graph
        // Fetch progress by id with async task
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> getProgressTask = new AsyncTask<Object, Void, String>() {

            @Override
            @SuppressLint("WrongThread")
            protected String doInBackground(Object... params) {
                // get progress for user
                return new ProgressController().getProgress(userId);
            }

            @Override
            protected void onPostExecute(String items) {
                Gson gson = new Gson();
                if(items != null) {
                    List<Progress> list = gson.fromJson(items, new TypeToken<List<Progress>>() {
                    }.getType());
                    Calendar calendar = Calendar.getInstance();
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                    List<Long> dates = new ArrayList<>();
                    List<Double> weights = new ArrayList<>();
                    for (Progress element : list) {
                        // Only show progress for selected exercise
                        exerciseIds.add(element.getExerciseId());
                        // check if exercise id matches selected exercise. If not, don't draw it.
                        if(selectedExerciseId == element.getExerciseId()) {
                            Date date = null;
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(element.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Add progress date to dates list and weights list
                            dates.add(date.getTime());
                            weights.add(element.getWeight());
                            // Add data points to graph
                            series.appendData(new DataPoint(date.getTime(), element.getWeight()), true, 100);
                            minWeights = element.getWeight();
                            maxDate = date.getTime();
                        }
                    }

                    // use weights list to set graph Y-axis boundaries
                    for(int i = 0; i < weights.size(); i++) {
                        if (minWeights > weights.get(i)) {
                            minWeights = weights.get(i);
                        }
                        if(maxWeights < weights.get(i)){
                            maxWeights = weights.get(i);
                        }
                    }
                    // use minDate and maxDate to set X-axis boundaries
                    minDate = dates.get(0);
                    if(dates.size()==1){
                        maxDate = minDate + 1;
                    }
                    series.setDrawDataPoints(true);
                    graph.addSeries(series);

                    // Format labels to display a date
                    gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                    gridLabel.setNumHorizontalLabels(4);

                    // set axis boundaries correctly
                    graph.getViewport().setMinY(minWeights-5);
                    graph.getViewport().setMaxY(maxWeights+5);
                    graph.getViewport().setMinX(minDate);
                    graph.getViewport().setMaxX(maxDate);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setYAxisBoundsManual(true);
                }
            }
        };

        getProgressTask.execute();
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