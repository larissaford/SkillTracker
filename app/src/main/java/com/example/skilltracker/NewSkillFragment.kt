package com.example.skilltracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.*
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime

/**
 * Used to create a new Skill
 * @property binding The binding variable for this fragment
 * @property vm The view model for skills
 * @property spinner A custom multi-selection spinner to display all skills
 * @property tasksListView A list view that contains all of the skills already added to the skill set
 * @property adapter An ArrayAdapter for the tasksListView
 * @property skillName The name of the skill
 * @property skillSet The SkillSet that was clicked on to view/add skills
 * @property skill The skill that is being edited
 * @property allTasks A list of all of the tasks currently stored in the database
 * @property currentTasks A list of all of the tasks that are part of the skill initially
 * @property currentTaskNames A list of all the names of the tasks that are part of the skill initially
 */
@Suppress("UNCHECKED_CAST")
class NewSkillFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillBinding
    private lateinit var vm: SkillsViewModel
    private lateinit var spinner: MultiSelectionSpinner
    private lateinit var tasksListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var skillName: String

    private var skillSet: SkillSet? = null
    private var skill: Skill? = null
    private var allTasks: ArrayList<Task> = ArrayList()
    private var currentTasks: ArrayList<Task> = ArrayList()
    private var currentTaskNames: ArrayList<String?> = ArrayList()

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
            inflater, R.layout.fragment_new_skill, container, false
        )

        // Get the skill set and skill (if editing) from the passed in arguments
        skillSet = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skillSet }
        skill = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skill }

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        spinner = binding.taskMultiSelectList

        // Get all of the tasks stored in the database & set them to the multi-select spinner's options
        vm.getTasks().observe(viewLifecycleOwner, { tasks ->
            allTasks = tasks as ArrayList<Task>
            spinner.setItems(allTasks as ArrayList<Any>)
        })

        // If the skill is not null, the user is editing an existing skill. Otherwise they are making a new skill
        if (skill != null) {
            // Get all of the tasks already added to the skill
            vm.getSpecificSkillWithTasks(skill!!.skillId).observe(viewLifecycleOwner, { tasksFromJoin: List<SkillWithTasks> ->
                currentTasks = tasksFromJoin[0].tasks as ArrayList<Task>

                // Make the already added tasks selected initially in the multi-select spinner
                spinner.setSelection(currentTasks as ArrayList<Any>)

                for (i in 0 until currentTasks.size) {
                    currentTaskNames.add(currentTasks[i].taskName)
                }

                // Put the already added tasks in the tasksListView
                tasksListView = binding.currentTasksListView
                adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, currentTaskNames)
                tasksListView.adapter = adapter
            })

            // Make the tasksListView card visible
            binding.cardThreeFragmentNewSkill.visibility = View.VISIBLE

            // Set the skill's current information to the proper input boxes
            binding.createNewSkillButton.text = getString(R.string.update_skill)
            binding.newSkillNameInput.setText(skill!!.skillName)
            binding.createNewSkillButton.text = getString(R.string.update_skill)
        } // end if (skill != null)

        // Create an onClickListener for the createNewSkillButton
        binding.createNewSkillButton.setOnClickListener { view: View ->
            // Ensure the name is valid before creating/updating the skill
            if (isValidName()) {
                // Get any of the selected tasks from the multi-select spinner & add them to the skill
                val selectedTasks: ArrayList<Task> = spinner.getSelectedItems() as ArrayList<Task>

                // Create/update the skill with any selected tasks and store them in the database
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        if (skill == null) {
                            skill = Skill(skillName, false)
                            val newSkillId = vm.insertSkill(skill!!)
                            skill!!.skillId = newSkillId

                            vm.insertNewSkillWithJoin(skillSet!!, skill!!)

                            // Add any selected tasks from the multi-select spinner
                            for (task in selectedTasks) {
                                vm.insertNewTaskWithJoin(skill!!, task)
                            }
                        }
                        else {
                            // The user is editing a skill, so remove any tasks from the skill that they un-selected in the multi-select spinner
                            if (spinner.selectionChanged) {
                                for (task in currentTasks) {
                                    if (selectedTasks.indexOf(task) == -1) {
                                        vm.deleteSkillTaskCrossRef(skill!!, task)
                                    }
                                }

                                // Add any tasks that were selected in the multi-select spinner that were not selected previously
                                for (task in selectedTasks) {
                                    if (currentTasks.indexOf(task) == -1) {
                                        vm.insertNewTaskWithJoin(skill!!, task)
                                    }
                                }
                            } // end if(spinner.selectionChanged)

                            // Update the skills information
                            skill!!.skillName = skillName
                            vm.updateSkill(skill!!)
                        } // end if(skill == null) else statement

                        // Navigate back the skill fragment
                        withContext(Dispatchers.Main) {
                            val navController = view.findNavController()
                            navController.navigateUp()
                        }
                    }
                } // end GlobalScope.launch Coroutine

                // Hide the user's keyboard
                (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
            } // end if (isValidName())
        } // end setOnClickListener for createNewSkillButton

        // Create an onClickListener for the addNewTasksButton
        binding.addNewTasksButton.setOnClickListener { view: View ->
            // Ensure the name is valid before creating/updating the skill
            if (isValidName()) {
                // Get any of the selected tasks from the multi-select spinner & add them to the skill
                val selectedTasks: ArrayList<Task> = spinner.getSelectedItems() as ArrayList<Task>

                if (skill == null) {
                    // Create the skill, add any tasks, and add them to the database
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            skill = Skill(skillName, false)
                            val newSkillId = vm.insertSkill(skill!!)
                            skill!!.skillId = newSkillId

                            vm.insertNewSkillWithJoin(skillSet!!, skill!!)

                            // Add any selected tasks from the multi-select spinner
                            for (task in selectedTasks) {
                                vm.insertNewTaskWithJoin(skill!!, task)
                            }

                            // Navigate to the task fragment
                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillFragmentDirections.actionNewSkillFragmentToTaskFragment(skill!!, skill!!.skillName))
                            }
                        }
                    } // end GlobalScope.Launch Coroutine
                }
                else {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            // The user is editing a skill, so remove any tasks from the skill that they un-selected in the multi-select spinner
                            if (spinner.selectionChanged) {
                                for (task in currentTasks) {
                                    if (selectedTasks.indexOf(task) == -1) {
                                        vm.deleteSkillTaskCrossRef(skill!!, task)
                                    }
                                }

                                // Add any tasks that were selected in the multi-select spinner that were not selected previously
                                for (task in selectedTasks) {
                                    if (currentTasks.indexOf(task) == -1) {
                                        vm.insertNewTaskWithJoin(skill!!, task)
                                    }
                                }
                            } // end if(spinner.selectionChanged)

                            // Update the skills information
                            skill!!.skillName = skillName
                            vm.updateSkill(skill!!)

                            // Navigate to the task fragment
                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillFragmentDirections.actionNewSkillFragmentToTaskFragment(skill!!,  skill!!.skillName))
                            }
                        }
                    } // end GlobalScope.Launch Coroutine
                } // end if-else statement
            } // end if (isValidName())

            // Hide the user's keyboard
            (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
        }

        return binding.root
    }

    /**
     * Ensures the skill was provided a name
     */
    private fun isValidName(): Boolean {
        skillName = binding.newSkillNameInput.text.toString()

        // Ensure a name was provided for the skill
        return if (skillName.isBlank()) {
            val toast = Toast.makeText(context, "Please give the new skill a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillMissingName.visibility = View.VISIBLE
            false
        } else {
            binding.newSkillMissingName.visibility = View.INVISIBLE
            skillName = skillName.trim()
            true
        }
    }
}