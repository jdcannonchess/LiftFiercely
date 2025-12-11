package com.liftfiercely.ui.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object ActiveWorkout : NavRoutes("active_workout/{workoutId}") {
        fun createRoute(workoutId: Long) = "active_workout/$workoutId"
    }
    object WorkoutDetail : NavRoutes("workout_detail/{workoutId}") {
        fun createRoute(workoutId: Long) = "workout_detail/$workoutId"
    }
    object WeightReference : NavRoutes("weight_reference")
    object Calendar : NavRoutes("calendar")
}

