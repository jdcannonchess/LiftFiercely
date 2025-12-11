package com.liftfiercely.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.liftfiercely.data.model.WorkoutSet
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSetDao {
    
    @Insert
    suspend fun insertSet(workoutSet: WorkoutSet): Long
    
    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId ORDER BY timestamp ASC")
    fun getSetsForWorkoutFlow(workoutId: Long): Flow<List<WorkoutSet>>
    
    @Query("SELECT * FROM workout_sets WHERE workoutId = :workoutId ORDER BY timestamp ASC")
    suspend fun getSetsForWorkout(workoutId: Long): List<WorkoutSet>
    
    @Query("SELECT COUNT(*) FROM workout_sets WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    suspend fun getSetCountForExercise(workoutId: Long, exerciseId: String): Int
    
    @Query("SELECT COUNT(*) FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun getTotalSetCount(workoutId: Long): Int
    
    @Query("DELETE FROM workout_sets WHERE id = :id")
    suspend fun deleteSet(id: Long)
    
    @Query("DELETE FROM workout_sets WHERE workoutId = :workoutId")
    suspend fun deleteSetsForWorkout(workoutId: Long)
    
    // Find exact match: same exercise, same overall set number, same target reps
    // Only from completed workouts (joined with workouts table where isActive = 0)
    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.overallSetNumber = :overallSetNumber 
        AND ws.targetReps = :targetReps
        AND w.isActive = 0
        ORDER BY w.startTime DESC
        LIMIT 1
    """)
    suspend fun findExactHistoricalMatch(
        exerciseId: String,
        overallSetNumber: Int,
        targetReps: Int
    ): WorkoutSet?
    
    // Find all historical sets for this exercise with same target reps
    // Used to find closest set number match
    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.targetReps = :targetReps
        AND w.isActive = 0
        ORDER BY w.startTime DESC
    """)
    suspend fun findHistoricalSetsForExercise(
        exerciseId: String,
        targetReps: Int
    ): List<WorkoutSet>
    
    // Find the best weight for an exercise where target reps were met
    // For regular exercises: highest weight where reps >= targetReps
    // Returns null if no qualifying sets exist
    @Query("""
        SELECT MAX(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
    """)
    suspend fun getBestWeightForExercise(exerciseId: String): Double?
    
    // Find the best (lowest) weight for assisted exercises where target reps were met
    @Query("""
        SELECT MIN(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
    """)
    suspend fun getBestAssistedWeightForExercise(exerciseId: String): Double?
    
    // Find the best weight for an exercise at a specific set number where target reps were met
    // For regular exercises: highest weight
    @Query("""
        SELECT MAX(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.overallSetNumber = :overallSetNumber
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
    """)
    suspend fun getBestWeightForExerciseAtSet(exerciseId: String, overallSetNumber: Int): Double?
    
    // Find the best (lowest) weight for assisted exercises at a specific set number
    @Query("""
        SELECT MIN(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.overallSetNumber = :overallSetNumber
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
    """)
    suspend fun getBestAssistedWeightForExerciseAtSet(exerciseId: String, overallSetNumber: Int): Double?
    
    // Get all sets where a PR was achieved in a date range
    // A PR is defined as meeting target reps with the highest weight ever for that exercise
    // This returns sets where the weight was the best at the time of completion
    @Query("""
        SELECT ws.* FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE w.isActive = 0
        AND w.startTime >= :startTime 
        AND w.startTime < :endTime
        AND ws.reps >= ws.targetReps
        ORDER BY w.startTime DESC
    """)
    suspend fun getSuccessfulSetsInRange(startTime: Long, endTime: Long): List<WorkoutSet>
    
    // Get the best weight ever for an exercise up to a certain time
    @Query("""
        SELECT MAX(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
        AND ws.timestamp < :beforeTime
    """)
    suspend fun getBestWeightForExerciseBefore(exerciseId: String, beforeTime: Long): Double?
    
    // Get the best (lowest) assisted weight ever for an exercise up to a certain time
    @Query("""
        SELECT MIN(ws.weight) FROM workout_sets ws
        INNER JOIN workouts w ON ws.workoutId = w.id
        WHERE ws.exerciseId = :exerciseId 
        AND ws.reps >= ws.targetReps
        AND w.isActive = 0
        AND ws.timestamp < :beforeTime
    """)
    suspend fun getBestAssistedWeightForExerciseBefore(exerciseId: String, beforeTime: Long): Double?
}
