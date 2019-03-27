package com.example.pigs.controllers;

import android.util.Log;

import com.example.pigs.MainActivity;
import com.example.pigs.entities.Exercise;
import com.example.pigs.entities.Progress;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class WorkoutController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String TAG = MainActivity.class.getSimpleName();


}
