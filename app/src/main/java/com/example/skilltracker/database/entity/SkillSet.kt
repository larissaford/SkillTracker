package com.example.skilltracker.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

/**
 * @property name of SkillSet
 * @property description of SkillSet
 * @property dateCreated of SkillSet
 */


@Parcelize
@Entity
data class SkillSet(
    var name: String,
    var description: String,
    var dateCreated: LocalDateTime
): Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(): this("add name", "add description", LocalDateTime.now())

    constructor(name: String, description: String): this(name, description, LocalDateTime.now())
}