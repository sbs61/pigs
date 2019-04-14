package com.example.pigs.activities.authentication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pigs.R;
import com.example.pigs.controllers.AuthenticationController;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    // Register button handler
    public void registerUser(View button){
        EditText u = findViewById(R.id.register_username);
        EditText p = findViewById(R.id.register_password);
        System.out.println("ok" + u.getText().toString().equals(""));
        if(!u.getText().toString().equals("") && !p.getText().toString().equals("")) {
            @SuppressLint("StaticFieldLeak")
            AsyncTask<Object, Void, Boolean> registerTask = new AsyncTask<Object, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Object... params) {
                    EditText username = (EditText) findViewById(R.id.register_username);
                    String reg_username = username.getText().toString();
                    EditText password = (EditText) findViewById(R.id.register_password);
                    String reg_password = password.getText().toString();

                    // Call register function from Authentication Controller
                    return new AuthenticationController().register(reg_username, reg_password);
                }

                @Override
                protected void onPostExecute(Boolean items) {
                    System.out.println(items);
                    Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            };
            registerTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "One does not simply " +
                    "leave a field empty when creating a user", Toast.LENGTH_LONG).show();
        }
    }
}