package com.example.moodtracker

import java.time.ZonedDateTime

object MoodHistoryRepository {
    private val moodHistory = mutableListOf<MoodHistory>()

    fun addMood(moodName: String, causes: List<Cause>, note: String) {
        moodHistory.add(
            MoodHistory(
                date = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd MMM")) ,
                day = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                dayOfWeek = ZonedDateTime.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                mood = moodName,
                time = ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a")), // 12:30 PM
                tags = causes.map { it.text },
                note = note
            )
        )
    }

    fun getMoodHistory(): List<MoodHistory> {
        return moodHistory.sortedByDescending { it.date }
    }
}
