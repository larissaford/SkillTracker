package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime


/**
 * Data class that has individual tasks a user wants to complete.
 * @property taskName user designated name of Task
 * @property taskDescription user designated description of Task
 * @property difficultyPoints user designated points for Task
 * @property taskCompleted completion state of Task
 * @property active user is currently working on this Task
 * @property taskDateCreated creation date of the Task
 * @property taskDateCompleted completion date of the Task
 */
@Parcelize
@Entity
data class Task(
    @ColumnInfo(name="task_name")
    var taskName: String,
    @ColumnInfo(name="task_description")
    var taskDescription: String,
    @ColumnInfo(name="task_difficulty_points")
    val difficultyPoints: Int,
    @ColumnInfo(name="task_completed")
    var taskCompleted: Boolean,
    @ColumnInfo(name="task_active")
    var active: Boolean,
    @ColumnInfo(name="task_created")
    val taskDateCreated: LocalDateTime,
    @ColumnInfo(name="task_date_completed")
    var taskDateCompleted: LocalDateTime?
)     : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L

    constructor(): this("add name", "add description", 0, false, true, LocalDateTime.now(), null)

    constructor(name: String, description: String, active: Boolean, difficultyPoints: Int): this(name, description, difficultyPoints, false, active, LocalDateTime.now(), null)
}