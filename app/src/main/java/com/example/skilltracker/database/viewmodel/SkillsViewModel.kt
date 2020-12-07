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
 * Uses Coroutines seen by the viewModelScope.launch function
 */
class SkillsViewModel(app: Application): AndroidViewModel(app) {
    private val repository: SkillsRepository = SkillsRepository(app)
    private val allSkillSets : LiveData<List<SkillSet>> = repository.getSkillSet()
    private val allSkills : LiveData<List<Skill>> = repository.getSkills()
    private val allTasks : LiveData<List<Task>> = repository.getTasks()

    /**
     * get all SkillSetes from database
     */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSets
    }

    fun getTasks(): LiveData<List<Task>> {
        return allTasks
    }

    /**
     * Get all Skills from the database
     */
    fun getSkills(): LiveData<List<Skill>> {
        return allSkills
    }

    fun getSpecificSkillSetWithSkills(skillSetId: Long): LiveData<List<SkillSetWithSkills>> {
        return repository.getSpecificSkillSetWithSkills(skillSetId)
    }

    fun getSpecificSkillWithTasks(skillId: Long): LiveData<List<SkillWithTasks>> {
        return repository.getSpecificSkillWithTasks(skillId)
    }

    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>> {
        return repository.getSkillsFromJoin(skillSetId)
    }

    /**
     * gets rid of all values in database
     */
    fun nuke() = viewModelScope.launch{
        repository.nukeTable()
    }

    fun nukeSkill() = viewModelScope.launch {
        repository.nukeSkillTable()
    }

    fun nukeTask() = viewModelScope.launch {
        repository.nukeTaskTable()
    }

    fun nukeSkillSetSkillCrossRef() = viewModelScope.launch {
        repository.nukeSkillSetSkillCrossRefTable()
    }

    fun nukeSkillTaskCrossRef() = viewModelScope.launch {
        repository.nukeSkillTaskCrossRefTable()
    }

    /**
     * adds SkillSet to database
     */
    suspend fun insertSkillSet(skillSet: SkillSet): Long {
        return repository.insertSkillSet(skillSet)
    }

    /**
     * Insert a specific Task
     */
    suspend fun insertTasks(task: Task): Long {
        return repository.insertTask(task)
    }

    /**
     * Adds a Skill to the database
     */
    suspend fun insertSkill(skill: Skill): Long {
        return repository.insertSkill(skill)
    }

    /**
     * Adds a Skill to a SkillSet in the database
     */
    fun insertSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills) = GlobalScope.launch {
        repository.insertSkillSetWithSkills(skillSetWithSkills)
    }

    suspend fun insertNewSkillWithJoin(skillSet: SkillSet, skill: Skill) {
        repository.insertNewSkillAndJoin(skillSet, skill)
    }

    suspend fun insertNewTaskWithJoin(skill: Skill, task: Task) {
        repository.insertNewTaskWithJoin(skill, task)
    }

    /**
     * Update a specific skillSet
     */
    fun updateSkillSet(skillSet: SkillSet) = viewModelScope.launch {
        repository.update(skillSet)
    }

    fun updateSkill(skill: Skill) = viewModelScope.launch {
        repository.updateSkill(skill)
    }

    /**
     * Update a specific Task
     */
    fun updateTasks(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    /**
     * Delete a skill from a skill set
     */
    fun deleteSkillSetSkillCrossRef(skillSet: SkillSet, skill: Skill) = viewModelScope.launch {
        repository.deleteSkillSetSkillJoin(skillSet, skill)
    }
}