package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM video_creations ORDER BY createdAt DESC")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM video_creations WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: String): VideoEntity?

    @Query("SELECT * FROM video_creations WHERE status = 'COMPLETED' ORDER BY createdAt DESC LIMIT 5")
    fun getRecentCompletedVideos(): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Query("UPDATE video_creations SET status = :status, progress = :progress, statusMessage = :message, videoUrl = :videoUrl, errorMessage = :error WHERE id = :id")
    suspend fun updateJobProgress(id: String, status: String, progress: Float, message: String, videoUrl: String?, error: String?)

    @Query("UPDATE video_creations SET localFilePath = :path WHERE id = :id")
    suspend fun updateLocalPath(id: String, path: String)

    @Delete
    suspend fun deleteVideo(video: VideoEntity)

    @Query("DELETE FROM video_creations WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM video_creations")
    suspend fun clearAll()
}
