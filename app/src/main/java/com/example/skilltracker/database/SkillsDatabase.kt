package com.example.skilltracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.room.RoomDatabase
import com.example.skilltracker.database.converter.DateConverter
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.SkillSet
import kotlinx.coroutines.launch


/**
 * adding the synchronized keyword to the database makes it work with Coroutines
 */
@Database(entities = [SkillSet::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SkillsDatabase : RoomDatabase() {
    abstract fun skillDao(): SkillDao

    companion object {
        @Volatile
        private var INSTANCE: SkillsDatabase? = null

        fun getInstance(context: Application): SkillsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(SkillsDatabase::class){
                val instance = Room.databaseBuilder(
                    context.applicationContext, SkillsDatabase::class.java,
                    "skills_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}