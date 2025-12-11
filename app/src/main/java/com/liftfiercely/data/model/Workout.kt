package com.liftfiercely.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val isActive: Boolean = true
) {
    fun getDurationMinutes(): Long? {
        return endTime?.let { (it - startTime) / 60000 }
    }
}


