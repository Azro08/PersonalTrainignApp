package com.example.personaltrainignapp.data.api

import com.example.personaltrainignapp.data.model.ExerciseDto
import com.example.personaltrainignapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseApi {

    @GET("bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String,
        @Query("rapidapi-key") apiKey : String = Constants.API_KEY
    ): List<ExerciseDto>

    @GET("exercise/{id}")
    suspend fun getExerciseById(
        @Path("id") id: String,
        @Query("rapidapi-key") apiKey : String = Constants.API_KEY
    ): ExerciseDto

}