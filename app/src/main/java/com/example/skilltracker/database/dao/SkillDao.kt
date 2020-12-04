package com.example.skilltracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skilltracker.database.entity.*

// TODO: Add updates and deletes as needed
/**
 * adding suspend to functions that need to run asynchronously
 */
@Dao
interface SkillDao {

    /* QUERIES */
    /**
     * Return all SkillSet data from database
     * @return LiveData with list of SkillSets
     */
    @Query("SELECT * FROM SkillSet")
    fun getAll(): LiveData<List<SkillSet>>

    /**
     * Deletes all data in SkillSet table
     */
    @Query("DELETE FROM SkillSet")
    suspend fun nukeTable()

    /**
     * Return all Skills data from database
     * @return LiveData with list of Skills
     */
    @Query("SELECT * FROM Skill")
    fun getAllSkills(): LiveData<List<Skill>>

    /**
     * Return all Task data from database
     * @return LiveData with list of Tasks
     */
    @Query("SELECT * FROM Task")
    fun getAllTasks(): LiveData<List<Task>>

    /**
     * Return Join data between SkillSet and Skill tables from database
     * @return LiveData with list of SkillSet with related Skill
     */
    @Transaction
    @Query("SELECT * FROM SkillSet")
    fun getAllSkillSetWithSkills(): LiveData<List<SkillSetWithSkills>>

    /**
     * Return Join data between SkillSet and Skill tables from database with specific
     * skillSetId
     * @return LiveData with a SkillSet with List of Skills
     */
    @Transaction
    @Query("SELECT * FROM SkillSet WHERE skillSetId=:skillSetId")
    fun getSpecificSkillSetWithSkills(skillSetId: Long): LiveData<List<SkillSetWithSkills>>

    /**
     * Return Join data between Skill and Task tables from database
     * @return LiveData with list of Skill with related Tasks
     */
    @Transaction
    @Query("SELECT * FROM Skill")
    fun getAllSkillWithTasks(): LiveData<List<SkillWithTasks>>



    /* INSERTS */
    /**
     * Insert SkillSet data into database
     * @param skillSet SkillSets to be added to SkillSet table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg skillSet: SkillSet)

    /**
     * Insert Skill data into database
     * @param skill Skills to be added to Skill table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg skill: Skill)

    /**
     * Insert Task data into database
     * @param task Task to be added to Task table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: Task)

    /**
     * Insert join data between Skillset and Skill into database
     * @param join data be added to SkillSetSkillCrossRef table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg join: SkillSetSkillCrossRef)

    /**
     * Insert SkillSet data into database
     * @param join data to be added to SkillTaskCrossRef table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg join: SkillTaskCrossRef)

    /* DELETES */
    /**
     * Delete SkillSet data from database
     * @param skillSet SkillSets to be removed from SkillSet table
     */
    @Delete
    suspend fun delete(skillSet: SkillSet)

    /* UPDATES */
    /**
     * Update SkillSet data in database
     * @param skillSets SkillSet rows to be updated
     */
    @Update
    suspend fun update(vararg skillSets: SkillSet)

    @Update
    suspend fun updateSkill(vararg skills: Skill)

    @Update
    suspend fun updateTask(vararg task: Task)
}