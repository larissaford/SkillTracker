package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.Task
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewTaskBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

/**
 * Used to create a new Skill
 * @property binding The binding variable for this fragment
 * @property vm The view model for skills
 */
class NewTaskFragment : Fragment() {
    private lateinit var binding: FragmentNewTaskBinding
    private lateinit var vm: SkillsViewModel

    private var task: Task? = null
    private var skill: Skill? = null

    /**
     * Inflates the layout for this fragment and sets an onClickListener for the createNewSkillSet button
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The view of the fragment's UI or null
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Set the fab visibility to false so it does not display while the user is creating a new skill set
        (activity as MainActivity).hideFAB()

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container, false
        )

        task = arguments?.let { NewTaskFragmentArgs.fromBundle(it).task }
        skill = arguments?.let { NewTaskFragmentArgs.fromBundle(it).skill }

        // If the skill is not null, the user is editing an existing skill
        if (task != null) {
            binding.newTaskNameInput.setText(task!!.taskName)
            binding.taskCompleted.visibility = View.VISIBLE
            binding.taskCompletedCheckbox.visibility = View.VISIBLE
            binding.taskCompletedCheckbox.isChecked = task!!.taskCompleted
            binding.createNewTaskButton.text = getString(R.string.create_task)

            // If the skill is completed, show the date it was completed on
            if (task!!.taskCompleted) {
                binding.taskCompletedOn.visibility = View.VISIBLE
                binding.taskDateCompletedOn.visibility = View.VISIBLE
                binding.taskDateCompletedOn.text = task!!.taskDateCompleted?.toLocalDate().toString()
            }
        }

        binding.createNewTaskButton.setOnClickListener {
            if (addNewTask()) {
                // Navigate back to the SkillSet fragment
                val navController = this.findNavController()
                navController.navigateUp()

                // Ensure the FAB is visible
                (activity as MainActivity).showFAB()

                // Hide the user's keyboard
                (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
            }
        }

        return binding.root
    }

    /**
     * Initializes the view model variable vm
     *
     * @param view The view that was created
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The view of the fragment's UI or null
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
    }

    /**
     * Adds a new skill to the database or updates an existing skill
     */
    private fun addNewTask(): Boolean {
        val name: String = binding.newTaskNameInput.text.toString()

        // Ensure a name was provided for the skill
        if (name == null || name == "") {
            val toast = Toast.makeText(context, "Please give the new skill a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newTaskMissingName.visibility = View.VISIBLE
            return false
        }
        else {
            binding.newTaskMissingName.visibility = View.INVISIBLE
            // If task is null, the user is adding a new task, otherwise they are updating an existing task
            if (task == null) {
                GlobalScope.launch {
                    // Create a new Task, insert into DB, and get returned rowId
                    var newTask = Task(name, "Description")
                    var newTaskId = vm.insertTasks(newTask)

                    // Set returned rowId to Task's id and insert join
                    newTask.taskId = newTaskId
                    vm.insertNewTaskWithJoin(skill!!, newTask)
                }
//                var result = vm.insertSkill(Skill(name,false))
//                println("RESULT: $result")
                //vm.insertNewSkillWithJoin(skillSet!!, Skill(name, false))
//                var newSkill = Skill(name, false)
//                var newSkillId = vm.insertSkill(newSkill)
//                newSkill.skillId = newSkillId
//
//                val skillList = listOf<Skill>(newSkill)
//                val skillSetWithSkill = SkillSetWithSkills(skillSet!!, skillList)
//                vm.insertSkillSetWithSkills(skillSetWithSkill) // insert join
////                var newSkillId = vm.insertSkill(Skill(name, false))
//
//                val skillList = listOf<Skill>(newSkill)
//                val skillSetWithSkill = SkillSetWithSkills(skillSet!!, skillList)
//                vm.insertSkillSetWithSkills(skillSetWithSkill) // insert join
            }
            else {
                // If the skill was marked as completed, set the dateCompleted
                if (binding.taskCompletedCheckbox.isChecked && !skill!!.completed) {
                    task!!.taskDateCompleted = LocalDateTime.now()
                }

                task!!.taskName = name
                task!!.taskCompleted = binding.taskCompletedCheckbox.isChecked
                vm.updateTasks(task!!)
                print("updated task")
            }
            return true
        }
    }
}