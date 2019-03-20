package com.example.pigs.controllers;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.example.pigs.MainActivity;
import com.example.pigs.entities.Exercise;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;

public class ExerciseController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = MainActivity.class.getSimpleName();

    public String getExercises(String name) {
        String url = "https://hugbun2.herokuapp.com/exercise/name?name="+name;

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

    public String getExercisesByCategory(String category) {
        String url = "https://hugbun2.herokuapp.com/category/"+category;

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

    public Boolean createExercise(String name, String category) {
        Exercise ex = new Exercise(null, name, category);
        String url = "https://hugbun2.herokuapp.com/exercise";
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(ex);

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
