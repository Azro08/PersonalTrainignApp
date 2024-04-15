package com.example.personaltrainignapp.data.repository

import android.util.Log
import com.example.personaltrainignapp.data.model.Workout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    firebaseFirestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    private val COLLECTION_WORKOUTS = "workouts"
    private val FIELD_USER_ID = "userId"

    private val workoutCollection = firebaseFirestore.collection(COLLECTION_WORKOUTS)

    suspend fun saveWorkout(workout: Workout): String {
        workout.userId = firebaseAuth.currentUser?.uid ?: ""
        return try {
            val workoutRef = workoutCollection.document(workout.id)
            workoutRef.set(workout)
            "Done"
        } catch (e: FirebaseException) {
            e.message ?: "Unknown error"
        }

    }

    suspend fun getWorkoutHistoryByDate(date: String): List<Workout> {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        val workouts = getAllWorkoutHistory()
        Log.d("Workout", workouts.toString())

        val filteredWorkouts = mutableListOf<Workout>()
        return try {
            for (workout in workouts) {
                Log.d("Workout", workout.toString())
                if (workout.date == date && workout.userId == userId) filteredWorkouts.add(workout)
            }
            filteredWorkouts
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of failure
        }
    }

    private suspend fun getAllWorkoutHistory(): List<Workout> {
        val userId = firebaseAuth.currentUser?.uid ?: ""
        Log.d("WorkoutHistory", "Current User ID: $userId")

        return try {
            val querySnapshot = workoutCollection
                .whereEqualTo(FIELD_USER_ID, userId)
                .get()
                .await()

            val workouts = mutableListOf<Workout>()
            for (document in querySnapshot.documents) {
                val workout = document.toObject(Workout::class.java)
                workout?.let {
                    workouts.add(it)
                }
            }
            Log.d("WorkoutHistory", "Retrieved ${workouts.size} workouts")
            workouts
        } catch (e: Exception) {
            Log.e("WorkoutHistory", "Error retrieving workout history: ${e.message}", e)
            emptyList() // Return an empty list in case of failure
        }
    }


}