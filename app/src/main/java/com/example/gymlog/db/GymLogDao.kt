package com.example.gymlog.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface GymLogDao {
    @Query("SELECT * FROM gym_logs ORDER BY date DESC")
    fun getAll(): List<GymLog>

    @Query("SELECT * FROM gym_logs WHERE id = :id LIMIT 1")
    fun getById(id: Int): GymLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gymLog: GymLog)

    @Update
    fun update(gymLog: GymLog)

    @Delete
    fun delete(gymLog: GymLog)

    @Query("DELETE FROM gym_logs")
    fun deleteAll()
}