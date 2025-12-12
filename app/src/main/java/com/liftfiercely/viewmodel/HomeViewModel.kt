package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.SettingsDataStore
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.ExerciseCategory
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.data.model.WorkoutWithSets
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class HomeUiState(
    val activeWorkout: Workout? = null,
    val recentWorkouts: List<WorkoutWithSets> = emptyList(),
    val isLoading: Boolean = true
)

data class ShareContent(
    val text: String
)

data class WorkoutPR(
    val exerciseName: String,
    val weight: Double,
    val reps: Int,
    val isAssisted: Boolean
)

class HomeViewModel(
    private val repository: WorkoutRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadData()
    }
    
    private fun loadData() {
        viewModelScope.launch {
            repository.getActiveWorkoutFlow().collect { activeWorkout ->
                _uiState.value = _uiState.value.copy(activeWorkout = activeWorkout)
            }
        }
        
        viewModelScope.launch {
            repository.getCompletedWorkoutsWithSetsFlow().collect { workouts ->
                _uiState.value = _uiState.value.copy(
                    recentWorkouts = workouts,
                    isLoading = false
                )
            }
        }
    }
    
    fun startWorkout(onWorkoutStarted: (Long) -> Unit) {
        viewModelScope.launch {
            // Get current body weight from settings to store with workout
            val bodyWeight = settingsDataStore.getBodyWeightOnce()
            val workoutId = repository.startWorkout(bodyWeight)
            onWorkoutStarted(workoutId)
        }
    }
    
    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            repository.deleteWorkout(workoutId)
        }
    }
    
    /**
     * Generates share content for a workout.
     * Format (Option C - Minimal but Fun):
     * 💪 Pull Day Done!
     * 12/11/25
     * 
     * 📊 19,930 lbs lifted
     * 
     * ✨ New PR: Seated Row — 265lbs for 8
     * 
     * #LiftFiercely
     */
    suspend fun generateShareContent(workoutWithSets: WorkoutWithSets): ShareContent {
        val workout = workoutWithSets.workout
        val sets = workoutWithSets.sets
        
        // Format date as MM/dd/yy
        val dateFormat = SimpleDateFormat("M/d/yy", Locale.getDefault())
        val dateStr = dateFormat.format(Date(workout.startTime))
        
        // Infer focus from exercises
        val focus = inferWorkoutFocus(sets)
        val focusLabel = when (focus) {
            "Push Focused" -> "Push Day"
            "Pull Focused" -> "Pull Day"
            "Arms/Shoulders Focused" -> "Arms & Shoulders"
            else -> "Workout"
        }
        
        // Use stored body weight if available, otherwise fallback to settings
        val bodyWeight = if (workout.bodyWeight > 0) {
            workout.bodyWeight
        } else {
            settingsDataStore.getBodyWeightOnce()
        }
        val totalPounds = calculateTotalPounds(sets, bodyWeight)
        val formattedPounds = NumberFormat.getNumberInstance(Locale.US).format(totalPounds.toLong())
        
        // Find PRs for this workout
        val prs = findWorkoutPRs(sets)
        
        // Build share text - Option C style (Minimal but Fun)
        val builder = StringBuilder()
        builder.append("💪 $focusLabel Done!\n")
        builder.append("$dateStr\n")
        builder.append("\n")
        builder.append("📊 $formattedPounds lbs lifted")
        
        if (prs.isNotEmpty()) {
            builder.append("\n")
            for (pr in prs) {
                val weightStr = if (pr.weight % 1.0 == 0.0) {
                    pr.weight.toInt().toString()
                } else {
                    pr.weight.toString()
                }
                builder.append("\n✨ New PR: ${pr.exerciseName} — ${weightStr}lbs for ${pr.reps}")
            }
        }
        
        builder.append("\n\n#LiftFiercely")
        
        return ShareContent(text = builder.toString())
    }
    
    private fun inferWorkoutFocus(sets: List<WorkoutSet>): String {
        if (sets.isEmpty()) return "Workout"
        
        // Count exercises by category
        val categoryCounts = mutableMapOf<ExerciseCategory, Int>()
        for (set in sets) {
            val exercise = Exercise.getById(set.exerciseId) ?: continue
            categoryCounts[exercise.category] = (categoryCounts[exercise.category] ?: 0) + 1
        }
        
        if (categoryCounts.isEmpty()) return "Workout"
        
        // Find the most common category
        val dominantCategory = categoryCounts.maxByOrNull { it.value }?.key ?: return "Workout"
        
        return when (dominantCategory) {
            ExerciseCategory.PUSH -> "Push Focused"
            ExerciseCategory.PULL -> "Pull Focused"
            ExerciseCategory.OTHER -> "Arms/Shoulders Focused"
        }
    }
    
    private fun calculateTotalPounds(sets: List<WorkoutSet>, bodyWeight: Double): Double {
        return sets.sumOf { set ->
            val exercise = Exercise.getById(set.exerciseId)
            val actualWeight = if (exercise?.isAssisted == true && bodyWeight > 0) {
                // For assisted exercises: body weight minus assistance = actual weight lifted
                (bodyWeight - set.weight).coerceAtLeast(0.0)
            } else {
                set.weight
            }
            set.reps * actualWeight
        }
    }
    
    private suspend fun findWorkoutPRs(sets: List<WorkoutSet>): List<WorkoutPR> {
        val prs = mutableListOf<WorkoutPR>()
        
        for (set in sets) {
            // Only consider sets where target reps were met
            if (set.reps < set.targetReps) continue
            
            val exercise = Exercise.getById(set.exerciseId) ?: continue
            val isAssisted = exercise.isAssisted
            
            // Get the best weight before this set was logged
            val previousBest = repository.getBestWeight(set.exerciseId, isAssisted)
            
            // Check if this set is a PR (overall best)
            val isPR = if (previousBest == null) {
                true // First time doing this exercise
            } else if (isAssisted) {
                // For assisted exercises, lower weight is better (less assistance)
                set.weight <= previousBest
            } else {
                // For regular exercises, higher weight is better
                set.weight >= previousBest
            }
            
            if (isPR) {
                // Check if we already have a PR for this exercise (keep the best one)
                val existingPrIndex = prs.indexOfFirst { it.exerciseName == exercise.name }
                if (existingPrIndex >= 0) {
                    val existing = prs[existingPrIndex]
                    // Keep the better one
                    val currentIsBetter = if (isAssisted) {
                        set.weight < existing.weight
                    } else {
                        set.weight > existing.weight
                    }
                    if (currentIsBetter) {
                        prs[existingPrIndex] = WorkoutPR(
                            exerciseName = exercise.name,
                            weight = set.weight,
                            reps = set.reps,
                            isAssisted = isAssisted
                        )
                    }
                } else {
                    prs.add(WorkoutPR(
                        exerciseName = exercise.name,
                        weight = set.weight,
                        reps = set.reps,
                        isAssisted = isAssisted
                    ))
                }
            }
        }
        
        return prs
    }
    
    class Factory(
        private val repository: WorkoutRepository,
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository, settingsDataStore) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


