package com.example.skilltracker.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.*

// TODO: Determine what should be cached later
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

    /* QUERIES */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSets
    }

    fun getSkills(): LiveData<List<Skill>> {
        return skillsDao.getAllSkills()
    }

    fun getTasks(): LiveData<List<Task>> {
        return skillsDao.getAllTasks()
    }

    fun getSkillSetWithSkills(): LiveData<List<SkillSetWithSkills>> {
        return skillsDao.getAllSkillSetWithSkills()
    }

    fun getSkillWithTasks(): LiveData<List<SkillWithTasks>> {
        return skillsDao.getAllSkillWithTasks()
    }

    /* INSERTS */
    suspend fun insertSkillSet(skillSet: SkillSet){
        skillsDao.insert(skillSet)
    }

    suspend fun insertSkill(skill: Skill){
        skillsDao.insert(skill)
    }

    suspend fun insertTask(task: Task){
        skillsDao.insert(task)
    }

//    suspend fun insertSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills){
//        skillsDao.insert(skillSetWithSkills)
//    }
//
//    suspend fun insertSkillsWithTasks(skillWithTasks: SkillWithTasks){
//        skillsDao.insert(skillWithTasks)
//    }

    /* DELETES */
    suspend fun nukeTable() = skillsDao.nukeTable()
}