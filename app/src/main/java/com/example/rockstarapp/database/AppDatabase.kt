package com.example.rockstarapp.database

import android.content.Context
import androidx.room.*
import com.example.rockstarapp.database.dao.RockstarDao
import com.example.rockstarapp.model.Rockstar

@Database(
    entities = [Rockstar::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun RockstarDao(): RockstarDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "rockstar.db").allowMainThreadQueries()
            .build()
    }
}

