package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.ExerciseCategory
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ActiveWorkoutUiState(
    val workout: Workout? = null,
    val sets: List<WorkoutSet> = emptyList(),
    val selectedExercise: Exercise? = null,
    val selectedCategory: ExerciseCategory = ExerciseCategory.PUSH,
    val targetReps: Int = 8,
    val reps: String = "",
    val weight: String = "",
    val recommendedWeight: Double = 0.0,
    val nextOverallSetNumber: Int = 1,
    val isLoading: Boolean = true,
    val workoutEnded: Boolean = false,
    // Rest timer state
    val isTimerRunning: Boolean = false,
    val isTimerPaused: Boolean = false,
    val timerSecondsRemaining: Int = 90,
    val timerDuration: Int = 90,
    val timerFinished: Boolean = false
)

class ActiveWorkoutViewModel(
    private val repository: WorkoutRepository,
    private val workoutId: Long
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    val uiState: StateFlow<ActiveWorkoutUiState> = _uiState.asStateFlow()
    
    private var timerJob: Job? = null
    
    init {
        loadWorkout()
    }
    
    private fun loadWorkout() {
        viewModelScope.launch {
            repository.getWorkoutWithSetsFlow(workoutId).collect { workoutWithSets ->
                workoutWithSets?.let {
                    val nextSetNumber = it.sets.size + 1
                    _uiState.value = _uiState.value.copy(
                        workout = it.workout,
                        sets = it.sets,
                        nextOverallSetNumber = nextSetNumber,
                        isLoading = false
                    )
                    // Refresh recommended weight if exercise is selected
                    _uiState.value.selectedExercise?.let { exercise ->
                        updateRecommendedWeight(exercise)
                    }
                }
            }
        }
    }
    
    fun selectCategory(category: ExerciseCategory) {
        // Auto-select a default exercise for each category
        val defaultExerciseId = when (category) {
            ExerciseCategory.PUSH -> "flat_press"
            ExerciseCategory.PULL -> "assisted_pull_up"
            ExerciseCategory.OTHER -> "barbell_curl"
        }
        val defaultExercise = Exercise.getById(defaultExerciseId)
        
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            selectedExercise = defaultExercise,
            reps = _uiState.value.targetReps.toString(),
            weight = "",
            recommendedWeight = 0.0
        )
        
        // Fetch recommended weight for the default exercise
        defaultExercise?.let { updateRecommendedWeight(it) }
    }
    
    fun selectExercise(exercise: Exercise) {
        val currentTarget = _uiState.value.targetReps
        _uiState.value = _uiState.value.copy(
            selectedExercise = exercise,
            reps = currentTarget.toString()
        )
        // Fetch recommended weight
        updateRecommendedWeight(exercise)
    }
    
    private fun updateRecommendedWeight(exercise: Exercise) {
        viewModelScope.launch {
            val nextSetNumber = _uiState.value.nextOverallSetNumber
            val targetReps = _uiState.value.targetReps
            
            val recommendedWeight = repository.getRecommendedWeight(
                exerciseId = exercise.id,
                overallSetNumber = nextSetNumber,
                targetReps = targetReps
            )
            
            // Round to nearest 5
            val roundedWeight = (Math.round(recommendedWeight / 5.0) * 5).toDouble()
            
            _uiState.value = _uiState.value.copy(
                recommendedWeight = roundedWeight,
                weight = if (roundedWeight > 0) roundedWeight.toInt().toString() else ""
            )
        }
    }
    
    fun updateTargetReps(target: Int) {
        _uiState.value = _uiState.value.copy(
            targetReps = target,
            reps = target.toString()
        )
        // Refresh recommended weight with new target
        _uiState.value.selectedExercise?.let { exercise ->
            updateRecommendedWeight(exercise)
        }
    }
    
    fun updateReps(reps: String) {
        _uiState.value = _uiState.value.copy(reps = reps)
    }
    
    fun updateWeight(weight: String) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }
    
    fun logSet() {
        val state = _uiState.value
        val exercise = state.selectedExercise ?: return
        val reps = state.reps.toIntOrNull() ?: return
        val weight = state.weight.toDoubleOrNull() ?: return
        val targetReps = state.targetReps
        
        viewModelScope.launch {
            repository.addSet(
                workoutId = workoutId,
                exerciseId = exercise.id,
                reps = reps,
                weight = weight,
                targetReps = targetReps
            )
            _uiState.value = _uiState.value.copy(
                reps = "",
                weight = ""
            )
            // Auto-start rest timer after logging set
            startTimer()
        }
    }
    
    // Rest Timer Functions
    fun startTimer() {
        timerJob?.cancel()
        val duration = _uiState.value.timerDuration
        _uiState.value = _uiState.value.copy(
            isTimerRunning = true,
            isTimerPaused = false,
            timerSecondsRemaining = duration,
            timerFinished = false
        )
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSecondsRemaining > 0) {
                delay(1000)
                if (!_uiState.value.isTimerPaused) {
                    _uiState.value = _uiState.value.copy(
                        timerSecondsRemaining = _uiState.value.timerSecondsRemaining - 1
                    )
                }
            }
            // Timer finished
            _uiState.value = _uiState.value.copy(
                isTimerRunning = false,
                timerFinished = true
            )
        }
    }
    
    fun pauseTimer() {
        _uiState.value = _uiState.value.copy(isTimerPaused = true)
    }
    
    fun resumeTimer() {
        _uiState.value = _uiState.value.copy(isTimerPaused = false)
    }
    
    fun skipTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isTimerRunning = false,
            isTimerPaused = false,
            timerSecondsRemaining = _uiState.value.timerDuration,
            timerFinished = false
        )
    }
    
    fun adjustTimer(seconds: Int) {
        val newRemaining = (_uiState.value.timerSecondsRemaining + seconds).coerceAtLeast(0)
        val newDuration = (_uiState.value.timerDuration + seconds).coerceAtLeast(15)
        _uiState.value = _uiState.value.copy(
            timerSecondsRemaining = newRemaining,
            timerDuration = newDuration
        )
    }
    
    fun clearTimerFinished() {
        _uiState.value = _uiState.value.copy(timerFinished = false)
    }
    
    fun deleteSet(setId: Long) {
        viewModelScope.launch {
            repository.deleteSet(setId)
        }
    }
    
    fun endWorkout() {
        viewModelScope.launch {
            repository.endWorkout(workoutId)
            _uiState.value = _uiState.value.copy(workoutEnded = true)
        }
    }
    
    class Factory(
        private val repository: WorkoutRepository,
        private val workoutId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActiveWorkoutViewModel::class.java)) {
                return ActiveWorkoutViewModel(repository, workoutId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
