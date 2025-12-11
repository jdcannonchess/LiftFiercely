package com.liftfiercely.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liftfiercely.data.model.Exercise
import com.liftfiercely.data.model.ExerciseCategory
import com.liftfiercely.data.model.WorkoutSet
import com.liftfiercely.ui.theme.CoralPrimary
import com.liftfiercely.ui.theme.DangerRed
import com.liftfiercely.ui.theme.LightSurface
import com.liftfiercely.ui.theme.LightSurfaceAlt
import com.liftfiercely.ui.theme.getCategoryColor
import com.liftfiercely.viewmodel.ActiveWorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActiveWorkoutScreen(
    viewModel: ActiveWorkoutViewModel,
    onWorkoutEnded: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showEndDialog by remember { mutableStateOf(false) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var repsDropdownExpanded by remember { mutableStateOf(false) }
    var timerMenuExpanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    
    // Handle timer finished - play sound and vibrate
    LaunchedEffect(uiState.timerFinished) {
        if (uiState.timerFinished) {
            playTimerSound(context)
            vibrateDevice(context)
            viewModel.clearTimerFinished()
        }
    }
    
    LaunchedEffect(uiState.workoutEnded) {
        if (uiState.workoutEnded) {
            onWorkoutEnded()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
MaterialTheme.colorScheme.background
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WORKOUT",
                        style = MaterialTheme.typography.headlineSmall,
                        color = CoralPrimary,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    uiState.workout?.let { workout ->
                        Text(
                            text = "Started ${formatTime(workout.startTime)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Compact Rest Timer (only visible when running)
                    AnimatedVisibility(
                        visible = uiState.isTimerRunning,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Box {
                            val isLowTime = uiState.timerSecondsRemaining <= 10
                            val timerColor by animateColorAsState(
                                targetValue = if (isLowTime) DangerRed else CoralPrimary,
                                label = "timerColor"
                            )
                            
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { timerMenuExpanded = true },
                                color = timerColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val minutes = uiState.timerSecondsRemaining / 60
                                    val seconds = uiState.timerSecondsRemaining % 60
                                    
                                    if (uiState.isTimerPaused) {
                                        Icon(
                                            imageVector = Icons.Default.Pause,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = timerColor
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    
                                    Text(
                                        text = String.format("%d:%02d", minutes, seconds),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = timerColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            // Timer controls dropdown
                            DropdownMenu(
                                expanded = timerMenuExpanded,
                                onDismissRequest = { timerMenuExpanded = false },
                                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = CoralPrimary
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("+15 seconds")
                                        }
                                    },
                                    onClick = {
                                        viewModel.adjustTimer(15)
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Remove,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = CoralPrimary
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("-15 seconds")
                                        }
                                    },
                                    onClick = {
                                        viewModel.adjustTimer(-15)
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (uiState.isTimerPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = CoralPrimary
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(if (uiState.isTimerPaused) "Resume" else "Pause")
                                        }
                                    },
                                    onClick = {
                                        if (uiState.isTimerPaused) viewModel.resumeTimer()
                                        else viewModel.pauseTimer()
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = DangerRed
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Skip", color = DangerRed)
                                        }
                                    },
                                    onClick = {
                                        viewModel.skipTimer()
                                        timerMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // End button
                    Button(
                        onClick = { showEndDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DangerRed.copy(alpha = 0.2f),
                            contentColor = DangerRed
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("END", fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            // Dropdown selectors row (Category and Target Reps)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category Dropdown
                Box(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .neumorphic(
                                backgroundColor = LightSurfaceAlt,
                                cornerRadius = 12.dp,
                                shadowRadius = 5.dp,
                                offsetX = 3.dp,
                                offsetY = 3.dp
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { categoryDropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CATEGORY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = uiState.selectedCategory.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    DropdownMenu(
                        expanded = categoryDropdownExpanded,
                        onDismissRequest = { categoryDropdownExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        ExerciseCategory.entries.forEach { category ->
                            val catColor = getCategoryColor(category)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(catColor)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = category.name,
                                            color = if (uiState.selectedCategory == category) 
                                                catColor 
                                            else MaterialTheme.colorScheme.onSurface,
                                            fontWeight = if (uiState.selectedCategory == category) 
                                                FontWeight.Bold 
                                            else FontWeight.Normal
                                        )
                                    }
                                },
                                onClick = {
                                    viewModel.selectCategory(category)
                                    categoryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                
                // Target Reps Dropdown
                Box {
                    Box(
                        modifier = Modifier
                            .neumorphic(
                                backgroundColor = LightSurfaceAlt,
                                cornerRadius = 12.dp,
                                shadowRadius = 5.dp,
                                offsetX = 3.dp,
                                offsetY = 3.dp
                            )
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { repsDropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TARGET",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "${uiState.targetReps} reps",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    DropdownMenu(
                        expanded = repsDropdownExpanded,
                        onDismissRequest = { repsDropdownExpanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        listOf(6, 8, 10, 12, 15).forEach { reps ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "$reps reps",
                                        color = if (uiState.targetReps == reps) 
                                            CoralPrimary 
                                        else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (uiState.targetReps == reps) 
                                            FontWeight.Bold 
                                        else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    viewModel.updateTargetReps(reps)
                                    repsDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                // Exercise selector
                item {
                    Text(
                        text = "EXERCISE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val exercises = Exercise.getByCategory(uiState.selectedCategory)
                    val chunkedExercises = exercises.chunked(3)
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        chunkedExercises.forEach { rowExercises ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowExercises.forEach { exercise ->
                                    ExerciseChip(
                                        exercise = exercise,
                                        isSelected = uiState.selectedExercise?.id == exercise.id,
                                        onClick = { viewModel.selectExercise(exercise) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                // Fill empty slots if row has less than 3 items
                                repeat(3 - rowExercises.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Set input (only show when exercise selected)
                item {
                    AnimatedVisibility(
                        visible = uiState.selectedExercise != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column {
                            Text(
                                text = "LOG SET",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            SetInputCard(
                                exerciseName = uiState.selectedExercise?.name ?: "",
                                setNumber = uiState.nextOverallSetNumber,
                                targetReps = uiState.targetReps,
                                recommendedWeight = uiState.recommendedWeight,
                                reps = uiState.reps,
                                weight = uiState.weight,
                                onRepsChange = viewModel::updateReps,
                                onWeightChange = viewModel::updateWeight,
                                onLogSet = {
                                    viewModel.logSet()
                                    focusManager.clearFocus()
                                },
                                canLog = uiState.reps.isNotBlank() && uiState.weight.isNotBlank()
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
                
                // Logged sets header
                if (uiState.sets.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LOGGED SETS",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${uiState.sets.size} total",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                
                // Logged sets list (reversed to show most recent first)
                items(
                    items = uiState.sets.reversed(),
                    key = { it.id }
                ) { set ->
                    LoggedSetCard(
                        set = set,
                        onDelete = { viewModel.deleteSet(set.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        
    }
    
    // End workout confirmation dialog
    if (showEndDialog) {
        AlertDialog(
            onDismissRequest = { showEndDialog = false },
            title = {
                Text(
                    text = "End Workout?",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "You've logged ${uiState.sets.size} sets. Are you sure you want to end this workout?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEndDialog = false
                        viewModel.endWorkout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Text("End Workout")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun ExerciseChip(
    exercise: Exercise,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = getCategoryColor(exercise.category)
    Box(
        modifier = modifier
            .neumorphic(
                backgroundColor = if (isSelected) categoryColor else LightSurfaceAlt,
                cornerRadius = 10.dp,
                shadowRadius = 4.dp,
                offsetX = 3.dp,
                offsetY = 3.dp,
                isPressed = isSelected
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = exercise.name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun SetInputCard(
    exerciseName: String,
    setNumber: Int,
    targetReps: Int,
    recommendedWeight: Double,
    reps: String,
    weight: String,
    onRepsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onLogSet: () -> Unit,
    canLog: Boolean
) {
    val focusManager = LocalFocusManager.current
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .neumorphic(
                backgroundColor = LightSurface,
                cornerRadius = 16.dp,
                shadowRadius = 8.dp,
                offsetX = 5.dp,
                offsetY = 5.dp
            )
            .clip(RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with exercise name and set number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = exerciseName,
                        style = MaterialTheme.typography.titleMedium,
                        color = CoralPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Set #$setNumber",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Target: $targetReps reps",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (recommendedWeight > 0) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Rec: ${recommendedWeight.toInt()} lbs",
                                style = MaterialTheme.typography.bodySmall,
                                color = CoralPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            PlateBreakdown(totalWeight = recommendedWeight)
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = reps,
                    onValueChange = { onRepsChange(it.filter { c -> c.isDigit() }) },
                    label = { Text("Reps") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Right) }
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CoralPrimary,
                        cursorColor = CoralPrimary,
                        focusedLabelColor = CoralPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                OutlinedTextField(
                    value = weight,
                    onValueChange = { 
                        val filtered = it.filter { c -> c.isDigit() || c == '.' }
                        if (filtered.count { c -> c == '.' } <= 1) {
                            onWeightChange(filtered)
                        }
                    },
                    label = { Text("Weight (lbs)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (canLog) onLogSet()
                        }
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CoralPrimary,
                        cursorColor = CoralPrimary,
                        focusedLabelColor = CoralPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onLogSet,
                enabled = canLog,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoralPrimary,
                    disabledContainerColor = CoralPrimary.copy(alpha = 0.3f)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOG SET",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun LoggedSetCard(
    set: WorkoutSet,
    onDelete: () -> Unit
) {
    val exercise = Exercise.getById(set.exerciseId)
    val categoryColor = exercise?.let { getCategoryColor(it.category) } ?: CoralPrimary
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category indicator
            Box(
                modifier = Modifier
                    .size(4.dp, 40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(categoryColor)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise?.name ?: "Unknown",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Set ${set.setNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${set.reps} reps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = categoryColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${set.weight} lbs",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete set",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}

// Neumorphic shadow modifier for soft 3D effect (light theme optimized)
private fun Modifier.neumorphic(
    backgroundColor: Color,
    cornerRadius: Dp = 12.dp,
    shadowRadius: Dp = 6.dp,
    lightShadowColor: Color = Color.White.copy(alpha = 0.85f),
    darkShadowColor: Color = Color.Black.copy(alpha = 0.12f),
    offsetX: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    isPressed: Boolean = false
): Modifier = this.then(
    Modifier.drawBehind {
        val shadowRadiusPx = shadowRadius.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()
        val cornerRadiusPx = cornerRadius.toPx()
        
        // For pressed state, invert the shadows (creates "pressed in" effect)
        val lightOffsetX = if (isPressed) offsetXPx else -offsetXPx
        val lightOffsetY = if (isPressed) offsetYPx else -offsetYPx
        val darkOffsetX = if (isPressed) -offsetXPx else offsetXPx
        val darkOffsetY = if (isPressed) -offsetYPx else offsetYPx
        
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            
            // Draw light shadow (top-left for raised, bottom-right for pressed)
            frameworkPaint.color = lightShadowColor.toArgb()
            frameworkPaint.setShadowLayer(
                shadowRadiusPx,
                lightOffsetX,
                lightOffsetY,
                lightShadowColor.toArgb()
            )
            canvas.drawRoundRect(
                0f, 0f, size.width, size.height,
                cornerRadiusPx, cornerRadiusPx,
                paint
            )
            
            // Draw dark shadow (bottom-right for raised, top-left for pressed)
            frameworkPaint.color = darkShadowColor.toArgb()
            frameworkPaint.setShadowLayer(
                shadowRadiusPx,
                darkOffsetX,
                darkOffsetY,
                darkShadowColor.toArgb()
            )
            canvas.drawRoundRect(
                0f, 0f, size.width, size.height,
                cornerRadiusPx, cornerRadiusPx,
                paint
            )
            
            // Draw the main background
            frameworkPaint.clearShadowLayer()
            frameworkPaint.color = backgroundColor.toArgb()
            canvas.drawRoundRect(
                0f, 0f, size.width, size.height,
                cornerRadiusPx, cornerRadiusPx,
                paint
            )
        }
    }
)

private fun formatTime(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}

// Calculate minimum plates needed per side for a given total weight
private fun calculatePlateBreakdown(totalWeight: Double): Map<Double, Int> {
    val plateSizes = listOf(45.0, 35.0, 25.0, 10.0, 5.0, 2.5)
    val plates = mutableMapOf<Double, Int>()
    var remaining = totalWeight / 2  // Per side
    
    for (size in plateSizes) {
        val count = (remaining / size).toInt()
        if (count > 0) {
            plates[size] = count
            remaining -= count * size
        }
    }
    return plates
}

// Get color for plate size
private fun getPlateColor(weight: Double): Color {
    return when (weight) {
        45.0 -> Color(0xFFE53935)  // Red
        35.0 -> Color(0xFF1E88E5)  // Blue
        25.0 -> Color(0xFF43A047)  // Green
        10.0 -> Color(0xFFFFB300)  // Yellow/Amber
        5.0 -> CoralPrimary        // Orange
        2.5 -> Color(0xFF757575)   // Gray
        else -> Color.Gray
    }
}

@Composable
private fun PlateBreakdown(
    totalWeight: Double,
    modifier: Modifier = Modifier
) {
    val plates = calculatePlateBreakdown(totalWeight)
    
    if (plates.isEmpty()) return
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display plates in order from largest to smallest
        listOf(45.0, 35.0, 25.0, 10.0, 5.0, 2.5).forEach { size ->
            val count = plates[size] ?: 0
            repeat(count) {
                PlateBadge(weight = size)
            }
        }
    }
}

@Composable
private fun PlateBadge(weight: Double) {
    val color = getPlateColor(weight)
    val displayText = if (weight == 2.5) "2.5" else weight.toInt().toString()
    
    Surface(
        modifier = Modifier
            .height(20.dp),
        color = color,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayText,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun playTimerSound(context: Context) {
    try {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        
        val soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        
        // Use system notification sound
        val notification = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI
        val mediaPlayer = android.media.MediaPlayer.create(context, notification)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun vibrateDevice(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
