package com.example.gymlog.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users")
    fun getAll(): List<User>
}