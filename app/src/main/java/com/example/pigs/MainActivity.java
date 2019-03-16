package com.example.pigs;

<<<<<<< HEAD
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
=======
import android.os.AsyncTask;
>>>>>>> ca36ed229f411711d051477a262a05002d07e083
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.pigs.R;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ExerciseController exerciseController;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_create_exercise);

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

                        return true;
                    }
                });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
=======

        setContentView(R.layout.activity_exercise);




        AsyncTask task = new FetchItemsTask();
        task.execute();

    }

    private class FetchItemsTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            return new ExerciseController().getExercises();
        }

        @Override
        protected void onPostExecute(String items) {
            System.out.println(items);
        }
>>>>>>> ca36ed229f411711d051477a262a05002d07e083
    }
}