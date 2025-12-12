package com.liftfiercely.data.repository

import com.liftfiercely.data.database.WorkoutDao
import com.liftfiercely.data.database.WorkoutSetDao
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.data.model.WorkoutWithSets
import kotlinx.coroutines.flow.Flow
import kotlin.math.abs

class WorkoutRepository(
    private val workoutDao: WorkoutDao,
    private val workoutSetDao: WorkoutSetDao
) {
    
    // Workout operations
    suspend fun startWorkout(bodyWeight: Double = 0.0): Long {
        val workout = Workout(bodyWeight = bodyWeight)
        return workoutDao.insertWorkout(workout)
    }
    
    suspend fun endWorkout(workoutId: Long) {
        val workout = workoutDao.getWorkoutById(workoutId)
        workout?.let {
            workoutDao.updateWorkout(
                it.copy(
                    endTime = System.currentTimeMillis(),
                    isActive = false
                )
            )
        }
    }
    
    suspend fun getActiveWorkout(): Workout? {
        return workoutDao.getActiveWorkout()
    }
    
    fun getActiveWorkoutFlow(): Flow<Workout?> {
        return workoutDao.getActiveWorkoutFlow()
    }
    
    fun getCompletedWorkoutsFlow(): Flow<List<Workout>> {
        return workoutDao.getCompletedWorkoutsFlow()
    }
    
    fun getCompletedWorkoutsWithSetsFlow(): Flow<List<WorkoutWithSets>> {
        return workoutDao.getCompletedWorkoutsWithSetsFlow()
    }
    
    suspend fun getWorkoutWithSets(workoutId: Long): WorkoutWithSets? {
        return workoutDao.getWorkoutWithSets(workoutId)
    }
    
    fun getWorkoutWithSetsFlow(workoutId: Long): Flow<WorkoutWithSets?> {
        return workoutDao.getWorkoutWithSetsFlow(workoutId)
    }
    
    suspend fun deleteWorkout(workoutId: Long) {
        workoutDao.deleteWorkout(workoutId)
    }
    
    suspend fun updateWorkoutDate(workoutId: Long, newStartTime: Long, newEndTime: Long?) {
        workoutDao.updateWorkoutDate(workoutId, newStartTime, newEndTime)
    }
    
    // Set operations
    suspend fun addSet(
        workoutId: Long,
        exerciseId: String,
        reps: Int,
        weight: Double,
        targetReps: Int
    ): Long {
        val exerciseSetNumber = workoutSetDao.getSetCountForExercise(workoutId, exerciseId) + 1
        val overallSetNumber = workoutSetDao.getTotalSetCount(workoutId) + 1
        
        val workoutSet = WorkoutSet(
            workoutId = workoutId,
            exerciseId = exerciseId,
            reps = reps,
            weight = weight,
            targetReps = targetReps,
            overallSetNumber = overallSetNumber,
            setNumber = exerciseSetNumber
        )
        return workoutSetDao.insertSet(workoutSet)
    }
    
    suspend fun getNextOverallSetNumber(workoutId: Long): Int {
        return workoutSetDao.getTotalSetCount(workoutId) + 1
    }
    
    fun getSetsForWorkoutFlow(workoutId: Long): Flow<List<WorkoutSet>> {
        return workoutSetDao.getSetsForWorkoutFlow(workoutId)
    }
    
    suspend fun getSetsForWorkout(workoutId: Long): List<WorkoutSet> {
        return workoutSetDao.getSetsForWorkout(workoutId)
    }
    
    suspend fun deleteSet(setId: Long) {
        workoutSetDao.deleteSet(setId)
    }
    
    // Weight recommendation
    suspend fun getRecommendedWeight(
        exerciseId: String,
        overallSetNumber: Int,
        targetReps: Int
    ): Double {
        val exercise = Exercise.getById(exerciseId) ?: return 0.0
        val isAssisted = exercise.isAssisted
        
        // Step 1: Try to find exact match
        val exactMatch = workoutSetDao.findExactHistoricalMatch(
            exerciseId = exerciseId,
            overallSetNumber = overallSetNumber,
            targetReps = targetReps
        )
        
        if (exactMatch != null) {
            return calculateWeightFromPerformance(
                previousWeight = exactMatch.weight,
                previousReps = exactMatch.reps,
                targetReps = targetReps,
                isAssisted = isAssisted
            )
        }
        
        // Step 2: Find closest set number match
        val historicalSets = workoutSetDao.findHistoricalSetsForExercise(
            exerciseId = exerciseId,
            targetReps = targetReps
        )
        
        if (historicalSets.isNotEmpty()) {
            // Find the closest set number
            val closestSet = historicalSets.minByOrNull { 
                abs(it.overallSetNumber - overallSetNumber) 
            }
            
            if (closestSet != null) {
                // Calculate base weight from performance
                val baseWeight = calculateWeightFromPerformance(
                    previousWeight = closestSet.weight,
                    previousReps = closestSet.reps,
                    targetReps = targetReps,
                    isAssisted = isAssisted
                )
                
                // Adjust for set number difference
                val setDifference = overallSetNumber - closestSet.overallSetNumber
                val adjustment = (abs(setDifference) / 2.0) * 5.0
                
                return if (setDifference > 0) {
                    // Current set is later (more fatigue) - reduce weight
                    if (isAssisted) baseWeight + adjustment else baseWeight - adjustment
                } else {
                    // Current set is earlier (less fatigue) - increase weight
                    if (isAssisted) baseWeight - adjustment else baseWeight + adjustment
                }
            }
        }
        
        // Step 3: No history - use default weight adjusted for set number
        val defaultWeight = exercise.defaultWeight
        // Adjust from set 1 default: reduce 2.5 lbs per set (or increase for assisted)
        val setAdjustment = ((overallSetNumber - 1) / 2.0) * 5.0
        
        return if (isAssisted) {
            defaultWeight + setAdjustment
        } else {
            (defaultWeight - setAdjustment).coerceAtLeast(0.0)
        }
    }
    
    private fun calculateWeightFromPerformance(
        previousWeight: Double,
        previousReps: Int,
        targetReps: Int,
        isAssisted: Boolean
    ): Double {
        return when {
            // Met or exceeded target - progress!
            previousReps >= targetReps -> {
                if (isAssisted) previousWeight - 5.0 else previousWeight + 5.0
            }
            // Got target - 1 reps - keep same weight
            previousReps == targetReps - 1 -> {
                previousWeight
            }
            // Got less than target - 1 - regress
            else -> {
                val repsUnder = (targetReps - 1) - previousReps
                val adjustment = repsUnder * 5.0
                if (isAssisted) previousWeight + adjustment else previousWeight - adjustment
            }
        }
    }
    
    // Get the best weight for an exercise (highest for regular, lowest for assisted)
    suspend fun getBestWeight(exerciseId: String, isAssisted: Boolean): Double? {
        return if (isAssisted) {
            workoutSetDao.getBestAssistedWeightForExercise(exerciseId)
        } else {
            workoutSetDao.getBestWeightForExercise(exerciseId)
        }
    }
    
    // Get the best weight for an exercise at a specific set number
    suspend fun getBestWeightAtSet(exerciseId: String, overallSetNumber: Int, isAssisted: Boolean): Double? {
        return if (isAssisted) {
            workoutSetDao.getBestAssistedWeightForExerciseAtSet(exerciseId, overallSetNumber)
        } else {
            workoutSetDao.getBestWeightForExerciseAtSet(exerciseId, overallSetNumber)
        }
    }
    
    // Calendar features
    suspend fun getAllWorkoutDates(): List<Long> {
        return workoutDao.getAllWorkoutDates()
    }
    
    suspend fun getWorkoutsInRange(startTime: Long, endTime: Long): List<Workout> {
        return workoutDao.getWorkoutsInRange(startTime, endTime)
    }
    
    suspend fun getWorkoutsWithSetsInRange(startTime: Long, endTime: Long): List<WorkoutWithSets> {
        return workoutDao.getWorkoutsWithSetsInRange(startTime, endTime)
    }
    
    // Calculate workout streak (resets only if 3 consecutive days are missed)
    suspend fun calculateStreak(): Int {
        val workouts = workoutDao.getAllCompletedWorkouts()
        if (workouts.isEmpty()) return 0
        
        // Get unique workout dates (day precision)
        val workoutDates = workouts.map { workout ->
            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = workout.startTime
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
            cal.set(java.util.Calendar.MINUTE, 0)
            cal.set(java.util.Calendar.SECOND, 0)
            cal.set(java.util.Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }.toSet().sortedDescending()
        
        if (workoutDates.isEmpty()) return 0
        
        val today = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val oneDayMs = 24 * 60 * 60 * 1000L
        
        // Check if most recent workout is within 3 days of today
        val mostRecentWorkout = workoutDates.first()
        val daysSinceLast = ((today - mostRecentWorkout) / oneDayMs).toInt()
        if (daysSinceLast > 3) return 0
        
        // Count streak: streak continues as long as gaps are <= 2 days
        var streak = 1
        var previousDate = workoutDates.first()
        
        for (i in 1 until workoutDates.size) {
            val currentDate = workoutDates[i]
            val dayGap = ((previousDate - currentDate) / oneDayMs).toInt()
            
            if (dayGap <= 3) {
                // Gap of 3 days or less (allowing 2 rest days) - continue streak
                streak++
                previousDate = currentDate
            } else {
                // Gap too large, streak broken
                break
            }
        }
        
        return streak
    }
    
    // Get PRs achieved in a date range
    // Returns a list of (WorkoutSet, exerciseName) for sets that were PRs when achieved
    suspend fun getPRsInRange(startTime: Long, endTime: Long): List<PRRecord> {
        val successfulSets = workoutSetDao.getSuccessfulSetsInRange(startTime, endTime)
        val prRecords = mutableListOf<PRRecord>()
        
        for (set in successfulSets) {
            val exercise = Exercise.getById(set.exerciseId) ?: continue
            val isAssisted = exercise.isAssisted
            
            // Get the best weight before this set was logged
            val previousBest = if (isAssisted) {
                workoutSetDao.getBestAssistedWeightForExerciseBefore(set.exerciseId, set.timestamp)
            } else {
                workoutSetDao.getBestWeightForExerciseBefore(set.exerciseId, set.timestamp)
            }
            
            // Check if this set is a PR
            val isPR = if (isAssisted) {
                // For assisted exercises, lower weight is better
                previousBest == null || set.weight < previousBest
            } else {
                // For regular exercises, higher weight is better
                previousBest == null || set.weight > previousBest
            }
            
            if (isPR) {
                prRecords.add(PRRecord(
                    set = set,
                    exerciseName = exercise.name,
                    weight = set.weight,
                    reps = set.reps,
                    isAssisted = isAssisted,
                    timestamp = set.timestamp
                ))
            }
        }
        
        return prRecords
    }
}

data class PRRecord(
    val set: WorkoutSet,
    val exerciseName: String,
    val weight: Double,
    val reps: Int,
    val isAssisted: Boolean,
    val timestamp: Long
)
