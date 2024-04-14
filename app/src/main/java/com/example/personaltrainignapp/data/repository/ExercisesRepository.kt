package com.example.personaltrainignapp.data.repository

import com.example.personaltrainignapp.data.api.ExerciseApi
import javax.inject.Inject

class ExercisesRepository @Inject constructor(
    private val apiService: ExerciseApi
) {

    suspend fun getExercises(bodyPart: String) = apiService.getExercisesByBodyPart(bodyPart)

    suspend fun getExerciseById(id: String) = apiService.getExerciseById(id)

}