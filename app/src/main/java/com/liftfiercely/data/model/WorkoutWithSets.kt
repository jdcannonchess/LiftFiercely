package com.liftfiercely.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithSets(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "workoutId"
    )
    val sets: List<WorkoutSet>
)


