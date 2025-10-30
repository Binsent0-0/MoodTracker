package com.example.moodtracker

import com.example.moodtracker.data.AppDatabase
import com.example.moodtracker.data.MoodEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

object MoodHistoryRepository {
    private lateinit var database: AppDatabase

    fun initialize(database: AppDatabase) {
        this.database = database
    }

    fun addMood(username: String, moodName: String, causes: List<Cause>, note: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val moodEntity = MoodEntity(
                username = username,
                mood = moodName,
                date = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM")),
                time = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")),
                day = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                dayOfWeek = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                tags = causes.joinToString(",") { it.text },
                note = note
            )
            database.moodDao().insertMood(moodEntity)
        }
    }

    fun addMoodDirect(username: String, moodName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val moodEntity = MoodEntity(
                username = username,
                mood = moodName,
                date = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM")),
                time = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")),
                day = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                dayOfWeek = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                tags = "",
                note = ""
            )
            database.moodDao().insertMood(moodEntity)
        }
    }

    suspend fun getMoodHistory(username: String): List<MoodHistory> {
        return database.moodDao().getMoodsByUsername(username).map { entity ->
            MoodHistory(
                date = entity.date,
                day = entity.day,
                dayOfWeek = entity.dayOfWeek,
                mood = entity.mood,
                time = entity.time,
                tags = entity.tags.split(",").map { it.trim() },
                note = entity.note
            )
        }.sortedByDescending { it.date }
    }
}
