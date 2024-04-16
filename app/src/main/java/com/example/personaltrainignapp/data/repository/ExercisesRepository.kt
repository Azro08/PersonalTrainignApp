package com.example.personaltrainignapp.data.repository

import com.example.personaltrainignapp.data.api.ExerciseApi
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.ExerciseDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExercisesRepository @Inject constructor(
    private val apiService: ExerciseApi,
    private val firebaseFirestore: FirebaseFirestore
) {

    private val exercisesCollection = firebaseFirestore.collection("exercises")

    suspend fun getExercises(bodyPart: String): List<ExerciseDto> {
        val apiExercisesList = apiService.getExercisesByBodyPart(bodyPart)
        val firestoreExercises = getFirestoreExercises(bodyPart)
        return apiExercisesList + firestoreExercises
    }

    suspend fun saveExercise(exercise: Exercise) {
        val exerciseRef = exercisesCollection.document(exercise.id)
        exerciseRef.set(exerciseRef)
    }

    private suspend fun getFirestoreExercises(bodyPart: String): List<ExerciseDto> {
        val querySnapshot = exercisesCollection
            .whereEqualTo("bodyPart", bodyPart)
            .get()
            .await()

        val exercises = mutableListOf<ExerciseDto>()
        for (document in querySnapshot.documents) {
            val exercise = document.toObject(ExerciseDto::class.java)
            exercise?.let {
                exercises.add(it)
            }
        }
        return exercises
    }

    suspend fun getExerciseById(id: String) = apiService.getExerciseById(id)

}