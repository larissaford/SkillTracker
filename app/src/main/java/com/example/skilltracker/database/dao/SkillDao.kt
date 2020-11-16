package com.example.skilltracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.skilltracker.database.entity.SkillSet


/**
 * adding suspend to functions that need to run asynchronously
 */
@Dao
interface SkillDao {
    @Query("SELECT * FROM SkillSet")
    fun getAll(): LiveData<List<SkillSet>>

    @Query("DELETE FROM SkillSet")
    suspend fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg SkillSets: SkillSet)

    @Delete
    suspend fun delete(SkillSet: SkillSet)

    @Update
    suspend fun update(vararg SkillSets: SkillSet)
}