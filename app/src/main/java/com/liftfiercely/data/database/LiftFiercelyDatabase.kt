package com.liftfiercely.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.liftfiercely.data.model.Workout
import com.liftfiercely.data.model.WorkoutSet

@Database(
    entities = [Workout::class, WorkoutSet::class],
    version = 3,
    exportSchema = false
)
abstract class LiftFiercelyDatabase : RoomDatabase() {
    
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSetDao(): WorkoutSetDao
    
    companion object {
        @Volatile
        private var INSTANCE: LiftFiercelyDatabase? = null
        
        // Migration from version 2 to 3: Add bodyWeight column to workouts table
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE workouts ADD COLUMN bodyWeight REAL NOT NULL DEFAULT 0.0")
            }
        }
        
        fun getDatabase(context: Context): LiftFiercelyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LiftFiercelyDatabase::class.java,
                    "lift_fiercely_database"
                )
                    .addMigrations(MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

