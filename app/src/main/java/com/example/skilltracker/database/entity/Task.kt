package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

// TODO: Add dateCreated
/**
 * Data class that has individual tasks a user wants to complete.
 */
@Parcelize
@Entity
data class Task(
    @ColumnInfo(name="task_name")
    var taskName: String,
    @ColumnInfo(name="task_description")
    val taskDescription: String,
    @ColumnInfo(name="task_difficulty_points")
    val difficultyPoints: Int,
    @ColumnInfo(name="task_completed")
    var taskCompleted: Boolean,
    @ColumnInfo(name="task_active")
    val active: Boolean,
    @ColumnInfo(name="task_created")
    val taskDateCreated: LocalDateTime,
    @ColumnInfo(name="task_date_completed")
    var taskDateCompleted: LocalDateTime?
)     : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L

    constructor(): this("add name", "add description", 0, false, true, LocalDateTime.now(), null)

    constructor(name: String, description: String): this(name, description, 0, false, true, LocalDateTime.now(), null)
}