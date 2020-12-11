package com.example.skilltracker.database.entity

import androidx.room.Entity

/**
 * Data class that sets up relationship between the SkillSet and Skill tables
 * @property skillSetId id of SkillSet for relationship
 * @property skillId id of Skill for relationship
 */
@Entity(primaryKeys = ["skillSetId", "skillId"])
data class SkillSetSkillCrossRef (
    val skillSetId: Long,
    val skillId: Long
)