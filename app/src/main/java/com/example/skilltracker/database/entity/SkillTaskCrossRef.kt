package com.example.skilltracker.database.entity

import androidx.room.Entity

/**
 * Data class that sets up relationship between Skill and Task tables
 */
@Entity(primaryKeys = ["skillId", "taskId"])
data class SkillTaskCrossRef(
    val skillId: Long,
    val taskId: Long
)