package com.example.personaltrainignapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.personaltrainignapp.data.api.ExerciseApi
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.ExerciseDto
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class ExercisesRepository @Inject constructor(
    private val apiService: ExerciseApi,
    firebaseFirestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val exercisesCollection = firebaseFirestore.collection("exercises")

    suspend fun getExercises(bodyPart: String): List<ExerciseDto> {
        Log.d("BodyPart Rep", bodyPart)
        val apiExercisesList = apiService.getExercisesByBodyPart(bodyPart)
        val firestoreExercises = getFirestoreExercises(bodyPart)
        Log.d("ExercisesList rep", firestoreExercises.toString())
        Log.d("ExercisesList rep", firestoreExercises.toString() + apiExercisesList.toString())
        return apiExercisesList + firestoreExercises
    }

    suspend fun saveExercise(exercise: Exercise): String {
        return try {
            val exerciseRef = exercisesCollection.document(exercise.id)
            exerciseRef.set(exercise).await()
            "Done"
        } catch (e: FirebaseException) {
            e.message ?: "Unknown error"
        }
    }

    private suspend fun getFirestoreExercises(bodyPart: String): List<ExerciseDto> {
        return try {
            val querySnapshot = exercisesCollection
                .whereEqualTo(
                    "bodyPart",
                    bodyPart
                )
                .get()
                .await()

            val exercisesList = mutableListOf<ExerciseDto>()
            for (document in querySnapshot.documents) {
                val exercise = document.toObject(ExerciseDto::class.java)
                exercise?.let {
                    exercisesList.add(it)
                }
            }

            exercisesList
        } catch (e: Exception) {
            // Handle exceptions (e.g., FirestoreException)
            emptyList() // Return empty list or handle error as needed
        }
    }

    suspend fun uploadImageAndGetUri(userId: String, imageUri: Uri): String {
        return try {
            val imageFilename = "exercises_images/$userId/${UUID.randomUUID()}.jpg"
            val imageRef = storage.reference.child(imageFilename)
            imageRef.putFile(imageUri).await()
            val imageUrl = imageRef.downloadUrl.await()
            imageUrl.toString()
        } catch (e: Exception) {
            ""
        }

    }

    suspend fun getExerciseById(id: String) = apiService.getExerciseById(id)

}