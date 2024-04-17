package com.example.personaltrainignapp.util

import android.content.Context
import android.content.SharedPreferences
import com.example.personaltrainignapp.data.model.WorkoutDay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object WorkoutDaySharedPreferencesHelper {

    private const val WORKOUT_DAYS_PREF_NAME = "WorkoutDays"

    fun saveWorkoutDays(context: Context, workoutDays: List<WorkoutDay>) {
        val sharedPreferences = context.getSharedPreferences(WORKOUT_DAYS_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Serialize the list of WorkoutDay objects to JSON using Gson
        val gson = Gson()
        val json = gson.toJson(workoutDays)

        // Save the JSON string to SharedPreferences
        editor.putString("WorkoutDays", json)
        editor.apply()
    }

    fun loadWorkoutDays(context: Context): List<WorkoutDay> {
        val sharedPreferences = context.getSharedPreferences(WORKOUT_DAYS_PREF_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("WorkoutDays", null)

        if (json != null) {
            // Deserialize the JSON string to a list of WorkoutDay objects using Gson
            val gson = Gson()
            val type = object : TypeToken<List<WorkoutDay>>() {}.type
            return gson.fromJson(json, type)
        }

        return emptyList()
    }
}
