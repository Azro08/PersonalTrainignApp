package com.example.personaltrainignapp.data.model

data class WorkoutDay(
    val dayNumber: Int,
    var plan: String = "",
    var completed : Boolean = false
)
