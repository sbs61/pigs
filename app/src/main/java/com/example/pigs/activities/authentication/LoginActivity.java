package com.example.pigs.activities.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pigs.R;
import com.example.pigs.activities.exercise.ExercisesActivity;
import com.example.pigs.controllers.AuthenticationController;

public class LoginActivity extends AppCompatActivity {

    private TextView incorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        incorrect = findViewById(R.id.incorrect);
    }

    // Initiate register activity when Register button is clicked
    public void goToRegister(View button){
        Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(i);
    }

    // Handler for login button, take input from username and password fields
    public void userLogin(View button){
        incorrect.setVisibility(View.GONE);
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Object, Void, String> loginTask = new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                EditText username = (EditText) findViewById(R.id.username);
                String login_username = username.getText().toString();
                EditText password = (EditText) findViewById(R.id.password);
                String login_password = password.getText().toString();

                // Call register function from Authentication Controller
                System.out.println(login_username + " " + login_password);
                return new AuthenticationController().login(login_username, login_password);
            }
            @Override
            protected void onPostExecute(String items) {
                if(items != null && !items.equals("")){
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putString("userId", items);
                    editor.apply();
                    Intent i = new Intent(LoginActivity.this, ExercisesActivity.class);
                    startActivity(i);
                } else {
                    incorrect.setVisibility(View.VISIBLE);
                }
            }
        };
        loginTask.execute();
    }
}