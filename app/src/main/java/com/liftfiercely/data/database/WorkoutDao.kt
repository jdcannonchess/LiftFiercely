package com.liftfiercely.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutWithSets
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    
    @Insert
    suspend fun insertWorkout(workout: Workout): Long
    
    @Update
    suspend fun updateWorkout(workout: Workout)
    
    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutById(id: Long): Workout?
    
    @Query("SELECT * FROM workouts WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveWorkout(): Workout?
    
    @Query("SELECT * FROM workouts WHERE isActive = 1 LIMIT 1")
    fun getActiveWorkoutFlow(): Flow<Workout?>
    
    @Query("SELECT * FROM workouts WHERE isActive = 0 ORDER BY startTime DESC")
    fun getCompletedWorkoutsFlow(): Flow<List<Workout>>
    
    @Query("SELECT * FROM workouts ORDER BY startTime DESC")
    fun getAllWorkoutsFlow(): Flow<List<Workout>>
    
    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutWithSets(id: Long): WorkoutWithSets?
    
    @Transaction
    @Query("SELECT * FROM workouts WHERE id = :id")
    fun getWorkoutWithSetsFlow(id: Long): Flow<WorkoutWithSets?>
    
    @Transaction
    @Query("SELECT * FROM workouts WHERE isActive = 0 ORDER BY startTime DESC")
    fun getCompletedWorkoutsWithSetsFlow(): Flow<List<WorkoutWithSets>>
    
    @Query("DELETE FROM workouts WHERE id = :id")
    suspend fun deleteWorkout(id: Long)
    
    // Get all completed workout start times (for calendar)
    @Query("SELECT startTime FROM workouts WHERE isActive = 0")
    suspend fun getAllWorkoutDates(): List<Long>
    
    // Get workouts in a date range (for a specific month)
    @Query("SELECT * FROM workouts WHERE isActive = 0 AND startTime >= :startTime AND startTime < :endTime ORDER BY startTime DESC")
    suspend fun getWorkoutsInRange(startTime: Long, endTime: Long): List<Workout>
}

