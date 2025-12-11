package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeightReferenceUiState(
    val selectedExercise: Exercise? = null,
    val targetReps: Int = 8,
    val weightsBySet: Map<Int, Double> = emptyMap(),
    val prBySet: Map<Int, Double?> = emptyMap(), // Set-specific PRs for sets 1-10
    val overallPr: Double? = null, // Overall PR for this exercise/rep range
    val isLoading: Boolean = false
)

class WeightReferenceViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeightReferenceUiState())
    val uiState: StateFlow<WeightReferenceUiState> = _uiState.asStateFlow()
    
    fun selectExercise(exercise: Exercise) {
        _uiState.value = _uiState.value.copy(
            selectedExercise = exercise,
            isLoading = true
        )
        loadWeightsForExercise(exercise)
    }
    
    fun updateTargetReps(target: Int) {
        _uiState.value = _uiState.value.copy(targetReps = target)
        _uiState.value.selectedExercise?.let { exercise ->
            loadWeightsForExercise(exercise)
        }
    }
    
    private fun loadWeightsForExercise(exercise: Exercise) {
        viewModelScope.launch {
            val targetReps = _uiState.value.targetReps
            val weights = mutableMapOf<Int, Double>()
            val setPrs = mutableMapOf<Int, Double?>()
            
            // Calculate recommended weight for sets 1-10 and fetch set PRs
            for (setNumber in 1..10) {
                val recommendedWeight = repository.getRecommendedWeight(
                    exerciseId = exercise.id,
                    overallSetNumber = setNumber,
                    targetReps = targetReps
                )
                // Round to nearest 5
                val roundedWeight = (Math.round(recommendedWeight / 5.0) * 5).toDouble()
                weights[setNumber] = roundedWeight
                
                // Fetch set-specific PR
                val setPr = repository.getBestWeightAtSet(
                    exerciseId = exercise.id,
                    overallSetNumber = setNumber,
                    isAssisted = exercise.isAssisted
                )
                setPrs[setNumber] = setPr
            }
            
            // Fetch overall PR for this exercise
            val overallPr = repository.getBestWeight(
                exerciseId = exercise.id,
                isAssisted = exercise.isAssisted
            )
            
            _uiState.value = _uiState.value.copy(
                weightsBySet = weights,
                prBySet = setPrs,
                overallPr = overallPr,
                isLoading = false
            )
        }
    }
    
    class Factory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeightReferenceViewModel::class.java)) {
                return WeightReferenceViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

