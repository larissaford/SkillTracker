package com.example.skilltracker.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import androidx.room.RoomDatabase
import com.example.skilltracker.database.converter.DateConverter
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.*
import kotlinx.coroutines.launch


/**
 * Database to be implemented by Room. Sets up tables for SkillSet, Skill, Task, and joins between
 * them.  Implemented in a singleton pattern.
 */
@Database(entities = [SkillSet::class, SkillSetSkillCrossRef::class, Skill::class,
    SkillTaskCrossRef::class, Task::class], version = 11, exportSchema = false)
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