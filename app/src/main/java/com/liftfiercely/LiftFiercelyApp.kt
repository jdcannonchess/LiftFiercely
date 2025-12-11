package com.liftfiercely

import android.app.Application
import com.liftfiercely.data.database.LiftFiercelyDatabase

class LiftFiercelyApp : Application() {
    
    val database: LiftFiercelyDatabase by lazy {
        LiftFiercelyDatabase.getDatabase(this)
    }
}


