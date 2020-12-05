package com.example.skilltracker.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

// TODO: Add active flag here?
/**
 * Data class that will be returned by a join query of Skill and Task table.
 * Join will be in structure of a Skill with a List of Tasks related to it.
 * Relationship between table is established by the Junction of the CrossRef table entity.
 * @property skill the skill we want
 * @property tasks the list of tasks related to the current skill
 */
class SkillWithTasks(
    @Embedded val skill: Skill,
    @Relation(
        parentColumn = "skillId",
        entityColumn = "taskId",
        associateBy = Junction(SkillTaskCrossRef::class)
    )
    val tasks: List<Task>
)