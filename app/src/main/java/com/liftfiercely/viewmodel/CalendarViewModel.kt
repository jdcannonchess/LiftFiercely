package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.SettingsDataStore
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.repository.PRRecord
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class DayStats(
    val totalSets: Int,
    val totalPounds: Int
)

data class CalendarUiState(
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    val workoutDays: Set<Int> = emptySet(), // Days in the current month with workouts
    val dayStats: Map<Int, DayStats> = emptyMap(), // Stats per day
    val currentStreak: Int = 0,
    val prsThisMonth: List<PRRecord> = emptyList(),
    val isLoading: Boolean = true
)

class CalendarViewModel(
    private val repository: WorkoutRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    
    init {
        loadDataForCurrentMonth()
    }
    
    private fun loadDataForCurrentMonth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Get current body weight from settings as fallback for old workouts
            val settingsBodyWeight = settingsDataStore.getBodyWeightOnce()
            
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, _uiState.value.currentYear)
            calendar.set(Calendar.MONTH, _uiState.value.currentMonth)
            
            // Get start of month
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfMonth = calendar.timeInMillis
            
            // Get start of next month
            calendar.add(Calendar.MONTH, 1)
            val endOfMonth = calendar.timeInMillis
            
            // Get workouts with sets in this month
            val workoutsWithSets = repository.getWorkoutsWithSetsInRange(startOfMonth, endOfMonth)
            
            // Extract which days have workouts and calculate daily stats
            val workoutDays = mutableSetOf<Int>()
            val dayStats = mutableMapOf<Int, DayStats>()
            
            for (workoutWithSets in workoutsWithSets) {
                val workoutCal = Calendar.getInstance()
                workoutCal.timeInMillis = workoutWithSets.workout.startTime
                val day = workoutCal.get(Calendar.DAY_OF_MONTH)
                
                workoutDays.add(day)
                
                // Use stored body weight if available, otherwise fallback to settings
                val bodyWeight = if (workoutWithSets.workout.bodyWeight > 0) {
                    workoutWithSets.workout.bodyWeight
                } else {
                    settingsBodyWeight
                }
                
                // Calculate stats for this day with true weight for assisted exercises
                val sets = workoutWithSets.sets
                val totalSets = sets.size
                val totalPounds = sets.sumOf { set ->
                    val exercise = Exercise.getById(set.exerciseId)
                    val actualWeight = if (exercise?.isAssisted == true && bodyWeight > 0) {
                        // For assisted exercises: body weight minus assistance = actual weight lifted
                        (bodyWeight - set.weight).coerceAtLeast(0.0)
                    } else {
                        set.weight
                    }
                    (actualWeight * set.reps).toInt()
                }
                
                // Merge with existing stats for this day (if multiple workouts)
                val existingStats = dayStats[day]
                if (existingStats != null) {
                    dayStats[day] = DayStats(
                        totalSets = existingStats.totalSets + totalSets,
                        totalPounds = existingStats.totalPounds + totalPounds
                    )
                } else {
                    dayStats[day] = DayStats(totalSets = totalSets, totalPounds = totalPounds)
                }
            }
            
            // Get PRs for this month
            val prs = repository.getPRsInRange(startOfMonth, endOfMonth)
            
            // Calculate streak
            val streak = repository.calculateStreak()
            
            _uiState.value = _uiState.value.copy(
                workoutDays = workoutDays,
                dayStats = dayStats,
                currentStreak = streak,
                prsThisMonth = prs,
                isLoading = false
            )
        }
    }
    
    fun previousMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, _uiState.value.currentYear)
        calendar.set(Calendar.MONTH, _uiState.value.currentMonth)
        calendar.add(Calendar.MONTH, -1)
        
        _uiState.value = _uiState.value.copy(
            currentYear = calendar.get(Calendar.YEAR),
            currentMonth = calendar.get(Calendar.MONTH)
        )
        loadDataForCurrentMonth()
    }
    
    fun nextMonth() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, _uiState.value.currentYear)
        calendar.set(Calendar.MONTH, _uiState.value.currentMonth)
        calendar.add(Calendar.MONTH, 1)
        
        _uiState.value = _uiState.value.copy(
            currentYear = calendar.get(Calendar.YEAR),
            currentMonth = calendar.get(Calendar.MONTH)
        )
        loadDataForCurrentMonth()
    }
    
    class Factory(
        private val repository: WorkoutRepository,
        private val settingsDataStore: SettingsDataStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(repository, settingsDataStore) as T
        }
    }
}


