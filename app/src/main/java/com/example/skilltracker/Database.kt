package com.example.skilltracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.launch
import org.threeten.bp.*
import java.util.*

/**
 * adding suspend to functions that need to run asynchronously
 */
@Dao
interface SkillDao {
    @Query("SELECT * FROM SkillSet")
    fun getAll(): LiveData<List<SkillSet>>

    @Query("DELETE FROM SkillSet")
    suspend fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg SkillSets: SkillSet)

    @Delete
    suspend fun delete(SkillSet: SkillSet)

    @Update
    suspend fun update(vararg SkillSets: SkillSet)
}

/**
 * adding the synchronized keyword to the database makes it work with Coroutines
 */
@Database(entities = [SkillSet::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SkillsDatabase : RoomDatabase() {
    abstract fun orderDao(): SkillDao

    companion object {
        @Volatile
        private var INSTANCE: SkillsDatabase? = null

        fun getInstance(context: Application): SkillsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(SkillsDatabase::class){
                val instance = Room.databaseBuilder(
                    context.applicationContext, SkillsDatabase::class.java,
                    "skills_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}

/**
 * I'm using org.threeten.bp.* for my localDateTime class because Java.time wants a higher base API
 * than I have. This means I have to use LocalDateTime instead of the Date class
 *
 */
class DateConverter {
    @TypeConverter
    fun dateToLong(date: LocalDateTime?): Long? {
        var dateTime = date?.plusDays(10L)
        dateTime = dateTime?.plusHours(10L)
        dateTime = dateTime?.plusMinutes(10L)

        val zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault())
        return zonedDateTime.toInstant().toEpochMilli()
    }
    @TypeConverter
    fun longToDate(timestamp: Long?) : LocalDateTime? {
        return LocalDateTime.ofInstant(
            timestamp?.let { Instant.ofEpochMilli(it) },
            DateTimeUtils.toZoneId(TimeZone.getDefault())
        )
    }
}

/**
 * uses Coroutines seen by the "suspend" keyword
 *
 * uses Live data because it handles synchronization issues (doesn't have to be requeiried over and
 * over)
 */
class SkillsRepository(app: Application){
    private var allSkillSets: LiveData<List<SkillSet>>
    private var skillsDao: SkillDao

    init{
        val database: SkillsDatabase = SkillsDatabase.getInstance(app)
        skillsDao = database.orderDao()
        allSkillSets = skillsDao.getAll()
    }

    fun getSkillSet(): LiveData<List<SkillSet>>{
        return allSkillSets
    }

    suspend fun insertSkillSet(SkillSet: SkillSet){
        skillsDao.insert(SkillSet)
    }

    suspend fun nukeTable() = skillsDao.nukeTable()
}

/**
 * Uses Coroutines seen by the viewModelScope.launch function
 */
class SkillsViewModel(app: Application): AndroidViewModel(app) {
    private val repository: SkillsRepository = SkillsRepository(app)
    private val allSkillSetes : LiveData<List<SkillSet>> = repository.getSkillSet()

    /**
     * get all SkillSetes from database
     */
    fun getSkillSet(): LiveData<List<SkillSet>>{
        return allSkillSetes
    }

    /**
     * gets rid of all values in database
     */
    fun nuke() = viewModelScope.launch{
        repository.nukeTable()
    }

    /**
     * adds SkillSet to database
     */
    fun insertSkillSet(SkillSet: SkillSet) = viewModelScope.launch {
        repository.insertSkillSet(SkillSet)
    }
}