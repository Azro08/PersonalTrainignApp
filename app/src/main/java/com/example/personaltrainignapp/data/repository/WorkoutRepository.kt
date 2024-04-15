package com.example.personaltrainignapp.data.repository

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
    private val FIELD_DATE = "date"
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
        return try {
            val querySnapshot = workoutCollection
                .whereEqualTo(FIELD_USER_ID, userId)
                .whereEqualTo(FIELD_DATE, date)
                .get()
                .await()

            val workouts = mutableListOf<Workout>()
            for (document in querySnapshot.documents) {
                val workout = document.toObject(Workout::class.java)
                workout?.let {
                    workouts.add(it)
                }
            }
            workouts
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of failure
        }
    }

    suspend fun getAllWorkoutHistory(): List<Workout> {
        val userId = firebaseAuth.currentUser?.uid ?: ""
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
            workouts
        } catch (e: Exception) {
            emptyList() // Return an empty list in case of failure
        }
    }

}