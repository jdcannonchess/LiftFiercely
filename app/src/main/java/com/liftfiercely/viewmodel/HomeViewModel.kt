package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutWithSets
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val activeWorkout: Workout? = null,
    val recentWorkouts: List<WorkoutWithSets> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val repository: WorkoutRepository
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
            val workoutId = repository.startWorkout()
            onWorkoutStarted(workoutId)
        }
    }
    
    fun deleteWorkout(workoutId: Long) {
        viewModelScope.launch {
            repository.deleteWorkout(workoutId)
        }
    }
    
    class Factory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


