package com.example.moodtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_credentials")
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
