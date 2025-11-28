package com.example.gymlog.db

import android.content.Context
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GymLogRepository @Inject constructor(
    context: Context
) {
    val db = GymLogDatabase.getDatabase(context)

    fun getAllGymLogs() = db.gymLogDao().getAll()

    fun getGymLogById(logId: Int) = db.gymLogDao().getById(logId)

    suspend fun insertGymLog(gymLog: GymLog) = db.gymLogDao().insert(gymLog)

    fun getUserById(userId: Int) = db.userDao().getById(userId)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: GymLogRepository? = null


        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: GymLogRepository(context).also { instance = it }
            }
    }
}