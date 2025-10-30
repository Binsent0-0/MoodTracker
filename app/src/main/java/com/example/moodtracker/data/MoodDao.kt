package com.example.moodtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoodDao {
    @Insert
    suspend fun insertMood(mood: MoodEntity)

    @Update
    suspend fun updateMood(mood: MoodEntity)

    @Query("SELECT * FROM mood_info WHERE username = :username ORDER BY date DESC, time DESC LIMIT 1")
    suspend fun getLastMoodByUsername(username: String): MoodEntity?

    @Query("SELECT * FROM mood_info WHERE username = :username ORDER BY date DESC, time DESC")
    suspend fun getMoodsByUsername(username: String): List<MoodEntity>

    @Query("SELECT * FROM mood_info ORDER BY date DESC, time DESC")
    suspend fun getAllMoods(): List<MoodEntity>
}
