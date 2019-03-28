package com.example.pigs.entities;

import java.util.Date;
import java.util.List;

public class Workout {
    private Long id;
    private Long userId;
    private String name;
    private String category;
    private List<WorkoutExercise> exercises;
    private String date;
    private int day;
    private int month;
    private int year;

    public Workout() {
    }

    public Workout(Long id, Long userId, String name, String category, List exercises, String date) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.exercises = exercises;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<WorkoutExercise> getExercises() { return exercises; }

    public void setExercises(List exercises) { this.exercises = exercises; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }
}
