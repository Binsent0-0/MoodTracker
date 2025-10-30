package com.example.moodtracker

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object MoodHistoryRepository {
    private val moodHistory = mutableListOf<MoodHistory>()

    fun addMood(moodName: String, causes: List<Cause>, note: String) {
        val now = ZonedDateTime.now()
        val today = ZonedDateTime.now().toLocalDate()

        val dayText = when {
            now.toLocalDate().isEqual(today) -> "Today"
            now.toLocalDate().isEqual(today.minusDays(1)) -> "Yesterday"
            else -> now.format(DateTimeFormatter.ofPattern("dd MMM"))
        }

        moodHistory.add(
            MoodHistory(
                date = now.format(DateTimeFormatter.ofPattern("dd MMM")),
                day = dayText,
                dayOfWeek = now.format(DateTimeFormatter.ofPattern("EEEE")),
                mood = moodName,
                time = now.format(DateTimeFormatter.ofPattern("hh:mm a")), // 12:30 PM
                tags = causes.map { it.text },
                note = note,
                zonedDateTime = now
            )
        )
    }

    fun getMoodHistory(): List<MoodHistory> {
        return moodHistory.sortedByDescending { it.zonedDateTime }
    }
}
