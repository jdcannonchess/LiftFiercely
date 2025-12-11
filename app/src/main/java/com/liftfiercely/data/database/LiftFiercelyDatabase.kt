package com.liftfiercely.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet

@Database(
    entities = [Workout::class, WorkoutSet::class],
    version = 2,
    exportSchema = false
)
abstract class LiftFiercelyDatabase : RoomDatabase() {
    
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSetDao(): WorkoutSetDao
    
    companion object {
        @Volatile
        private var INSTANCE: LiftFiercelyDatabase? = null
        
        fun getDatabase(context: Context): LiftFiercelyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LiftFiercelyDatabase::class.java,
                    "lift_fiercely_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

