package com.example.moodtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserEntity::class, MoodEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mood_tracker_database"
                )
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Pre-populate with sample users
            CoroutineScope(Dispatchers.IO).launch {
                val userDao = getDatabase(context).userDao()
                val sampleUsers = listOf(
                    UserEntity("roxanneroxas", "roxanneroxas", "Roxanne", "Roxas"),
                    UserEntity("kyrabassig", "kyrabassig", "Kyra", "Bassig"),
                    UserEntity("loisagulto", "loisagulto", "Lois", "Agulto"),
                    UserEntity("kianagulan", "kianagulan", "Kian", "Agulan"),
                    UserEntity("romnavarro", "romnavarro", "Rom", "Navarro"),
                    UserEntity("lestermendoza", "lestermendoza", "Lester", "Mendoza"),
                    UserEntity("vincerufino", "vincerufino", "Vince", "Rufino")
                )
                sampleUsers.forEach { userDao.insertUser(it) }
            }
        }
    }
}
