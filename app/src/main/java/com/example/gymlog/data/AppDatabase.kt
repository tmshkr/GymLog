package com.example.gymlog.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gymlog.utils.hashPassword

@Database(
    entities = [User::class, GymLog::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun gymLogDao(): GymLogDao


    companion object {
        val DATABASE_NAME = "gym_log.db"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.i("AppDatabase", "Seeding initial data...")
                            // seed initial users
                            val users = listOf(
                                Pair("admin", "adminpassword"),
                                Pair("user2", "user2password"),
                                Pair("user3", "user3password")
                            )

                            for ((username, password) in users) {
                                val hash = hashPassword(password)
                                db.execSQL("INSERT INTO users (username, passwordSalt, passwordHash) VALUES ('${username}', '${hash.first}', '${hash.second}')")
                            }
                        }
                    }
                )
                .build()
        }
    }
}
