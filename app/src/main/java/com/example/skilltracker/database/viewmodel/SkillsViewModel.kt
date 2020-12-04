package com.example.skilltracker.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skilltracker.database.SkillsRepository
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.entity.SkillSetWithSkills
import com.example.skilltracker.database.entity.Task
import kotlinx.coroutines.launch

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

    /**
     * gets rid of all values in database
     */
    fun nuke() = viewModelScope.launch{
        repository.nukeTable()
    }

    /**
     * adds SkillSet to database
     */
    fun insertSkillSet(SkillSet: SkillSet) = viewModelScope.launch {
        repository.insertSkillSet(SkillSet)
    }

    /**
     * Update a specific skillSet
     */
    fun updateSkillSet(skillSet: SkillSet) = viewModelScope.launch {
        repository.update(skillSet)
    }

    /**
     * Update a specific Task
     */
    fun updateTasks(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    /**
     * Insert a specific Task
     */
    fun insertTasks(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    /**
     * Adds a Skill to the database
     */
    fun insertSkill(skill: Skill) = viewModelScope.launch {
        repository.insertSkill(skill)
    }

    /**
     * Adds a Skill to a SkillSet in the database
     */
    fun insertSkillSetWithSkills(skillSetWithSkills: SkillSetWithSkills) = viewModelScope.launch {
        repository.insertSkillSetWithSkills(skillSetWithSkills)
    }

    fun updateSkill(skill: Skill) = viewModelScope.launch {
        repository.updateSkill(skill)
    }
}