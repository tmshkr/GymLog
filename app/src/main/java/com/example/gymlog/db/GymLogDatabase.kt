package com.example.gymlog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, GymLog::class],
    version = 1,
    exportSchema = false
)
abstract class GymLogDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun gymLogDao(): GymLogDao


    companion object {
        val DATABASE_NAME = "gym_log_db"

        // For Singleton instantiation
        @Volatile private var instance: GymLogDatabase? = null

        fun getDatabase(context: Context): GymLogDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): GymLogDatabase {
            return Room.databaseBuilder(context, GymLogDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }
                )
                .build()
        }
    }
}
