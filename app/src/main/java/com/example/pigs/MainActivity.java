package com.example.pigs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pigs.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        final int day = 25;
        final int month2 = 3;
        final int year2 = 2019;
        CalendarView calendarView = (CalendarView) findViewById(R.id.CalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), "" + dayOfMonth + "." + (month+1) + "." + year, 5).show();
                TextView foundWorkout = (TextView) findViewById(R.id.foundWorkout);
                if(dayOfMonth == day && (month+1) == month2 && year == year2) {
                    foundWorkout.setText("Bench Press");
                }
                else {
                    foundWorkout.setText("No Workout Found");
                }
            }
        });
    }
}
