package com.example.personaltrainignapp.data.api

import com.example.personaltrainignapp.data.model.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApi {

    @GET("bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String
    ): List<ExerciseDto>

    @GET("exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") id: String
    ): ExerciseDto

}