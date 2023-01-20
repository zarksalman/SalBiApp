package com.techventure.salbiapp.audioapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.techventure.salbiapp.audioapp.data.entities.Audio
import com.techventure.salbiapp.audioapp.other.Constants.AUDIO_COLLECTION
import kotlinx.coroutines.tasks.await

class AudioDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val audioCollection = fireStore.collection(AUDIO_COLLECTION)

    suspend fun getAllAudios(): List<Audio> {
        return try {
            audioCollection.get().await().toObjects(Audio::class.java)
        } catch (exception: Exception) {
            emptyList()
        }
    }
}