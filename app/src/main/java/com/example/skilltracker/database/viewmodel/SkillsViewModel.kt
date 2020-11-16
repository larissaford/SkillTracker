package com.example.skilltracker.database.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.skilltracker.database.SkillsRepository
import com.example.skilltracker.database.entity.SkillSet
import kotlinx.coroutines.launch

/**
 * Uses Coroutines seen by the viewModelScope.launch function
 */
class SkillsViewModel(app: Application): AndroidViewModel(app) {
    private val repository: SkillsRepository = SkillsRepository(app)
    private val allSkillSetes : LiveData<List<SkillSet>> = repository.getSkillSet()

    /**
     * get all SkillSetes from database
     */
    fun getSkillSet(): LiveData<List<SkillSet>> {
        return allSkillSetes
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
}