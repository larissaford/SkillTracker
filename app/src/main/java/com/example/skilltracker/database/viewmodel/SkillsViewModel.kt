package com.example.skilltracker.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skilltracker.database.SkillsRepository
import com.example.skilltracker.database.entity.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

/**
 * ViewModel for displaying data from Database to Fragments
 * Uses Coroutines seen by the viewModelScope.launch function
 * @property repository object that gets data from Database
 * @property allSkillSets cached SkillSet data from Database
 * @property allSkills cached Skill data from Database
 * @property allTasks cached Task data from Database
 */
class SkillsViewModel(app: Application): AndroidViewModel(app) {
    private val repository: SkillsRepository = SkillsRepository(app)
    private val allSkillSets : LiveData<List<SkillSet>> = repository.getSkillSet()
    private val allSkills : LiveData<List<Skill>> = repository.getSkills()
    private val allTasks : LiveData<List<Task>> = repository.getTasks()

    /* Queries */
    /**
     * Get all SkillSets from database
     * @return List of SkillSets
     */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSets
    }

    /**
     * Get all Skills from the database
     * @return List of Skills
     */
    fun getSkills(): LiveData<List<Skill>> {
        return allSkills
    }

    /**
     * Get all SkillSets from database
     * @return list of Tasks
     */
    fun getTasks(): LiveData<List<Task>> {
        return allTasks
    }

    /**
     * Get Skills with related Tasks based on the Skill's Id
     * @return list of Skills with related Tasks
     */
    fun getSpecificSkillWithTasks(skillId: Long): LiveData<List<SkillWithTasks>> {
        return repository.getSpecificSkillWithTasks(skillId)
    }

    /**
     * Get Skills related to a SkillSet based on the Skillset's Id
     * @return list of Skills
     */
    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>> {
        return repository.getSkillsFromJoin(skillSetId)
    }

    /**
     * Get Skills with related Tasks based on a SkillSet's Id
     * @return list of Skills with related Tasks
     */
    fun getAllSkillWithTasksForSpecificSkillSet(skillSetId: Long): LiveData<List<SkillWithTasks>> {
        return repository.getAllSkillWithTasksForSpecificSkillSet(skillSetId)
    }

    /* INSERTS */
    /**
     * Insert a SkillSet to database
     * @param skillSet SkillSet to be inserted to database
     * @return rowId of newly added SkillSet
     */
    suspend fun insertSkillSet(skillSet: SkillSet): Long {
        return repository.insertSkillSet(skillSet)
    }

    /**
     * Insert a Skill to the database
     * @param skill Skill to be inserted to database
     * @return rowId of newly added Skill
     */
    suspend fun insertSkill(skill: Skill): Long {
        return repository.insertSkill(skill)
    }

    /**
     * Insert a Task to database
     * @param task Task to be inserted to database
     * @return rowId of newly added Task
     */
    suspend fun insertTasks(task: Task): Long {
        return repository.insertTask(task)
    }

    /**
     * Insert join relation between an existing SkillSet and newly added Skill
     * @param skillSet existing SkillSet in database
     * @param skill newly added Skill in database
     */
    suspend fun insertNewSkillWithJoin(skillSet: SkillSet, skill: Skill) {
        repository.insertNewSkillAndJoin(skillSet, skill)
    }

    /**
     * Insert join relation between an existing Skill and newly added Task
     * @param skill existing Skill in database
     * @param task newly added Task in database
     */
    suspend fun insertNewTaskWithJoin(skill: Skill, task: Task) {
        repository.insertNewTaskWithJoin(skill, task)
    }

    /* UPDATES */
    /**
     * Update a specific skillSet
     * @param skillSet to be updated
     */
    fun updateSkillSet(skillSet: SkillSet) = viewModelScope.launch {
        repository.update(skillSet)
    }

    /**
     * Update a specific skillSet
     * @param skill Skill to be updated
     */
    fun updateSkill(skill: Skill) = viewModelScope.launch {
        repository.updateSkill(skill)
    }

    /**
     * Update a specific Task
     * @param task Task to be updated
     */
    fun updateTasks(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    /**
     * Deletes a join relation between SkillSet and Skill tables
     * @param skillSet existing SkillSet in database
     * @param skill existing Skill in database
     */
    fun deleteSkillSetSkillCrossRef(skillSet: SkillSet, skill: Skill) = viewModelScope.launch {
        repository.deleteSkillSetSkillJoin(skillSet, skill)
    }

    /* NUKES */
    /**
     * Deletes all rows in SkillSet table
     */
    fun nukeSkillSet() = viewModelScope.launch{
        repository.nukeSkillSetTable()
    }

    /**
     * Deletes all rows in Skill table
     */
    fun nukeSkill() = viewModelScope.launch {
        repository.nukeSkillTable()
    }

    /**
     * Deletes all rows in Task table
     */
    fun nukeTask() = viewModelScope.launch {
        repository.nukeTaskTable()
    }

    /**
     * Deletes all rows in join table between SkillSet and Skill tables
     */
    fun nukeSkillSetSkillCrossRef() = viewModelScope.launch {
        repository.nukeSkillSetSkillCrossRefTable()
    }

    /**
     * Deletes all rows in join table between Skill and Task tables
     */
    fun nukeSkillTaskCrossRef() = viewModelScope.launch {
        repository.nukeSkillTaskCrossRefTable()
    }
}