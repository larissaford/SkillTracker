package com.example.skilltracker.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that has individual tasks a user wants to complete.
 */
@Entity
data class Task (
    @PrimaryKey
    val taskId: Long?,
    @ColumnInfo(name="taks_name")
    val taskName: String,
    @ColumnInfo(name="task_description")
    val taskDescription: String,
    @ColumnInfo(name="task_difficulty_points")
    val difficultyPoints: Int,
    @ColumnInfo(name="task_completed")
    val taskCompleted: Boolean,
    @ColumnInfo(name="task_active")
    val active: Boolean
)