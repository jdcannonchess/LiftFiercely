package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WorkoutDetailUiState(
    val workout: Workout? = null,
    val sets: List<WorkoutSet> = emptyList(),
    val totalWeightLifted: Double = 0.0,
    val overallPrSetIds: Set<Long> = emptySet(),      // Overall personal records (best ever)
    val setSpecificPrSetIds: Set<Long> = emptySet(),  // Set-specific personal records
    val isLoading: Boolean = true
) {
    // Count of unique PRs (a set could be both, count it once)
    val totalPrCount: Int
        get() = (overallPrSetIds + setSpecificPrSetIds).size
}

class WorkoutDetailViewModel(
    private val repository: WorkoutRepository,
    private val workoutId: Long
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WorkoutDetailUiState())
    val uiState: StateFlow<WorkoutDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadWorkout()
    }
    
    private fun loadWorkout() {
        viewModelScope.launch {
            repository.getWorkoutWithSetsFlow(workoutId).collect { workoutWithSets ->
                val sets = workoutWithSets?.sets ?: emptyList()
                
                // Calculate total weight lifted (reps × weight for each set)
                val totalWeight = sets.sumOf { it.reps * it.weight }
                
                // Find both types of PRs
                val (overallPrs, setSpecificPrs) = findPersonalRecords(sets)
                
                _uiState.value = _uiState.value.copy(
                    workout = workoutWithSets?.workout,
                    sets = sets,
                    totalWeightLifted = totalWeight,
                    overallPrSetIds = overallPrs,
                    setSpecificPrSetIds = setSpecificPrs,
                    isLoading = false
                )
            }
        }
    }
    
    private suspend fun findPersonalRecords(sets: List<WorkoutSet>): Pair<Set<Long>, Set<Long>> {
        val overallPrIds = mutableSetOf<Long>()
        val setSpecificPrIds = mutableSetOf<Long>()
        
        // Process each set individually
        for (set in sets) {
            // Only consider sets where target reps were met
            if (set.reps < set.targetReps) continue
            
            val exercise = Exercise.getById(set.exerciseId) ?: continue
            val isAssisted = exercise.isAssisted
            
            // Check for overall PR (best ever for this exercise)
            val overallBest = repository.getBestWeight(set.exerciseId, isAssisted)
            val isOverallPr = checkIfPr(set.weight, overallBest, isAssisted)
            
            if (isOverallPr) {
                overallPrIds.add(set.id)
            }
            
            // Check for set-specific PR (best for this exercise at this set number)
            val setSpecificBest = repository.getBestWeightAtSet(set.exerciseId, set.overallSetNumber, isAssisted)
            val isSetSpecificPr = checkIfPr(set.weight, setSpecificBest, isAssisted)
            
            // Only count as set-specific PR if it's NOT also an overall PR
            // (to avoid double-badging the same achievement)
            if (isSetSpecificPr && !isOverallPr) {
                setSpecificPrIds.add(set.id)
            }
        }
        
        return Pair(overallPrIds, setSpecificPrIds)
    }
    
    private fun checkIfPr(currentWeight: Double, historicalBest: Double?, isAssisted: Boolean): Boolean {
        return if (historicalBest == null) {
            // No history, this is automatically a PR
            true
        } else if (isAssisted) {
            // For assisted: lower weight is better (harder)
            currentWeight <= historicalBest
        } else {
            // For regular: higher weight is better
            currentWeight >= historicalBest
        }
    }
    
    class Factory(
        private val repository: WorkoutRepository,
        private val workoutId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutDetailViewModel::class.java)) {
                return WorkoutDetailViewModel(repository, workoutId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
