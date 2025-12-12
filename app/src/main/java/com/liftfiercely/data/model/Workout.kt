package com.liftfiercely.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val isActive: Boolean = true,
    val bodyWeight: Double = 0.0  // 0 = not recorded, use settings fallback
) {
    fun getDurationMinutes(): Long? {
        return endTime?.let { (it - startTime) / 60000 }
    }
}


