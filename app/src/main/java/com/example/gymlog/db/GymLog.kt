package com.example.gymlog.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gym_logs")
data class GymLog(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val exerciseName: String,
    val weight: String,
    val reps: String,
    val date: Long
)