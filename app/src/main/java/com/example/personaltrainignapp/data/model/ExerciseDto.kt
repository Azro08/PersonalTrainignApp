package com.example.personaltrainignapp.data.model


import com.google.gson.annotations.SerializedName

data class ExerciseDto(
    @SerializedName("bodyPart")
    val bodyPart: String,
    @SerializedName("equipment")
    val equipment: String,
    @SerializedName("gifUrl")
    val gifUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instructions")
    val instructions: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("secondaryMuscles")
    val secondaryMuscles: List<String>,
    @SerializedName("target")
    val target: String
)

fun ExerciseDto.toExercise() = Exercise(
    bodyPart, equipment, gifUrl, id, instructions, name, secondaryMuscles, target
)