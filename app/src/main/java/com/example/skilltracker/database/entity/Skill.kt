package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

/**
 * Skill entity & data class
 *
 * @property skillName The name of the skill
 * @property completed Whether or not the skill has been completed
 * @property dateCreated The date the skill was created
 * @property dateCompleted The date the skill was completed
 */
@Parcelize
@Entity
data class Skill (
    var skillName: String,
    var completed: Boolean,
    var dateCreated: LocalDateTime,
    var dateCompleted: LocalDateTime?
): Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var skillId: Long = 0L

    constructor(): this("add name", false, LocalDateTime.now(), null)

    constructor(name: String, completed: Boolean): this(name, completed, LocalDateTime.now(), null)
}