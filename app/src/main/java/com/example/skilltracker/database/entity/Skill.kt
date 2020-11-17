package com.example.skilltracker.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class Skill (
    @PrimaryKey
    val skillId: Long?,
    @ColumnInfo(name="skill_name")
    val skillName: String,
    @ColumnInfo(name="skill_completed")
    val completed: Boolean,
    @ColumnInfo(name="skill_date_created")
    val dateCreated: LocalDateTime,
    @ColumnInfo(name="skill_date_completed")
    val dateCompleted: LocalDateTime
)