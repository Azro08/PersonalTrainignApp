package com.example.personaltrainignapp.data.model

data class Workout(
    val id : String = "",
    val exercise : Exercise = Exercise(),
    var userId : String = "",
    val date : String = "",
    val weights : List<Double> = emptyList(),
    val reps : List<Int> = emptyList()
)