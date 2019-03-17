package com.example.pigs.activities.exercise;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.pigs.MainActivity;
import com.example.pigs.R;
import com.example.pigs.controllers.ExerciseController;
import com.example.pigs.entities.Exercise;

public class ExercisesActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AsyncTask task = new ExercisesActivity.FetchItemsTask();
        task.execute();

        setContentView(R.layout.activity_exercise);

        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
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

    private class FetchItemsTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            Exercise ex = new Exercise(2L, "Bench press", "Chest");
            return new ExerciseController().createExercise(ex);
        }

        @Override
        protected void onPostExecute(Boolean items) {
            System.out.println(items);
        }
    }
}

