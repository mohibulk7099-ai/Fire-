package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_creations")
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val prompt: String,
    val motionPrompt: String? = null,
    val photoUri: String? = null,
    val style: String,
    val aspectRatio: String,
    val durationSeconds: Int,
    val quality: String,
    val status: String, // QUEUED, PROCESSING, COMPLETED, FAILED
    val progress: Float = 0f,
    val statusMessage: String = "",
    val videoUrl: String? = null,
    val localFilePath: String? = null,
    val thumbnailUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isDemo: Boolean = true,
    val errorMessage: String? = null
)
