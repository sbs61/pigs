package com.example.pigs.activities.progress;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.activities.workout.CreateWorkoutActivity;
import com.example.pigs.activities.workout.ScheduleActivity;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.controllers.WorkoutController;
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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class GraphActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    private DrawerLayout drawerLayout;
    private GraphView graph;
    private GridLabelRenderer gridLabel;
    private long selectedExerciseId = 1;
    Set<Long> exerciseIds = new LinkedHashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        graph = (GraphView) findViewById(R.id.graph);

        // set manual Y bounds
        /*
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(150);
        */

        // set date label formatter
        gridLabel = graph.getGridLabelRenderer();
        gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
        gridLabel.setNumHorizontalLabels(4); // only 4 because of the space
        gridLabel.setNumVerticalLabels(6);

        // set manual X bounds
        /*
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        */

        // Round numbers
        graph.getGridLabelRenderer().setHumanRounding(true, true);
        graph.getGridLabelRenderer().setPadding(32);

        drawGraph();

        Spinner exercise = (Spinner)findViewById(R.id.selectedExercise);

        String[] items = new String[]{"Bicep curls", "Bench press", "Pick a category"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items) {
            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };

        exercise.setAdapter(adapter);
        exercise.setSelection(adapter.getCount());

        exercise.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedEx  = exercise.getSelectedItem().toString();
                System.out.println(selectedEx);
                // TODO: make selectedExerciseId equal id of chosen exercise in spinner
                if(selectedEx == "Bicep curls"){
                    selectedExerciseId = 1;
                } else if(selectedEx ==  "Bench press") {
                    selectedExerciseId = 2;
                }
                drawGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        // Non-working implementation, can be ignored
        /*
        // Fetch progress by id with async task
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String[]> task2 = new AsyncTask<Object, Void, String[]>() {

            @Override
            @SuppressLint("WrongThread")
            protected String[] doInBackground(Object... params) {
                // TODO: Get id from logged in user when authentication is implemented
                return (String[]) new ExerciseController().getExercisesByIds(exerciseIds.toArray());
            }

            protected void onPostExecute(String[] items) {
                Gson gson = new Gson();
                //Exercise ex = gson.fromJson(items, Exercise.class);
                    if (items != null) {
                    }
                        List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {
                        }.getType());

                        Calendar calendar = Calendar.getInstance();
                        Date minDate = calendar.getTime();
                        double minWeights = 0;
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                        for (Exercise element : list) {
                            System.out.println(element.getName());
                        System.out.println(items[i]);
                        }
                    }
        };

        task2.execute();
        */

        /*
        String[] exercises =  new String[exerciseIds.size()];
        System.out.println(exerciseIds);
        for(int i = 0; i < exerciseIds.size(); i++) {
            // Fetch progress by id with async task
            int finalI = i;
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Object, Void, String> task2 = new AsyncTask<Object, Void, String>() {

                @Override
                @SuppressLint("WrongThread")
                protected String doInBackground(Object... params) {
                    // TODO: Get id from logged in user when authentication is implemented
                    return new ExerciseController().getExerciseById((int) exerciseIds.toArray()[0]);
                }

                @Override
                protected void onPostExecute(String items) {
                    Gson gson = new Gson();
                    if (items != null) {
                    }
                    List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {
                    }.getType());

                    for (Exercise element : list) {
                        exercises[finalI] = element.getName();
                    }
                }
            };

            task2.execute();
        }
        */


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
                                Intent i = new Intent(GraphActivity.this, CreateWorkoutActivity.class);
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

    // Draw graph for selected exercise
    public void drawGraph() {
        graph.removeAllSeries();
        // Fetch progress by id with async task
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> task1 = new AsyncTask<Object, Void, String>() {

            @Override
            @SuppressLint("WrongThread")
            protected String doInBackground(Object... params) {
                // TODO: Get id from logged in user when authentication is implemented
                Integer userId = 1;
                return new WorkoutController().getProgress(userId);
            }

            @Override
            protected void onPostExecute(String items) {
                Gson gson = new Gson();
                if(items != null) {
                    List<Progress> list = gson.fromJson(items, new TypeToken<List<Progress>>() {
                    }.getType());

                    Calendar calendar = Calendar.getInstance();
                    Date minDate = calendar.getTime();
                    double minWeights = 0;
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                    for (Progress element : list) {
                        // Only show progress for selected exercise
                        //exerciseIds.add(element.getExerciseId());
                        // System.out.println(exerciseIds);
                        // selectedExerciseId = (long) exerciseIds.toArray()[0];
                        System.out.println(selectedExerciseId);
                        if(selectedExerciseId == element.getExerciseId()) {
                            Date date = null;
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd").parse(element.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            // Add data points to graph
                            series.appendData(new DataPoint(date.getTime(), element.getWeight()), true, 40);
                            minWeights = element.getWeight();
                        }
                    }

                    series.setDrawDataPoints(true);
                    graph.addSeries(series);

                    gridLabel = graph.getGridLabelRenderer();
                    gridLabel.setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                    gridLabel.setNumHorizontalLabels(4); // only 4 because of the space
                    gridLabel.setNumVerticalLabels(6);

                    graph.getViewport().setMinY(minWeights-10);
                }
            }
        };

        task1.execute();
    }

    // TODO: Add exercises user has progress on into the spinner
    private void setSpinner(){
        String[] exercises =  new String[exerciseIds.size()];
        System.out.println((Long)exerciseIds.toArray()[0]);
            // Fetch progress by id with async task
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Object, Void, String> task2 = new AsyncTask<Object, Void, String>() {

                @Override
                @SuppressLint("WrongThread")
                protected String doInBackground(Object... params) {
                    // TODO: Get id from logged in user when authentication is implemented
                    return new ExerciseController().getExerciseById(1);
                }

                @Override
                protected void onPostExecute(String items) {
                    Gson gson = new Gson();
                    if (items != null) {
                    }
                    List<Exercise> list = gson.fromJson(items, new TypeToken<List<Exercise>>() {}.getType());

                    for (Exercise element : list) {
                        exercises[0] = element.getName();
                    }
                }
            };

            task2.execute();
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