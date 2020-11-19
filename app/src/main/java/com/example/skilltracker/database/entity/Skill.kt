package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import org.threeten.bp.LocalDateTime

// TODO: Add parcelable as needed later
@Entity
data class Skill (
    @PrimaryKey(autoGenerate = true)
    val skillId: Long = 0L,
    @ColumnInfo(name="skill_name")
    val skillName: String,
    @ColumnInfo(name="skill_completed")
    val completed: Boolean,
    @ColumnInfo(name="skill_date_created")
    val dateCreated: LocalDateTime,
    @ColumnInfo(name="skill_date_completed")
    val dateCompleted: LocalDateTime
)
//    : Parcelable {
//    @IgnoredOnParcel
//    @PrimaryKey(autoGenerate = true)
//    var skillSetId: Long = 0L
//
//    constructor(): this("add name", "add description", LocalDateTime.now())
//
//    constructor(name: String, description: String): this(name, description, LocalDateTime.now())
//}