package com.example.gymlog.db

import android.app.Application

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GymLogRepository @Inject constructor(
    private val gymLogDao: GymLogDao,
    private val userDao: UserDao
) {

    fun getAllGymLogs() = gymLogDao.getAll()

    fun getGymLogById(logId: Int) = gymLogDao.getById(logId)

    suspend fun insertGymLog(gymLog: GymLog) = gymLogDao.insert(gymLog)

    fun getUserById(userId: Int) = userDao.getById(userId)

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: GymLogRepository? = null

        fun getInstance(gymLogDao: GymLogDao, userDao: UserDao) =
            instance ?: synchronized(this) {
                instance ?: GymLogRepository(gymLogDao, userDao).also { instance = it }
            }
    }
}