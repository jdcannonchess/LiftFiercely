package com.liftfiercely.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sets",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["workoutId"]), Index(value = ["exerciseId"])]
)
data class WorkoutSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workoutId: Long,
    val exerciseId: String,
    val reps: Int,
    val weight: Double,
    val targetReps: Int = 8,
    val overallSetNumber: Int = 1,
    val timestamp: Long = System.currentTimeMillis(),
    val setNumber: Int = 1  // Exercise-specific set number
)
