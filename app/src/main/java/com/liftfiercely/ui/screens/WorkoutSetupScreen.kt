package com.liftfiercely.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liftfiercely.data.model.ExerciseCategory
import com.liftfiercely.ui.theme.CoralPrimary
import com.liftfiercely.ui.theme.OtherColor
import com.liftfiercely.ui.theme.PullColor
import com.liftfiercely.ui.theme.PushColor

@Composable
fun WorkoutSetupScreen(
    onBack: () -> Unit,
    onStartWorkout: (ExerciseCategory, Int, Float) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<ExerciseCategory?>(null) }
    var targetReps by remember { mutableIntStateOf(8) }
    var intensity by remember { mutableFloatStateOf(0.5f) }
    
    val allFieldsSelected = selectedCategory != null
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Workout Setup",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Category Selection
            Text(
                text = "What are you training today?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryCard(
                    category = ExerciseCategory.PUSH,
                    label = "Push",
                    color = PushColor,
                    isSelected = selectedCategory == ExerciseCategory.PUSH,
                    onClick = { selectedCategory = ExerciseCategory.PUSH },
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    category = ExerciseCategory.PULL,
                    label = "Pull",
                    color = PullColor,
                    isSelected = selectedCategory == ExerciseCategory.PULL,
                    onClick = { selectedCategory = ExerciseCategory.PULL },
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    category = ExerciseCategory.OTHER,
                    label = "Other",
                    color = OtherColor,
                    isSelected = selectedCategory == ExerciseCategory.OTHER,
                    onClick = { selectedCategory = ExerciseCategory.OTHER },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Rep Range Selection
            Text(
                text = "Target rep range",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(6, 8, 10, 12).forEach { reps ->
                    RepChip(
                        reps = reps,
                        isSelected = targetReps == reps,
                        onClick = { targetReps = reps },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Intensity Slider
            Text(
                text = "How intense are you feeling?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = getIntensityLabel(intensity),
                style = MaterialTheme.typography.bodyLarge,
                color = getIntensityColor(intensity),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Slider(
                value = intensity,
                onValueChange = { intensity = it },
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = getIntensityColor(intensity),
                    activeTrackColor = getIntensityColor(intensity),
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Light",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Maximum",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Let's Go Button
            AnimatedVisibility(
                visible = allFieldsSelected,
                enter = fadeIn() + slideInVertically { it }
            ) {
                Button(
                    onClick = { 
                        selectedCategory?.let { category ->
                            onStartWorkout(category, targetReps, intensity)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoralPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "LET'S GO",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun CategoryCard(
    category: ExerciseCategory,
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, color, RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        color = if (isSelected) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun RepChip(
    reps: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isSelected) {
                    Modifier.border(2.dp, CoralPrimary, RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }
            )
            .clickable(onClick = onClick),
        color = if (isSelected) CoralPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$reps",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) CoralPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getIntensityLabel(intensity: Float): String {
    return when {
        intensity < 0.2f -> "💤 Recovery Day"
        intensity < 0.4f -> "🚶 Light Session"
        intensity < 0.6f -> "💪 Solid Effort"
        intensity < 0.8f -> "🔥 Pushing Hard"
        else -> "⚡ MAXIMUM INTENSITY"
    }
}

private fun getIntensityColor(intensity: Float): Color {
    return when {
        intensity < 0.2f -> Color(0xFF4CAF50)  // Green
        intensity < 0.4f -> Color(0xFF8BC34A)  // Light Green
        intensity < 0.6f -> Color(0xFFFFC107)  // Amber
        intensity < 0.8f -> Color(0xFFFF9800)  // Orange
        else -> Color(0xFFFF5722)              // Deep Orange
    }
}



