package com.example.personaltrainignapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object Constants {
    const val BASE_URL = "https://exercisedb.p.rapidapi.com/exercises/"
    const val API_KEY = "38d69bfad7mshe1ddb8dcfb829c8p1c872ejsn18acd4c5def8"
    const val SHARED_PREF_NAME = "user_pref"
    const val USER_KEY = "user_key"
    const val ROLE_KEY = "role_key"
    const val ADMIN = "admin"
    const val USER = "user"
    const val EXERCISE_KEY = "exercise_key"
    const val USER_ID = "user_id"
    const val EMAIL = "email"
    const val LANGUAGE_KEY = "language_key"
    fun generateRandomId(): String {
        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random =
            Random(System.currentTimeMillis()) // Seed the random number generator with the current time

        val randomString = StringBuilder(28)

        for (i in 0 until 28) {
            val randomIndex = random.nextInt(characters.length)
            randomString.append(characters[randomIndex])
        }

        return randomString.toString()
    }

    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis() / 1000 // Get current timestamp in seconds
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    fun getBodyPartsList(): List<String> {
        return listOf(
            "back",
            "cardio",
            "chest",
            "lower arms",
            "lower legs",
            "neck",
            "shoulders",
            "upper arms",
            "upper legs",
            "waist"
        )
    }

}