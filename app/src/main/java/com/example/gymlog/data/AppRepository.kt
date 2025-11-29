package com.example.gymlog.data

import android.content.Context
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepository @Inject constructor(
    context: Context
) {
    val db = AppDatabase.getDatabase(context)

    suspend fun getAllGymLogs() = db.gymLogDao().getAll()

    suspend fun getGymLogById(logId: Int) = db.gymLogDao().getById(logId)

    fun getGymLogByUserId(userId: Int) = db.gymLogDao().getByUserId(userId)

    suspend fun insertGymLog(gymLog: GymLog) {
        Log.i("GymLogRepository", "insertGymLog: $gymLog")
        db.gymLogDao().insert(gymLog)
    }


    suspend fun getUserById(userId: Int) = db.userDao().getById(userId)

    suspend fun getUserByUsername(username: String) = db.userDao().getByUsername(username)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppRepository? = null


        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: AppRepository(context).also { instance = it }
            }
    }
}