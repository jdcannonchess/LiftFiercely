package com.liftfiercely.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.liftfiercely.LiftFiercelyApp
import com.liftfiercely.data.repository.WorkoutRepository
import com.liftfiercely.ui.screens.ActiveWorkoutScreen
import com.liftfiercely.ui.screens.CalendarScreen
import com.liftfiercely.ui.screens.HomeScreen
import com.liftfiercely.ui.screens.WeightReferenceScreen
import com.liftfiercely.ui.screens.WorkoutDetailScreen
import com.liftfiercely.viewmodel.ActiveWorkoutViewModel
import com.liftfiercely.viewmodel.CalendarViewModel
import com.liftfiercely.viewmodel.HomeViewModel
import com.liftfiercely.viewmodel.WeightReferenceViewModel
import com.liftfiercely.viewmodel.WorkoutDetailViewModel

@Composable
fun LiftFiercelyNavHost(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean = false,
    onThemeToggle: () -> Unit = {}
) {
    val context = LocalContext.current
    val app = context.applicationContext as LiftFiercelyApp
    val repository = WorkoutRepository(
        app.database.workoutDao(),
        app.database.workoutSetDao()
    )
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory(repository)
            )
            HomeScreen(
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onStartWorkout = { workoutId ->
                    navController.navigate(NavRoutes.ActiveWorkout.createRoute(workoutId))
                },
                onWorkoutClick = { workoutId ->
                    navController.navigate(NavRoutes.WorkoutDetail.createRoute(workoutId))
                },
                onContinueWorkout = { workoutId ->
                    navController.navigate(NavRoutes.ActiveWorkout.createRoute(workoutId))
                },
                onWeightReferenceClick = {
                    navController.navigate(NavRoutes.WeightReference.route)
                },
                onCalendarClick = {
                    navController.navigate(NavRoutes.Calendar.route)
                }
            )
        }
        
        composable(
            route = NavRoutes.ActiveWorkout.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: return@composable
            val viewModel: ActiveWorkoutViewModel = viewModel(
                factory = ActiveWorkoutViewModel.Factory(repository, workoutId)
            )
            ActiveWorkoutScreen(
                viewModel = viewModel,
                onWorkoutEnded = {
                    navController.popBackStack(NavRoutes.Home.route, inclusive = false)
                }
            )
        }
        
        composable(
            route = NavRoutes.WorkoutDetail.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: return@composable
            val viewModel: WorkoutDetailViewModel = viewModel(
                factory = WorkoutDetailViewModel.Factory(repository, workoutId)
            )
            WorkoutDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.WeightReference.route) {
            val viewModel: WeightReferenceViewModel = viewModel(
                factory = WeightReferenceViewModel.Factory(repository)
            )
            WeightReferenceScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        
        composable(NavRoutes.Calendar.route) {
            val viewModel: CalendarViewModel = viewModel(
                factory = CalendarViewModel.Factory(repository)
            )
            CalendarScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
