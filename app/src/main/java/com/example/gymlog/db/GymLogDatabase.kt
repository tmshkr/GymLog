package com.example.gymlog.db

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
                            Log.i("GymLogDatabase", "Seeding initial data...")
                            // seed initial admin user
                            val username = "admin"
                            val hash = hashPassword("password123")
                            db.execSQL("INSERT INTO users (username, passwordSalt, passwordHash) VALUES ('${username}', '${hash.first}', '${hash.second}')")
                        }
                    }
                )
                .build()
        }
    }
}
