package com.example.skilltracker.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.*

/**
 * Uses Dao to fetch data from Database and prepares joins for inserting into database
 * Coroutines seen by the "suspend" keyword
 * Returns Live data because it handles synchronization issues (doesn't have to be requeiried over
 * and over)
 * @param skillsDao Dao used to interact with database
 */
class SkillsRepository(app: Application){
    private var skillsDao: SkillDao

    init{
        val database: SkillsDatabase = SkillsDatabase.getInstance(app)
        skillsDao = database.skillDao()
    }

    /* QUERIES */
    /**
     * Uses Dao function to return all SkillSets in database
     * @return List of SkillSets
     */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return skillsDao.getAllSkillSets()
    }

    /**
     * Uses Dao function to return all Skills in database
     * @return List of Skills
     */
    fun getSkills(): LiveData<List<Skill>> {
        return skillsDao.getAllSkills()
    }

    /**
     * Uses Dao function to return all Tasks in database
     * @return List of Tasks
     */
    fun getTasks(): LiveData<List<Task>> {
        return skillsDao.getAllTasks()
    }

    /**
     * Uses Dao function to return specific join data between SkillSet and Skill tables in database
     * @param skillSetId the ID of SkillSet to return Skills related to it
     * @return List of join data between SkillSet and Skill tables
     */
    fun getSpecificSkillSetWithSkills(skillSetId: Long): LiveData<List<SkillSetWithSkills>> {
        return skillsDao.getSpecificSkillSetWithSkills(skillSetId)
    }

    /**
     * Uses Dao function to return specific join data between Skill and Tasks tables in database
     * @param skillId the ID of Skill to return Tasks related to it
     * @return List of join data between Skill and Task tables
     */
    fun getSpecificSkillWithTasks(skillId: Long): LiveData<List<SkillWithTasks>> {
        return skillsDao.getSpecificSkillWithTasks(skillId)
    }

    /**
     * Uses Dao function to return all join data between Skill and Task tables in database
     * @return List Skills with related Tasks
     */
    fun getSkillWithTasks(): LiveData<List<SkillWithTasks>> {
        return skillsDao.getAllSkillWithTasks()
    }

    /**
     * Uses Dao function to return specific join data between Skill and Task tables in database
     * based on a SkillSet's Id
     * @return List of Skills related to a SkillSet
     */
    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>> {
        return skillsDao.getSkillsFromJoin(skillSetId)
    }

    /**
     * Uses Dao function to return specific join data between Skill and Task tables in database
     * based on a SkillSet's Id
     * @return List Skills with related Tasks
     */
    fun getAllSkillWithTasksForSpecificSkillSet(skillSetId: Long): LiveData<List<SkillWithTasks>> {
        return skillsDao.getAllSkillWithTasksForSpecificSkillSet(skillSetId)
    }

    /* INSERTS */
    /**
     * Uses Dao function to insert a SkillSet into database
     * @param skillSet SkillSet to be inserted
     * @return rowId of newly inserted SkillSet
     */
    suspend fun insertSkillSet(skillSet: SkillSet): Long {
        var skillSetIds = skillsDao.insert(skillSet)
        return skillSetIds[0]
    }
    /**
     * Uses Dao function to insert a Skill into database
     * @param skill Skill to be inserted
     * @return rowId of newly inserted Skill
     */
    suspend fun insertSkill(skill: Skill): Long {
        var skillIds = skillsDao.insert(skill)
        return skillIds[0]
    }

    /**
     * Uses Dao function to insert a Task into database
     * @param task Task to be inserted
     * @return rowId of newly inserted Task
     */
    suspend fun insertTask(task: Task): Long {
        var taskIds = skillsDao.insert(task)
        return taskIds[0]
    }

    /**
     * Insert a join between a newly added Skill and existing SkillSet
     * @param skillSet existing SkillSet to have a relationship with new Skill
     * @param skill new Skill to have a relationship with an existing SkillSet
     */
    suspend fun insertNewSkillAndJoin(skillSet: SkillSet, skill: Skill) {
        this.insertSkillSetWithSkills(SkillSetWithSkills(skillSet, listOf(skill)))
    }

    /**
     * Insert a join between a newly added Task and existing Skill
     * @param skill existing Skill to have a relationship with new Task
     * @param task new Task to have a relationship with an existing Skill
     */
    suspend fun insertNewTaskWithJoin(skill: Skill, task: Task) {
        this.insertSkillsWithTasks(SkillWithTasks(skill, listOf(task)))
    }

    /**
     * Converts collection of SkillSet with list of Skills to SkillSetSkillCrossRef object
     * for insertion into database.  Uses Dao function to insert relationship between
     * SkillSet and Skill tables.
     * @param skillSetWithSkills collection of SkillSet with related Skills
     */
    suspend fun insertSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills){
        // Get skillset id
        val skillSetId = skillSetWithSkills.skillSet.skillSetId
        // Use array initialization to create join rows
        val join = Array(skillSetWithSkills.skills.size) { it ->
                SkillSetSkillCrossRef(
                    skillSetId,
                    skillSetWithSkills.skills[it].skillId
            )
        }
        skillsDao.insert(*join)
    }

    /**
     * Converts collection of Skill with list of Tasks to SkillTaskCrossRef object
     * for insertion into database.  Uses Dao function to insert relationship between
     * Skill and Task tables.
     * @param skillWithTasks collection of Skill with related Tasks
     */
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

    /* NUKES */
    /**
     * Uses Dao function to delete all rows in SkillSet table
     */
    suspend fun nukeSkillSetTable() = skillsDao.nukeSkillSetTable()

    /**
     * Uses Dao function to delete all rows in Skill table
     */
    suspend fun nukeSkillTable() = skillsDao.nukeSkillTable()

    /**
     * Uses Dao function to delete all rows in Task table
     */
    suspend fun nukeTaskTable() = skillsDao.nukeTaskTable()

    /**
     * Uses Dao function to delete all join rows between Skill and Task tables
     */
    suspend fun nukeSkillSetSkillCrossRefTable() = skillsDao.nukeSkillSetSkillCrossRefTable()

    /**
     * Uses Dao function to delete all join rows between Skill and Task tables
     */
    suspend fun nukeSkillTaskCrossRefTable() = skillsDao.nukeSkillTaskCrossRefTable()

    /* DELETES */
    /**
     * Prepares deletion for a single SkillSet and Skill.
     * All join rows should have the same SkillSet Id
     * @param skillSetWithSkills row data to be setup for deletion
     */
    suspend fun deleteSkillSetSkillJoin(skillSet: SkillSet, skill: Skill){
        this.deleteSkillSetWithSkills(SkillSetWithSkills(skillSet, listOf(skill)))
    }

    /**
     * Prepares join rows for deletion.
     * All join rows should have the same SkillSet Id
     * @param skillSetWithSkills row data to be setup for deletion
     */
    private suspend fun deleteSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills) {
        // Get SkillSet Id
        val skillSetId = skillSetWithSkills.skillSet.skillSetId
        // use array initialization to create rows
        val join = Array(skillSetWithSkills.skills.size) { it ->
            SkillSetSkillCrossRef(
                skillSetId,
                skillSetWithSkills.skills[it].skillId
            )
        }
        skillsDao.deleteSkillSetSkillCrossRef(*join)
    }

    /* UPDATES */
    /**
     * Uses Dao function to update a SkillSet
     */
    suspend fun update(skillSet: SkillSet) {
        skillsDao.update(skillSet)
    }

    /**
     * Uses Dao function to update a Skill
     */
    suspend fun updateSkill(skill: Skill) {
        skillsDao.updateSkill(skill)
    }

    /**
     * Uses Dao function to update a Task
     */
    suspend fun updateTask(task: Task) {
        skillsDao.updateTask(task)
    }
}