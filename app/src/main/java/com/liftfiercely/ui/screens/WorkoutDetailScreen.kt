package com.liftfiercely.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import java.text.NumberFormat
import java.util.Locale as JavaLocale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.ui.theme.CoralPrimary
import com.liftfiercely.ui.theme.getCategoryColor
import com.liftfiercely.viewmodel.WorkoutDetailViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    viewModel: WorkoutDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Date picker state - use workout ID as key to recreate when viewing different workout
    // and update selection when date changes
    val workoutStartTime = uiState.workout?.startTime
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = workoutStartTime
    )
    
    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDateUtc ->
                            // The DatePicker returns UTC midnight for the selected date
                            // We need to extract year/month/day and combine with original time
                            val originalTime = uiState.workout?.startTime ?: System.currentTimeMillis()
                            val originalCalendar = Calendar.getInstance().apply {
                                timeInMillis = originalTime
                            }
                            
                            // Parse the selected UTC date to get year/month/day
                            val utcCalendar = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = selectedDateUtc
                            }
                            
                            // Create new date with selected year/month/day but original time
                            val newCalendar = Calendar.getInstance().apply {
                                set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR))
                                set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH))
                                set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH))
                                set(Calendar.HOUR_OF_DAY, originalCalendar.get(Calendar.HOUR_OF_DAY))
                                set(Calendar.MINUTE, originalCalendar.get(Calendar.MINUTE))
                                set(Calendar.SECOND, originalCalendar.get(Calendar.SECOND))
                                set(Calendar.MILLISECOND, 0)
                            }
                            viewModel.updateWorkoutDate(newCalendar.timeInMillis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = CoralPrimary
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    
                    Text(
                        text = "WORKOUT DETAILS",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    // Workout summary card
                    item {
                        uiState.workout?.let { workout ->
                            WorkoutSummaryCard(
                                date = formatFullDate(workout.startTime),
                                time = formatTimeRange(workout.startTime, workout.endTime),
                                duration = workout.getDurationMinutes(),
                                totalSets = uiState.sets.size,
                                totalExercises = uiState.sets.map { it.exerciseId }.distinct().size,
                                totalWeightLifted = uiState.totalWeightLifted,
                                onEditDate = { showDatePicker = true }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    
                    // Sets grouped by exercise
                    val groupedSets = uiState.sets.groupBy { it.exerciseId }
                    
                    groupedSets.forEach { (exerciseId, sets) ->
                        val exercise = Exercise.getById(exerciseId)
                        
                        item(key = "header_$exerciseId") {
                            ExerciseGroupHeader(
                                exerciseName = exercise?.name ?: "Unknown",
                                category = exercise?.category?.name ?: "",
                                categoryColor = exercise?.let { getCategoryColor(it.category) } ?: CoralPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        
                        itemsIndexed(
                            items = sets,
                            key = { _, set -> set.id }
                        ) { index, set ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically(
                                    initialOffsetY = { it / 2 }
                                ) + fadeIn()
                            ) {
                                SetDetailRow(
                                    setNumber = set.overallSetNumber,
                                    reps = set.reps,
                                    weight = set.weight,
                                    targetReps = set.targetReps,
                                    isOverallPr = uiState.overallPrSetIds.contains(set.id),
                                    isSetSpecificPr = uiState.setSpecificPrSetIds.contains(set.id),
                                    categoryColor = exercise?.let { getCategoryColor(it.category) } ?: CoralPrimary
                                )
                            }
                            
                            if (index < sets.lastIndex) {
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                        
                        item(key = "spacer_$exerciseId") {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutSummaryCard(
    date: String,
    time: String,
    duration: Long?,
    totalSets: Int,
    totalExercises: Int,
    totalWeightLifted: Double,
    onEditDate: () -> Unit
) {
    val numberFormat = NumberFormat.getNumberInstance(JavaLocale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CoralPrimary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Fixed space for edit button
                IconButton(
                    onClick = onEditDate,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit date",
                        tint = CoralPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Total weight lifted highlight
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CoralPrimary.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Scale,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Total Weight Lifted",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "${numberFormat.format(totalWeightLifted.toLong())} lbs",
                        style = MaterialTheme.typography.titleLarge,
                        color = CoralPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.FitnessCenter,
                    value = totalSets.toString(),
                    label = "Sets"
                )
                
                StatItem(
                    icon = Icons.Default.FitnessCenter,
                    value = totalExercises.toString(),
                    label = "Exercises"
                )
                
                duration?.let {
                    StatItem(
                        icon = Icons.Default.Schedule,
                        value = "${it}min",
                        label = "Duration"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CoralPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CoralPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ExerciseGroupHeader(
    exerciseName: String,
    category: String,
    categoryColor: androidx.compose.ui.graphics.Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = exerciseName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                color = categoryColor
            )
        }
    }
}

@Composable
private fun SetDetailRow(
    setNumber: Int,
    reps: Int,
    weight: Double,
    targetReps: Int,
    isOverallPr: Boolean,
    isSetSpecificPr: Boolean,
    categoryColor: androidx.compose.ui.graphics.Color
) {
    val metTarget = reps >= targetReps
    val hasPr = isOverallPr || isSetSpecificPr
    
    // Gold for overall PR, blue for set-specific PR
    val prBackgroundColor = when {
        isOverallPr -> Color(0xFFFFD700).copy(alpha = 0.1f)
        isSetSpecificPr -> Color(0xFF4FC3F7).copy(alpha = 0.1f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = prBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Set $setNumber",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Overall PR badge (gold)
                if (isOverallPr) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFFFD700))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "PR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Set-specific PR badge (blue)
                if (isSetSpecificPr) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4FC3F7))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "SET PR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$reps reps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (metTarget) categoryColor else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${weight.toInt()} lbs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = when {
                        isOverallPr -> Color(0xFFB8860B)
                        isSetSpecificPr -> Color(0xFF0288D1)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = if (hasPr) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

private fun formatFullDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

private fun formatTimeRange(startTime: Long, endTime: Long?): String {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val start = timeFormat.format(Date(startTime))
    val end = endTime?.let { timeFormat.format(Date(it)) } ?: "In Progress"
    return "$start - $end"
}

