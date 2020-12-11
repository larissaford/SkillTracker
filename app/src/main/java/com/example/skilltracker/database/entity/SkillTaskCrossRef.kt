package com.example.skilltracker.database.entity

import androidx.room.Entity

/**
 * Data class that sets up relationship between Skill and Task tables
 * @property skillId id of Skill for relationship
 * @property taskId id of Task for relationship
 */
@Entity(primaryKeys = ["skillId", "taskId"])
data class SkillTaskCrossRef(
    val skillId: Long,
    val taskId: Long
)