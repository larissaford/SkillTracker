package com.example.skilltracker.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Data class that will be returned by a join query of SkillSet and Skill table.
 * Join will be in structure of a Skill with a List of Tasks related to it.
 * Relationship between table is established by the Junction of the CrossRef table entity.
 * @property skillSet the skillset we want
 * @property skills the list of skills related to the current skillset
 */
data class SkillSetWithSkills (
    @Embedded val skillSet: SkillSet,
    @Relation(
        parentColumn = "skillSetId",
        entityColumn = "skillId",
        associateBy = Junction(SkillSetSkillCrossRef::class)
    )
    val skills: List<Skill>
)