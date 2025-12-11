# Lift Fiercely

A native Android workout logging app built with Kotlin and Jetpack Compose.

## Features

- **Start/End Workouts**: Track your workout sessions with timestamps and duration
- **Exercise Selection**: Choose from 12 predefined exercises organized by category
  - **Push**: Incline Press, Deficit Press, Overhead Press, Flat Press, Seated Dip, Assisted Dip
  - **Pull**: High Row, Seated Row, Assisted Pull Up
  - **Other**: LU Raise, Barbell Curl, Skull Crusher
- **Set Logging**: Record reps and weight for each set
- **Workout History**: View all past workouts with detailed summaries
- **Local Storage**: All data saved locally using Room database

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Database**: Room (SQLite wrapper)
- **Architecture**: MVVM with Repository pattern
- **Navigation**: Jetpack Navigation Compose

## Project Structure

```
app/src/main/java/com/liftfiercely/
├── data/
│   ├── database/       # Room database, DAOs
│   ├── model/          # Data classes (Exercise, Workout, WorkoutSet)
│   └── repository/     # Repository for data access
├── ui/
│   ├── navigation/     # Navigation routes and NavHost
│   ├── screens/        # Compose screens (Home, ActiveWorkout, WorkoutDetail)
│   └── theme/          # Material 3 theme, colors, typography
├── viewmodel/          # ViewModels for each screen
├── LiftFiercelyApp.kt  # Application class
└── MainActivity.kt     # Entry point
```

## Building the App

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 34
- JDK 17

### Steps

1. Open the `LiftFiercely` folder in Android Studio
2. Wait for Gradle sync to complete
3. Connect an Android device or start an emulator (API 26+)
4. Click "Run" or press `Shift + F10`

## Color Theme

The app uses a fierce color scheme:
- **Primary**: Fierce Orange (#FF6B35)
- **Push exercises**: Orange tones
- **Pull exercises**: Teal/Cyan (#4ECDC4)
- **Other exercises**: Purple (#B388FF)
- **Background**: Dark (#0D0D0D)

## Usage

1. **Start a Workout**: Tap "START WORKOUT" on the home screen
2. **Select Category**: Choose Push, Pull, or Other
3. **Pick an Exercise**: Tap an exercise from the list
4. **Log a Set**: Enter reps and weight, tap "LOG SET"
5. **Repeat**: Continue logging sets for same or different exercises
6. **End Workout**: Tap "END" when finished
7. **View History**: Tap any past workout on the home screen to see details


