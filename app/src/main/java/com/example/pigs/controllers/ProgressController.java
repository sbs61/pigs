package com.example.pigs.controllers;

import android.util.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.pigs.activities.progress.GraphActivity;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Progress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgressController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = GraphActivity.class.getSimpleName();

    // POST progress to api
    public Boolean createProgress(long exerciseId, long userId, int reps, int sets, Double weight, String date) {
        Progress pr = new Progress(null, exerciseId, userId, sets, reps, weight, date);
        String url = "https://hugbun2.herokuapp.com/progress";
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(pr);
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

    // Get a users progress by user id
    public String getProgress(int userId) {
        String url = "https://hugbun2.herokuapp.com/progress?uid="+userId;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        String jsonString = null;

        try {
            Response res = client.newCall(request).execute();
            if (res.isSuccessful()){
                jsonString = res.body().string();
                Log.e(TAG, res.toString());
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception caught: ", e);
        }

        return jsonString;
    }

    // Get exercises that a user has progress for
    public List<Exercise> getProgressExercises(int userId) {
        String progress = getProgress(userId);

        List<Exercise> exercises = new ArrayList<>();
        if(progress != null) {
            Gson gson = new Gson();
            List<Progress> list = gson.fromJson(progress, new TypeToken<List<Progress>>() {}.getType());

            List<Long> exerciseIds = new ArrayList<>();
            for(Progress element : list) {
                // Add exercise name to list
                if(!exerciseIds.contains(element.getExerciseId())) {
                    exerciseIds.add(element.getExerciseId());
                }
            }

            for(Long id : exerciseIds) {
                // Get exercise from db
                String ex = new ExerciseController().getExerciseById(id);
                Exercise exercise = gson.fromJson(ex, Exercise.class);
                exercises.add(exercise);
            }
        }
        return exercises;
    }
}

