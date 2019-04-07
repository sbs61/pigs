package com.example.pigs.controllers;

import android.util.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.pigs.MainActivity;
import com.example.pigs.activities.progress.GraphActivity;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Progress;
import com.example.pigs.entities.Workout;
import com.example.pigs.entities.WorkoutExercise;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkoutController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = GraphActivity.class.getSimpleName();

    public String getAllWorkouts(int userId) {
        String url = "https://hugbun2.herokuapp.com/workout/" + userId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        String jsonString = null;

        try {
            Response res = client.newCall(request).execute();
            if (res.isSuccessful()) {
                jsonString = res.body().string();
                Log.e(TAG, res.toString());
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception caught: ", e);
        }

        return jsonString;
    }

    public Boolean createWorkout(String name, String category, List<String> exercises, String date) {
        Workout w = new Workout(null, 1L, name, category, exercises, date);
        String url = "https://hugbun2.herokuapp.com/workout";
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(w);

        RequestBody body = RequestBody.create(JSON, json);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String jsonString = null;

        try {
            Response res = client.newCall(request).execute();
            if (res.isSuccessful()){
                jsonString = res.body().string();
                Log.e(TAG, res.toString());
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception caught: ", e);
        }

        return false;
    }
}

