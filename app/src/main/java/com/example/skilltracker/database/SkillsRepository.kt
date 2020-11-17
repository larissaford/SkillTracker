package com.example.skilltracker.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.SkillSet

/**
 * uses Coroutines seen by the "suspend" keyword
 *
 * uses Live data because it handles synchronization issues (doesn't have to be requeiried over and
 * over)
 */
class SkillsRepository(app: Application){
    private var allSkillSets: LiveData<List<SkillSet>>
    private var skillsDao: SkillDao

    init{
        val database: SkillsDatabase = SkillsDatabase.getInstance(app)
        skillsDao = database.skillDao()
        allSkillSets = skillsDao.getAll()
    }

    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSets
    }

    suspend fun insertSkillSet(SkillSet: SkillSet){
        skillsDao.insert(SkillSet)
    }

    suspend fun nukeTable() = skillsDao.nukeTable()
}