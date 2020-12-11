package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

/**
 * Class representing a Skillset, which will be related to a subset of Skills
 * @property name user designated name of SkillSet
 * @property description user designated of SkillSet
 * @property dateCreated date of creation of SkillSet
 * @property skillSetId id of SkillSet within the database table
 */
@Parcelize
@Entity
data class SkillSet(
    var name: String,
    var description: String,
    var dateCreated: LocalDateTime
//    var dateCompleted: LocalDate?
): Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var skillSetId: Long = 0L

    constructor(): this("add name", "add description", LocalDateTime.now())

    constructor(name: String, description: String): this(name, description, LocalDateTime.now())
}