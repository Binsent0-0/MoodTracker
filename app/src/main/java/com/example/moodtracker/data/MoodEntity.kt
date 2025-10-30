package com.example.moodtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_info")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val mood: String,
    val date: String,
    val time: String,
    val day: String,
    val dayOfWeek: String,
    val tags: String, // Comma-separated causes
    val note: String
)
