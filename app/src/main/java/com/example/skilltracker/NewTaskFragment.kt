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
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber

/**
 * Used to create a new Skill
 * @property binding The binding variable for this fragment
 * @property vm The view model for skills
 * @property task If not null, the task that is being edited
 * @property skill The skill that was clicked on to view the tasks
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

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_task, container, false
        )

        task = arguments?.let { NewTaskFragmentArgs.fromBundle(it).task }
        skill = arguments?.let { NewTaskFragmentArgs.fromBundle(it).skill }

        // If the task is not null, the user is editing an existing task
        if (task != null) {
            binding.createNewTaskButton.text = getString(R.string.update_task)
            binding.newTaskNameInput.setText(task!!.taskName)
            binding.taskCompletedCheckbox.isChecked = task!!.taskCompleted
            binding.taskActiveCheckbox.isChecked = task!!.active
            binding.newTaskDescriptionInput.setText(task!!.taskDescription)
            binding.difficultyPointsInput.setText(task!!.difficultyPoints.toString())

            binding.taskCompleted.visibility = View.VISIBLE
            binding.taskCompletedCheckbox.visibility = View.VISIBLE
            binding.cardfour.visibility = View.VISIBLE

            // If the task is completed, show the date it was completed on
            if (task!!.taskCompleted) {
                // Format date and set it to the taskDateCompletedOn text view's text
                val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                binding.taskDateCompletedOn.text = task!!.taskDateCompleted?.format(formatter).toString()
                binding.cardfive.visibility = View.VISIBLE
            }
        }

        // Set an onClickListener for the createNewTaskButton
        binding.createNewTaskButton.setOnClickListener {
            if (addNewTask()) {
                // Navigate back to the Task fragment
                val navController = this.findNavController()
                navController.navigateUp()

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
     * Adds a new task to the database or updates an existing task
     */
    private fun addNewTask(): Boolean {
        var name: String = binding.newTaskNameInput.text.toString()
        var description: String = binding.newTaskDescriptionInput.text.toString()
        val active: Boolean = binding.taskActiveCheckbox.isChecked
        val difficultyPointsAsString: String = binding.difficultyPointsInput.text.toString().trim()

        if (description.isBlank()) {
            description = ""
        }

        name = name.trim()
        description = description.trim()

        // Ensure a name & difficulty points were provided for the task
        if (name.isBlank()) {
            val toast = Toast.makeText(context, "Please give the  task a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newTaskMissingName.visibility = View.VISIBLE
            return false
        }
        else if (difficultyPointsAsString.isBlank()) {
            val toast = Toast.makeText(context, "Please give the task a difficulty point between 1 and 10", Toast.LENGTH_SHORT)
            toast.show()
            //binding.newTaskMissingName.visibility = View.VISIBLE
            return false
        }
        else {
            val difficultyPoints: Int = difficultyPointsAsString.toInt()

            if (difficultyPoints < 1 || difficultyPoints > 10) {
                val toast = Toast.makeText(context, "Please give the task a difficulty point between 1 and 10", Toast.LENGTH_SHORT)
                toast.show()
                //binding.newTaskMissingName.visibility = View.VISIBLE
                return false
            }
            else {
                binding.newTaskMissingName.visibility = View.INVISIBLE
                // If task is null, the user is adding a new task, otherwise they are updating an existing task
                if (task == null) {
                    GlobalScope.launch {
                        // Create a new Task, insert into DB, and get returned rowId
                        val newTask = Task(name, description, active, difficultyPoints)
                        val newTaskId = vm.insertTasks(newTask)

                        // Set returned rowId to Task's id and insert join
                        newTask.taskId = newTaskId
                        vm.insertNewTaskWithJoin(skill!!, newTask)
                    }
                }
                else {
                    // If the task was marked as completed & it wasn't before, set the dateCompleted
                    if (binding.taskCompletedCheckbox.isChecked && !task!!.taskCompleted) {
                        task!!.taskDateCompleted = LocalDateTime.now()
                        task!!.active = false
                    }

                    // If the task task is completed, ensure active is false, otherwise set active based on the checkbox's isChecked value
                    if (binding.taskCompletedCheckbox.isChecked) {
                        task!!.active = false
                    }
                    else {
                        task!!.active = active
                    }

                    task!!.taskName = name
                    task!!.taskDescription = description
                    task!!.taskCompleted = binding.taskCompletedCheckbox.isChecked
                    vm.updateTasks(task!!)
                }
                return true
            }
        }
    }
}