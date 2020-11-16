package com.example.skilltracker.database.entity

import androidx.room.Entity

/**
 * Data class that sets up relationship between the SkillSet and Skill tables
 */
@Entity(primaryKeys = ["skillSetId, skillId"])
data class SkillSetSkillCrossRef (
    val skillSetId: Long,
    val skillId: Long
)