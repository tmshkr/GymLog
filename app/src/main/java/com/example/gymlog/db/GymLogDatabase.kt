package com.example.gymlog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gymlog.utils.hashPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, GymLog::class],
    version = 1,
    exportSchema = false
)
abstract class GymLogDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun gymLogDao(): GymLogDao


    companion object {
        val DATABASE_NAME = "gym_log.db"

        // For Singleton instantiation
        @Volatile
        private var instance: GymLogDatabase? = null

        fun getDatabase(context: Context): GymLogDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): GymLogDatabase {
            return Room.databaseBuilder(context, GymLogDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val hash = hashPassword("password123")

                                instance?.userDao()?.insertAll(
                                    User(
                                        username = "admin",
                                        passwordHash = hash.second,
                                        passwordSalt = hash.first
                                    )
                                )
                            }

                        }
                    }
                )
                .build()
        }
    }
}
