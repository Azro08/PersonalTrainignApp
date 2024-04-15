package com.example.personaltrainignapp.data.model


data class Exercise(
    val bodyPart: String = "",
    val equipment: String = "",
    val gifUrl: String = "",
    val id: String = "",
    val instructions: List<String> = emptyList(),
    val name: String = "",
    val secondaryMuscles: List<String> = emptyList(),
    val target: String = ""
)