package com.example.gymlog.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GymLogDao {
    @Query("SELECT * FROM gym_logs ORDER BY date DESC")
    suspend fun getAll(): List<GymLog>

    @Query("SELECT * FROM gym_logs WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): GymLog?

    @Query("SELECT * FROM gym_logs WHERE userId = :userId ORDER BY date DESC")
    suspend fun getByUserId(userId: Int): List<GymLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gymLog: GymLog)

    @Update
    suspend fun update(gymLog: GymLog)

    @Delete
    suspend fun delete(gymLog: GymLog)

    @Query("DELETE FROM gym_logs")
    suspend fun deleteAll()
}