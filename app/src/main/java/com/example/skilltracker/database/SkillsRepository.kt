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

    fun getSpecificSkillSetWithSkills(skillSetId: Long): LiveData<List<SkillSetWithSkills>> {
        return skillsDao.getSpecificSkillSetWithSkills(skillSetId)
    }

    fun getSkillWithTasks(): LiveData<List<SkillWithTasks>> {
        return skillsDao.getAllSkillWithTasks()
    }

    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>> {
        return skillsDao.getSkillsFromJoin(skillSetId)
    }

    /* INSERTS */
    suspend fun insertSkillSet(skillSet: SkillSet){
        skillsDao.insert(skillSet)
    }

    suspend fun insertSkill(skill: Skill) : Long {
        var skillIds = skillsDao.insert(skill)
        println("REPO ADDED SKILLID ${skillIds[0]}")
        return skillIds[0]
    }

    suspend fun insertTask(task: Task){
        skillsDao.insert(task)
    }

    suspend fun insertNewSkillAndJoin(skillSet: SkillSet, skill: Skill) {
        println("ADDING SKILL LAUNCHED")
        var skillId = this.insertSkill(skill)
        skill.skillId = skillId
        println("ADDING JOIN WITH SKILLID: $skillId")
        this.insertSkillSetWithSkills(SkillSetWithSkills(skillSet, listOf(skill)))
    }

//    suspend fun insertNewSkillSetSkillJoin()

    suspend fun insertSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills){
        println("IN REPO")
        // get skillset id
        val skillSetId = skillSetWithSkills.skillSet.skillSetId
        // use array initialization to create rows
        val join = Array(skillSetWithSkills.skills.size) { it ->
            println("NEW JOIN: ${skillSetWithSkills.skills[it].skillId}")
                SkillSetSkillCrossRef(
                    skillSetId,
                    skillSetWithSkills.skills[it].skillId
            )
        }

        for(x in join) {
            println(x.skillId)
        }
        skillsDao.insert(*join)
    }

    suspend fun insertSkillsWithTasks(skillWithTasks: SkillWithTasks){
        // get skill id for all rows
        val skillSetId = skillWithTasks.skill.skillId
        // use array initialization to create join rows
        val join = Array(skillWithTasks.tasks.size) { it ->
            SkillTaskCrossRef(
                skillSetId,
                skillWithTasks.tasks[it].taskId
            )
        }
        skillsDao.insert(*join)
    }

    /* DELETES */
    suspend fun nukeTable() = skillsDao.nukeTable()

    /* UPDATES */
    suspend fun update(skillSet: SkillSet) {
        skillsDao.update(skillSet)
    }

    suspend fun updateSkill(skill: Skill) {
        skillsDao.updateSkill(skill)
    }
}