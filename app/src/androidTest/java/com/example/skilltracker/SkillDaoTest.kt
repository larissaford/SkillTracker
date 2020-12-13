package com.example.skilltracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.skilltracker.database.SkillsDatabase
import com.example.skilltracker.database.dao.SkillDao
import com.example.skilltracker.database.entity.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SkillDaoTest {
    private lateinit var db: SkillsDatabase
    private lateinit var skillDao: SkillDao

    // Forces each test to execute synchronously
    @get:Rule
    var instantTaskExecutorRule:  InstantTaskExecutorRule = InstantTaskExecutorRule()

    // Creates an in-memory instance of the database & assign it to the db variable
    @Before
    @Throws(Exception::class)
    fun initDb() {
            this.db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context, SkillsDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        skillDao = db.skillDao()
    }

    // Closes the in-memory database
    @After
    @Throws(Exception::class)
    fun closeDb() {
        db.close()
    }

    /**
     * Tests inserting and retrieving 1 skill set
     */
    @Test
    fun insertAndGet1SkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        skillDao.insert(skillSet)
        assertEquals(skillSet.name, LiveDataTestUtil.getValue(skillDao.getAllSkillSets())!![0].name)
    }

    /**
     * Tests inserting and retrieving 3 skill sets
     */
    @Test
    fun insertAndGetMultipleSkillSets() = runBlocking {
        val skillSet1 = SkillSet("Skill set 1", "A skill set")
        val skillSet2 = SkillSet("Skill set 2", "A skill set")
        val skillSet3 = SkillSet("Skill set 3", "A skill set")

        skillDao.insert(skillSet1, skillSet2, skillSet3)
        val skillSets = LiveDataTestUtil.getValue(skillDao.getAllSkillSets())

        assertEquals(3, skillSets!!.size)
        assertEquals(skillSet1.name, skillSets[0].name)
        assertEquals(skillSet2.name, skillSets[1].name)
        assertEquals(skillSet3.name, skillSets[2].name)
    }

    /**
     * Tests inserting and retrieving 1 skill
     */
    @Test
    fun insertAndGet1Skill() = runBlocking {
        val skill = Skill("Skill 1", false)
        skillDao.insert(skill)
        assertEquals(skill.skillName, LiveDataTestUtil.getValue(skillDao.getAllSkills())!![0].skillName)
    }

    /**
     * Tests inserting and retrieving 3 skills
     */
    @Test
    fun insertAndGetMultipleSkills() = runBlocking {
        val skill1 = Skill("Skill 1", false)
        val skill2 = Skill("Skill 2", true)
        val skill3 = Skill("Skill 3", false)

        skillDao.insert(skill1, skill2, skill3)
        val skills = LiveDataTestUtil.getValue(skillDao.getAllSkills())

        assertEquals(skill1.skillName, skills!![0].skillName)
        assertEquals(skill2.completed, skills[1].completed)
        assertEquals(skill3.skillName, skills[2].skillName)
    }

    /**
     * Tests inserting and retrieving 1 task
     */
    @Test
    fun insertAndGet1Task() = runBlocking {
        val task = Task("Task", "Description")
        skillDao.insert(task)
        assertEquals(task.taskName, LiveDataTestUtil.getValue(skillDao.getAllTasks())!![0].taskName)
    }

    /**
     * Tests inserting and retrieving 3 tasks
     */
    @Test
    fun insertAndGetMultipleTasks() = runBlocking {
        val task1 = Task("Task 1", "Description")
        val task2 = Task("Task 2", "Description")
        val task3 = Task("Task 3", "Description")

        skillDao.insert(task1, task2, task3)
        val tasks = LiveDataTestUtil.getValue(skillDao.getAllTasks())

        assertEquals(task1.taskName, tasks!![0].taskName)
        assertEquals(task2.taskName, tasks[1].taskName)
        assertEquals(task3.taskName, tasks[2].taskName)
    }

    /**
     * Tests inserting & retrieving a skill as part of a skill set
     */
    @Test
    fun insertAndGetSkillWithSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill = Skill("Skill 1", false)

        val skillSetId: Long = skillDao.insert(skillSet)[0]
        val skillId: Long = skillDao.insert(skill)[0]

        val join = SkillSetSkillCrossRef(skillSetId, skillId)
        skillDao.insert(join)

        val skillWithSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSetWithSkills())!![0]

        assertEquals(skillSet.name, skillWithSkillSet.skillSet.name)
        assertEquals(skill.skillName, skillWithSkillSet.skills[0].skillName)
    }

    /**
     * Tests inserting & retrieving 3 skills as part of 1 skill set
     */
    @Test
    fun insertAndGetMultipleSkillsWithSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill1 = Skill("Skill 1", false)
        val skill2 = Skill("Skill 2", true)
        val skill3 = Skill("Skill 3", false)

        val skillSetId = skillDao.insert(skillSet)[0]
        val skillIds = skillDao.insert(skill1, skill2, skill3)

        val join = Array(skillIds.size) {
            SkillSetSkillCrossRef(
                skillSetId,
                skillIds[it]
            )
        }

        skillDao.insert(*join)
        val skillWithSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSetWithSkills())!![0]

        assertEquals(skillSet.name, skillWithSkillSet.skillSet.name)
        assertEquals(skill1.skillName, skillWithSkillSet.skills[0].skillName)
        assertEquals(skill2.skillName, skillWithSkillSet.skills[1].skillName)
        assertEquals(skill3.skillName, skillWithSkillSet.skills[2].skillName)
    }

    /**
     * Tests inserting & retrieving a task as part of a skill
     */
    @Test
    fun insertAndGet1TaskWithSkill() = runBlocking {
        val skill = Skill("Skill 1", false)
        val task = Task("Task", "Description")

        val skillId = skillDao.insert(skill)[0]
        val taskId = skillDao.insert(task)[0]

        val join = SkillTaskCrossRef(skillId, taskId)
        skillDao.insert(join)

        val taskWithSkill = LiveDataTestUtil.getValue(skillDao.getAllSkillWithTasks())!![0]

        assertEquals(skill.skillName, taskWithSkill.skill.skillName)
        assertEquals(task.taskName, taskWithSkill.tasks[0].taskName)
    }

    /**
     * Tests inserting & retrieving 3 tasks as part of 1 skill
     */
    @Test
    fun insertAndGetMultipleTasksWithSkill() = runBlocking {
        val skill = Skill("Skill 1", false)
        val task1 = Task("Task 1", "Description")
        val task2 = Task("Task 2", "Description")
        val task3 = Task("Task 3", "Description")

        val skillId = skillDao.insert(skill)[0]
        val taskIds = skillDao.insert(task1, task2, task3)

        val join = Array(taskIds.size) {
            SkillTaskCrossRef(
                skillId,
                taskIds[it]
            )
        }

        skillDao.insert(*join)
        val tasksWithSkill = LiveDataTestUtil.getValue(skillDao.getAllSkillWithTasks())!![0]

        assertEquals(skill.skillName, tasksWithSkill.skill.skillName)
        assertEquals(task1.taskName, tasksWithSkill.tasks[0].taskName)
        assertEquals(task2.taskName, tasksWithSkill.tasks[1].taskName)
        assertEquals(task3.taskName, tasksWithSkill.tasks[2].taskName)
    }

    /**
     * Tests getting a skill set and all of its skills
     */
    @Test
    fun getSpecificSkillSetWithSkills() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill1 = Skill("Skill 1", false)
        val skill2 = Skill("Skill 2", true)
        val skill3 = Skill("Skill 3", false)

        val skillSetId = skillDao.insert(skillSet)[0]
        val skillIds = skillDao.insert(skill1, skill2, skill3)

        val join = Array(skillIds.size) {
            SkillSetSkillCrossRef(
                skillSetId,
                skillIds[it]
            )
        }

        skillDao.insert(*join)
        val skillWithSkillSet = LiveDataTestUtil.getValue(skillDao.getSpecificSkillSetWithSkills(skillSetId))!![0]

        assertEquals(skillSet.name, skillWithSkillSet.skillSet.name)
        assertEquals(skillIds.size, skillWithSkillSet.skills.size)
    }

    /**
     * Tests getting a skill and all of its tasks
     */
    @Test
    fun getSpecificSkillWithTasks() = runBlocking {
        val skill = Skill("Skill 1", false)
        val task1 = Task("Task 1", "Description")
        val task2 = Task("Task 2", "Description")
        val task3 = Task("Task 3", "Description")

        val skillId = skillDao.insert(skill)[0]
        val taskIds = skillDao.insert(task1, task2, task3)

        val join = Array(taskIds.size) {
            SkillTaskCrossRef(
                skillId,
                taskIds[it]
            )
        }

        skillDao.insert(*join)
        val tasksWithSkill = LiveDataTestUtil.getValue(skillDao.getAllSkillWithTasks())!![0]

        assertEquals(skill.skillName, tasksWithSkill.skill.skillName)
        assertEquals(taskIds.size, tasksWithSkill.tasks.size)
    }

    /**
     * Tests getting all of the skills for a skill set
     */
    @Test
    fun getSkillsFromJoin() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill1 = Skill("Skill 1", false)
        val skill2 = Skill("Skill 2", true)
        val skill3 = Skill("Skill 3", false)

        val skillSetId = skillDao.insert(skillSet)[0]
        val skillIds = skillDao.insert(skill1, skill2, skill3)

        val join = Array(skillIds.size) {
            SkillSetSkillCrossRef(
                skillSetId,
                skillIds[it]
            )
        }

        skillDao.insert(*join)
        val skills = LiveDataTestUtil.getValue(skillDao.getSkillsFromJoin(skillSetId))

        assertEquals(skillIds.size, skills!!.size)
    }

    /**
     * Tests inserting & retrieving tasks that are with skills that are with a skill set.
     */
    @Test
    fun getAllSkillWithTasksForSpecificSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill1 = Skill("Skill 1", false)
        val skill2 = Skill("Skill 2", true)
        val skill3 = Skill("Skill 3", false)

        val skillSetId = skillDao.insert(skillSet)[0]
        val skillIds = skillDao.insert(skill1, skill2, skill3)

        val join1 = Array(skillIds.size) {
            SkillSetSkillCrossRef(
                skillSetId,
                skillIds[it]
            )
        }

        skillDao.insert(*join1)
        val skills = LiveDataTestUtil.getValue(skillDao.getSkillsFromJoin(skillSetId))

        val task1 = Task("Task 1", "Description")
        val task2 = Task("Task 2", "Description")
        val task3 = Task("Task 3", "Description")

        val taskIds = skillDao.insert(task1, task2, task3)

        val join2 = SkillTaskCrossRef(skills!![0].skillId, taskIds[0])
        val join3 = SkillTaskCrossRef(skills[1].skillId, taskIds[1])
        val join4 = SkillTaskCrossRef(skills[2].skillId, taskIds[2])

        skillDao.insert(join2, join3, join4)
        val tasksWithSkill = LiveDataTestUtil.getValue(skillDao.getAllSkillWithTasksForSpecificSkillSet(skillSetId))

        assertEquals(taskIds.size, tasksWithSkill!!.size)
        assertEquals(skill1.skillName, tasksWithSkill[0].skill.skillName)
        assertEquals(task1.taskName, tasksWithSkill[0].tasks[0].taskName)
        assertEquals(skill2.skillName, tasksWithSkill[1].skill.skillName)
        assertEquals(task2.taskName, tasksWithSkill[1].tasks[0].taskName)
        assertEquals(skill3.skillName, tasksWithSkill[2].skill.skillName)
        assertEquals(task3.taskName, tasksWithSkill[2].tasks[0].taskName)
    }

    /**
     * Tests updating a skill set
     */
    @Test
    fun updateSkillSet_ValidSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skillSetId = skillDao.insert(skillSet)[0]
        assertEquals(skillSet.name, LiveDataTestUtil.getValue(skillDao.getAllSkillSets())!![0].name)

        skillSet.skillSetId = skillSetId
        skillSet.name = "New Name"
        skillSet.description = "New"

        skillDao.update(skillSet)

        val updatedSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSets())!![0]

        assertEquals(skillSet.name, updatedSkillSet.name)
        assertEquals(skillSet.description, updatedSkillSet.description)
    }

    /**
     * Tests trying to update a skill set that does not exist
     */
    @Test
    fun updateSkillSet_InvalidSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        skillDao.insert(skillSet)
        assertEquals(skillSet.name, LiveDataTestUtil.getValue(skillDao.getAllSkillSets())!![0].name)

        skillSet.name = "New Name"
        skillSet.description = "New"

        skillDao.update(skillSet)

        val updatedSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSets())!![0]

        assertNotEquals(skillSet.name, updatedSkillSet.name)
        assertNotEquals(skillSet.description, updatedSkillSet.description)
    }

    /**
     * Tests updating a skill
     */
    @Test
    fun updateSkill_ValidSkill() = runBlocking {
        val skill = Skill("Skill Name", false)
        val skillId = skillDao.insert(skill)[0]
        assertEquals(skill.skillName, LiveDataTestUtil.getValue(skillDao.getAllSkills())!![0].skillName)

        skill.skillId = skillId
        skill.skillName = "New Name"
        skill.completed = true

        skillDao.updateSkill(skill)

        val updatedSkill = LiveDataTestUtil.getValue(skillDao.getAllSkills())!![0]

        assertEquals(skill.skillName, updatedSkill.skillName)
        assertEquals(skill.completed, updatedSkill.completed)
    }

    /**
     * Tests trying to update a skill that does not exist
     */
    @Test
    fun updateSkill_InvalidSkill() = runBlocking {
        val skill = Skill("Skill Name", false)
        skillDao.insert(skill)[0]
        assertEquals(skill.skillName, LiveDataTestUtil.getValue(skillDao.getAllSkills())!![0].skillName)

        skill.skillName = "New Name"
        skill.completed = true

        skillDao.updateSkill(skill)

        val updatedSkill = LiveDataTestUtil.getValue(skillDao.getAllSkills())!![0]

        assertNotEquals(skill.skillName, updatedSkill.skillName)
        assertNotEquals(skill.completed, updatedSkill.completed)
    }

    /**
     * Tests updating a task
     */
    @Test
    fun updateTask_ValidTask() = runBlocking {
        val task = Task("Task Name", "description")
        val taskId = skillDao.insert(task)[0]
        assertEquals(task.taskName, LiveDataTestUtil.getValue(skillDao.getAllTasks())!![0].taskName)

        task.taskId = taskId
        task.taskName = "New Name"
        task.taskCompleted = true

        skillDao.updateTask(task)

        val updatedTask = LiveDataTestUtil.getValue(skillDao.getAllTasks())!![0]

        assertEquals(task.taskName, updatedTask.taskName)
        assertEquals(task.taskCompleted, updatedTask.taskCompleted)
    }

    /**
     * Tests trying to update a task that does not exist
     */
    @Test
    fun updateTask_InvalidTask() = runBlocking {
        val task = Task("Task Name", "description")
        skillDao.insert(task)[0]
        assertEquals(task.taskName, LiveDataTestUtil.getValue(skillDao.getAllTasks())!![0].taskName)

        task.taskName = "New Name"
        task.taskCompleted = true

        skillDao.updateTask(task)

        val updatedTask = LiveDataTestUtil.getValue(skillDao.getAllTasks())!![0]

        assertNotEquals(task.taskName, updatedTask.taskName)
        assertNotEquals(task.taskCompleted, updatedTask.taskCompleted)
    }

    /**
     * Tests deleting a skill set
     */
    @Test
    fun deleteSkillSet_ValidSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skillSetId = skillDao.insert(skillSet)[0]

        skillSet.skillSetId = skillSetId
        skillDao.delete(skillSet)

        val skillSets = LiveDataTestUtil.getValue(skillDao.getAllSkillSets())
        assertEquals(0, skillSets!!.size)
    }

    /**
     * Tests trying to delete a skill set that does not exist
     */
    @Test
    fun deleteSkillSet_InvalidSkillSet() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        skillDao.insert(skillSet)[0]
        skillDao.delete(skillSet)

        val skillSets = LiveDataTestUtil.getValue(skillDao.getAllSkillSets())

        assertEquals(1, skillSets!!.size)
    }

    /**
     * Tests deleting a SkillSetSkillCrossRef
     */
    @Test
    fun deleteSkillSetSkillCrossRef_ValidSkillSetSkillCrossRef() = runBlocking {
        val skillSet = SkillSet("Skill set 111", "A skill set")
        val skill = Skill("Skill 1", false)

        val skillSetId: Long = skillDao.insert(skillSet)[0]
        val skillId: Long = skillDao.insert(skill)[0]

        val join = SkillSetSkillCrossRef(skillSetId, skillId)
        skillDao.insert(join)

        val skillWithSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSetWithSkills())

        assertEquals(skillSet.name, skillWithSkillSet!![0].skillSet.name)
        assertEquals(skill.skillName, skillWithSkillSet[0].skills[0].skillName)
        assertEquals(1, skillWithSkillSet!!.size)

        skillDao.deleteSkillSetSkillCrossRef(join)

        val skillsWithSkillSet = LiveDataTestUtil.getValue(skillDao.getSpecificSkillSetWithSkills(skillSetId))

        assertEquals(0, skillsWithSkillSet!!.size)
    }

    /**
     * Tests trying to delete a SkillSetSkillCrossRef that does not exist
     */
    @Test
    fun deleteSkillSetSkillCrossRef_InvalidSkillSetSkillCrossRef() = runBlocking {
        val skillSet = SkillSet("Skill set", "A skill set")
        val skill = Skill("Skill 1", false)

        val skillSetId: Long = skillDao.insert(skillSet)[0]
        val skillId: Long = skillDao.insert(skill)[0]

        var join = SkillSetSkillCrossRef(skillSetId, skillId)
        skillDao.insert(join)

        val skillWithSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSetWithSkills())!![0]

        assertEquals(skillSet.name, skillWithSkillSet.skillSet.name)
        assertEquals(skill.skillName, skillWithSkillSet.skills[0].skillName)

        join = SkillSetSkillCrossRef(2, skillId)

        skillDao.deleteSkillSetSkillCrossRef(join)

        val skillsWithSkillSet = LiveDataTestUtil.getValue(skillDao.getAllSkillSetWithSkills())

        assertEquals(1, skillsWithSkillSet!!.size)
    }
}