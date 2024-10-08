package com.example.personaltrainignapp.data.repository

import android.net.Uri
import com.example.personaltrainignapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UsersRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage,

    ) {

    private val usersCollection = firestore.collection("users")

    suspend fun getUser(userId: String): User? {
        val userDocument = usersCollection.document(userId)
        val documentSnapshot = userDocument.get().await()
        return if (documentSnapshot.exists()) {
            documentSnapshot.toObject(User::class.java)
        } else {
            null
        }
    }

    suspend fun deleteAccount(userId: String): String {
        val userDocument = usersCollection.document(userId)
        return try {
            userDocument.delete().await()
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    suspend fun saveUser(user: User): String {
        return try {
            // Firestore-specific logic here
            val userDocRef = usersCollection.document(user.id)
            userDocRef.set(user).await()
            "Done"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

    suspend fun updateUserFields(
        userId: String,
        updatedFields: Map<String, Any>,
        password: String,
        oldPassword: String = "",
        email: String = ""
    ): String {
        val userDocument = usersCollection.document(userId)

        val result = runCatching {
            userDocument.update(updatedFields).await()
            if (password.isNotEmpty() && oldPassword.isNotEmpty() && email.isNotEmpty()) {
                runCatching {
                    firebaseAuth.signInWithEmailAndPassword(email, oldPassword).await()
                }.onFailure {
                    return it.message.toString()
                }
                runCatching {
                    firebaseAuth.currentUser?.updatePassword(password)?.await()
                }.onFailure {
                    return it.message.toString()
                }
            }
            "Done"
        }.onFailure {
            return it.message.toString()
        }

        return result.getOrThrow()
    }


    suspend fun uploadImageAndGetUri(userId: String, imageUri: Uri): Uri? {
        return try {
            val imageFilename = "profile_images/$userId/${UUID.randomUUID()}.jpg"
            val imageRef = storage.reference.child(imageFilename)

            imageRef.putFile(imageUri).await()

            // Use await to wait for the task to complete
            try {
                imageRef.downloadUrl.await()
            } catch (e: Exception) {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


}
