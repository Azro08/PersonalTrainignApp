package com.example.personaltrainignapp.data.repository

import com.example.personaltrainignapp.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {


    suspend fun login(email: String, password: String): String {
        val deferred = CompletableDeferred<String>()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        deferred.complete("Done")
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error"
                        deferred.complete(errorMessage)
                    }
                }
        } catch (e: Exception) {
            deferred.complete(e.message ?: "Unknown error")
        }

        return deferred.await()
    }


    suspend fun signup(user: User, password: String): String = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = FirebaseAuth.getInstance()
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()

            val uid = authResult.user?.uid ?: ""
            if (uid.isNotEmpty()) {
                val result = saveUser(user)
                if (result == "Done") {
                    "Done"
                } else {
                    result
                }
            } else {
                "Error creating account"
            }
        } catch (e: Exception) {
            e.message ?: "Unknown error"
        }
    }

    private suspend fun saveUser(user: User): String = withContext(Dispatchers.IO) {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        return@withContext try {
            usersCollection.document(user.id).set(user).await()
            "Done"
        } catch (e: Exception) {
            e.message ?: "Unknown error"
        }
    }

    // Extension function to convert FirebaseTask to suspend function
    private suspend fun <T> Task<T>.await(): T = suspendCoroutine { cont ->
        addOnSuccessListener { result ->
            cont.resume(result)
        }
        addOnFailureListener { e ->
            cont.resumeWithException(e)
        }
    }


}