package com.example.pigs.controllers;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class ExerciseController {
    public static final String TAG = MainActivity.class.getSimpleName();

    public String getExercises() {
        String url = "https://hugbun2.herokuapp.com/exercise";

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


}
