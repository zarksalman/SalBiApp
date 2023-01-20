package com.techventure.salbiapp.audioapp.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.techventure.salbiapp.audioapp.data.remote.AudioDatabase
import com.techventure.salbiapp.audioapp.exoplayer.State.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FirebaseAudioSource @Inject constructor(
    private val audioDb: AudioDatabase
) {

    var audios = emptyList<MediaMetadataCompat>()
    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = STATE_INITIALIZING
        val allAudios = audioDb.getAllAudios()

        audios = allAudios.map { audios ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ARTIST, audios.subtitle)
                .putString(METADATA_KEY_MEDIA_ID, audios.mediaId)
                .putString(METADATA_KEY_TITLE, audios.title)
                .putString(METADATA_KEY_DISPLAY_TITLE, audios.title)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, audios.imageUrl)
                .putString(METADATA_KEY_MEDIA_URI, audios.audioUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, audios.imageUrl)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, audios.subtitle)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, audios.subtitle)
                .build()

        }
        state = STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        audios.forEach { audio ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(audio.getString(METADATA_KEY_MEDIA_URI)))

            concatenatingMediaSource.addMediaSource(mediaSource)
        }

        return concatenatingMediaSource
    }

    fun adMediaItems() = audios.map { audio ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(audio.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(audio.description.title)
            .setSubtitle(audio.description.subtitle)
            .setMediaId(audio.description.mediaId)
            .setIconUri(audio.description.iconUri)
            .build()

        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)

    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()
    private var state = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListeners += action
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }
}

enum class State {

    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR,
}