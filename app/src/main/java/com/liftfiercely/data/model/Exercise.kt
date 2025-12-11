package com.liftfiercely.data.model

data class Exercise(
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val defaultWeight: Double,
    val isAssisted: Boolean = false
) {
    companion object {
        val ALL_EXERCISES = listOf(
            // Pull exercises
            Exercise("high_row", "High Row", ExerciseCategory.PULL, defaultWeight = 235.0),
            Exercise("seated_row", "Seated Row", ExerciseCategory.PULL, defaultWeight = 275.0),
            Exercise("assisted_pull_up", "Assisted Pull Up", ExerciseCategory.PULL, defaultWeight = 40.0, isAssisted = true),
            
            // Push exercises
            Exercise("incline_press", "Incline Press", ExerciseCategory.PUSH, defaultWeight = 160.0),
            Exercise("deficit_press", "Deficit Press", ExerciseCategory.PUSH, defaultWeight = 170.0),
            Exercise("overhead_press", "Overhead Press", ExerciseCategory.PUSH, defaultWeight = 45.0),
            Exercise("flat_press", "Flat Press", ExerciseCategory.PUSH, defaultWeight = 195.0),
            Exercise("seated_dip", "Seated Dip", ExerciseCategory.PUSH, defaultWeight = 200.0),
            Exercise("assisted_dip", "Assisted Dip", ExerciseCategory.PUSH, defaultWeight = 65.0, isAssisted = true),
            
            // Other exercises
            Exercise("lu_raise", "LU Raise", ExerciseCategory.OTHER, defaultWeight = 20.0),
            Exercise("barbell_curl", "Barbell Curl", ExerciseCategory.OTHER, defaultWeight = 80.0),
            Exercise("skull_crusher", "Skull Crusher", ExerciseCategory.OTHER, defaultWeight = 90.0)
        )
        
        fun getByCategory(category: ExerciseCategory): List<Exercise> {
            return ALL_EXERCISES.filter { it.category == category }
        }
        
        fun getById(id: String): Exercise? {
            return ALL_EXERCISES.find { it.id == id }
        }
    }
}
