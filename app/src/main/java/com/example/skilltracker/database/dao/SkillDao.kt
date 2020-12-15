package com.example.skilltracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skilltracker.database.entity.*

/**
 * Interface Room will implement to interact with the Database
 * Will run inserts, deletes and updates asynchronously with Coroutines
 */
@Dao
interface SkillDao {

    /* QUERIES */
    /**
     * Return all SkillSet data from database
     * @return LiveData with list of SkillSets
     */
    @Query("SELECT * FROM SkillSet")
    fun getAllSkillSets(): LiveData<List<SkillSet>>

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
     * @param skillSetId id of SkillSet to query
     * @return LiveData with a SkillSet with List of Skills
     */
    @Transaction
    @Query("SELECT * FROM SkillSet WHERE skillSetId=:skillSetId")
    fun getSpecificSkillSetWithSkills(skillSetId: Long): LiveData<List<SkillSetWithSkills>>

    /**
     * Return Join data between Skill and Task tables from database with specific skillId
     * @param skillId id of Skill to query
     * @return LiveData with a Skill with List of Tasks
     */
    @Transaction
    @Query("SELECT * FROM Skill WHERE skillId=:skillId")
    fun getSpecificSkillWithTasks(skillId: Long): LiveData<List<SkillWithTasks>>

    /**
     * Return all Join data between Skill and Task tables from database
     * @return LiveData with list of Skill with related Tasks
     */
    @Transaction
    @Query("SELECT * FROM Skill")
    fun getAllSkillWithTasks(): LiveData<List<SkillWithTasks>>

    /**
     * Return Skill rows from database with specific skillSetId
     * @param skillSetId id of SkillSet to query
     * @return LiveData with a SkillSet with List of Skills
     */
    @Transaction
    @Query("""SELECT * FROM Skill s 
                    JOIN SkillSetSkillCrossRef ssXRef ON ssXRef.skillId = s.skillId 
                    WHERE ssXRef.skillSetId = :skillSetId""")
    fun getSkillsFromJoin(skillSetId: Long): LiveData<List<Skill>>

    /**
     * Returns Join data between Skill and Tasks based on a specific SkillSet
     * @param skillSetId id of SkillSet to base query on
     * @return List of Skills with their Tasks
     */
    @Transaction
    @Query("""SELECT * FROM SKILL s
            JOIN SkillSetSkillCrossRef ssXRef ON ssXRef.skillId = s.skillId
            WHERE ssXRef.skillSetId = :skillSetId""")
    fun getAllSkillWithTasksForSpecificSkillSet(skillSetId: Long): LiveData<List<SkillWithTasks>>

    /* INSERTS */
    /**
     * Insert SkillSet data into database
     * @param skillSet SkillSets to be added to SkillSet table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg skillSet: SkillSet): List<Long>

    /**
     * Insert Skill data into database
     * @param skill Skills to be added to Skill table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg skill: Skill): List<Long>

    /**
     * Insert Task data into database
     * @param task Task to be added to Task table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg task: Task): List<Long>

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

    /**
     * Delete SkillSet data from database
     * @param skillSetSkillCrossRef The skill set skill join we want to delete
     */
    @Delete
    suspend fun deleteSkillSetSkillCrossRef(vararg skillSetSkillCrossRef: SkillSetSkillCrossRef)

    /**
     * Delete a task from a skill
     */
    @Delete
    suspend fun deleteSkillTaskCrossRef(vararg skillTaskCrossRef: SkillTaskCrossRef)

    /**
     * Deletes all data in SkillSet table
     */
    @Query("DELETE FROM SkillSet")
    suspend fun nukeSkillSetTable()

    /**
     * Deletes all data in Skill table
     */
    @Query("DELETE FROM Skill")
    suspend fun nukeSkillTable()

    /**
     * Deletes all data in Task table
     */
    @Query("DELETE FROM Task")
    suspend fun nukeTaskTable()

    /**
     * Deletes all data in SkillSetSkillCrossRef table
     */
    @Query("DELETE FROM SkillSetSkillCrossRef")
    suspend fun nukeSkillSetSkillCrossRefTable()

    /**
     * Deletes all data in SkillTaskCrossRef table
     */
    @Query("DELETE FROM SkillTaskCrossRef")
    suspend fun nukeSkillTaskCrossRefTable()

    /* UPDATES */
    /**
     * Update SkillSet data in database
     * @param skillSets SkillSet rows to be updated
     */
    @Update
    suspend fun update(vararg skillSets: SkillSet)

    /**
     * Update Skill data in database
     * @param skills Skill rows to be updated
     */
    @Update
    suspend fun updateSkill(vararg skills: Skill)

    /**
     * Update Task data in database
     * @param task Task rows to be updated
     */
    @Update
    suspend fun updateTask(vararg task: Task)
}