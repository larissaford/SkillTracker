package com.example.skilltracker.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skilltracker.database.SkillsRepository
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.entity.SkillSetWithSkills
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

    /**
     * get all SkillSetes from database
     */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSets
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

    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>> {
        return repository.getSkillsFromJoin(skillSetId)
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
//        var result = insertSkill(skill)
//        var skillId = result.await()
//        skill.skillId = skillId
        repository.insertNewSkillAndJoin(skillSet, skill)
    }

//    fun insertNewSkillAndJoin(skillSet: SkillSet, skill: Skill) = viewModelScope.launch {
//        var result = repository.insertNewSkillAndJoin(skillSet, skill)
//        var skillId = result.await()
//    }

    fun updateSkill(skill: Skill) = viewModelScope.launch {
        repository.updateSkill(skill)
    }
}