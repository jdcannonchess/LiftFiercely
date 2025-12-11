package com.liftfiercely.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.liftfiercely.data.repository.PRRecord
import com.liftfiercely.data.repository.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class CalendarUiState(
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH),
    val workoutDays: Set<Int> = emptySet(), // Days in the current month with workouts
    val prsThisMonth: List<PRRecord> = emptyList(),
    val isLoading: Boolean = true
)

class CalendarViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()
    
    init {
        loadDataForCurrentMonth()
    }
    
    private fun loadDataForCurrentMonth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
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
            
            // Get workouts in this month
            val workouts = repository.getWorkoutsInRange(startOfMonth, endOfMonth)
            
            // Extract which days have workouts
            val workoutDays = workouts.map { workout ->
                val workoutCal = Calendar.getInstance()
                workoutCal.timeInMillis = workout.startTime
                workoutCal.get(Calendar.DAY_OF_MONTH)
            }.toSet()
            
            // Get PRs for this month
            val prs = repository.getPRsInRange(startOfMonth, endOfMonth)
            
            _uiState.value = _uiState.value.copy(
                workoutDays = workoutDays,
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
    
    class Factory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CalendarViewModel(repository) as T
        }
    }
}


