package com.example.personaltrainignapp.data.repository

import android.net.Uri
import android.util.Log
import com.example.personaltrainignapp.data.api.ExerciseApi
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.ExerciseDto
import com.example.personaltrainignapp.data.model.toExerciseDto
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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
        val firestoreExercises = getFirestoreExercises(bodyPart)
        val apiExercisesList = apiService.getExercisesByBodyPart(bodyPart)
        Log.d("ExercisesList rep", firestoreExercises.toString())
        Log.d("ExercisesList rep", firestoreExercises.toString() + apiExercisesList.toString())
        return apiExercisesList + firestoreExercises.map { it.toExerciseDto() }
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

    private suspend fun getFirestoreExercises(bodyPart: String): List<Exercise> {
        return try {
            val querySnapshot = exercisesCollection.whereEqualTo("bodyPart", bodyPart).get().await()
            querySnapshot.toObjects(Exercise::class.java)
        } catch (e: Exception) {
            // Handle exceptions
            Log.d("ExeceptioFirebase", e.message.toString())
            emptyList()
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

    suspend fun getExerciseById(id: String): ExerciseDto {
        return try {
            apiService.getExerciseById(id)
        } catch (apiException: Exception) {
            fetchExerciseFromFirestore(id).toExerciseDto()
        }
    }

    private suspend fun fetchExerciseFromFirestore(id: String): Exercise {
        Log.d("ExerciseId", id)
        return withContext(Dispatchers.IO) {
            try {
                // Implement fetching from Firestore
                val db = FirebaseFirestore.getInstance()
                val exerciseDoc = db.collection("exercises").document(id).get().await()
                if (exerciseDoc.exists()) {
                    exerciseDoc.toObject(Exercise::class.java)
                } else {
                    null
                }
            } catch (firestoreException: Exception) {
                // Handle Firestore fetch failure
                // Log the error or handle it appropriately
                null
            }
        } ?: Exercise()
    }

}