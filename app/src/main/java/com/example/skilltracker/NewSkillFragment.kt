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
    private var currentTaskNames: ArrayList<String?> = ArrayList<String?>()

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

        skillSet = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skillSet }
        skill = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skill }

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        spinner = binding.taskMultiSelectList

        vm.getTasks().observe(viewLifecycleOwner, { tasks ->
            allTasks = tasks as ArrayList<Task>
            spinner.setItems(allTasks as ArrayList<Any>)
        })

        // If the skill is not null, the user is editing an existing skill
        if (skill != null) {
            vm.getSpecificSkillWithTasks(skill!!.skillId).observe(viewLifecycleOwner, { tasksFromJoin: List<SkillWithTasks> ->
                currentTasks = tasksFromJoin[0].tasks as ArrayList<Task>
                spinner.setSelection(currentTasks as ArrayList<Any>)

                for (i in 0 until currentTasks.size) {
                    currentTaskNames.add(currentTasks[i].taskName)
                }

                tasksListView = binding.currentTasksListView
                adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, currentTaskNames)
                tasksListView.adapter = adapter
            })

            binding.cardThreeFragmentNewSkill.visibility = View.VISIBLE

            binding.createNewSkillButton.text = getString(R.string.update_skill)
            binding.newSkillNameInput.setText(skill!!.skillName)
            binding.skillCompletedCheckbox.isChecked = skill!!.completed
            binding.createNewSkillButton.text = getString(R.string.update_skill)

            // If the skill is completed, show the date it was completed on
            if (skill!!.completed) {
                binding.cardFourFragmentNewSkill.visibility = View.VISIBLE
                binding.skillDateCompletedOn.text = skill!!.dateCompleted?.toLocalDate().toString()
            }
        }

        binding.createNewSkillButton.setOnClickListener { view: View ->
            if (isValidName()) {
                val selectedTasks: ArrayList<Task> = spinner.getSelectedItems() as ArrayList<Task>

                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        if (skill == null) {
                            skill = Skill(skillName, false)
                            val newSkillId = vm.insertSkill(skill!!)
                            skill!!.skillId = newSkillId

                            vm.insertNewSkillWithJoin(skillSet!!, skill!!)

                            for (task in selectedTasks) {
                                vm.insertNewTaskWithJoin(skill!!, task)
                            }
                        }
                        else {
                            if (spinner.selectionChanged) {
                                for (task in currentTasks) {
                                    if (selectedTasks.indexOf(task) == -1) {
                                        //TODO - Make method to remove the skill task cross ref
                                    }
                                }

                                for (task in selectedTasks) {
                                    if (currentTasks.indexOf(task) == -1) {
                                        vm.insertNewTaskWithJoin(skill!!, task)
                                    }
                                }
                            } // end if(spinner.selectionChanged)

                            // If the skill was marked as completed, set the dateCompleted
                            if (binding.skillCompletedCheckbox.isChecked && !skill!!.completed) {
                                skill!!.dateCompleted = LocalDateTime.now()
                            }

                            // Update the skills information
                            skill!!.skillName = skillName
                            skill!!.completed = binding.skillCompletedCheckbox.isChecked
                            vm.updateSkill(skill!!)
                        } // end if(skill == null) else statement

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

        binding.addNewTasksButton.setOnClickListener { view: View ->
            if (isValidName()) {
                val selectedTasks: ArrayList<Task> = spinner.getSelectedItems() as ArrayList<Task>

                if (skill == null) {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            skill = Skill(skillName, false)
                            val newSkillId = vm.insertSkill(skill!!)
                            skill!!.skillId = newSkillId

                            vm.insertNewSkillWithJoin(skillSet!!, skill!!)

                            for (task in selectedTasks) {
                                vm.insertNewTaskWithJoin(skill!!, task)
                            }

                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillFragmentDirections.actionNewSkillFragmentToTaskFragment(skill!!))
                            }
                        }
                    } // end GlobalScope.Launch Coroutine
                }
                else {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            if (spinner.selectionChanged) {
                                for (task in currentTasks) {
                                    if (selectedTasks.indexOf(task) == -1) {
                                        //TODO - Make method to remove the skill task cross ref
                                    }
                                }

                                for (task in selectedTasks) {
                                    if (currentTasks.indexOf(task) == -1) {
                                        vm.insertNewTaskWithJoin(skill!!, task)
                                    }
                                }
                            } // end if(spinner.selectionChanged)

                            // If the skill was marked as completed, set the dateCompleted
                            if (binding.skillCompletedCheckbox.isChecked && !skill!!.completed) {
                                skill!!.dateCompleted = LocalDateTime.now()
                            }

                            // Update the skills information
                            skill!!.skillName = skillName
                            skill!!.completed = binding.skillCompletedCheckbox.isChecked
                            vm.updateSkill(skill!!)

                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillFragmentDirections.actionNewSkillFragmentToTaskFragment(skill!!))
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

    private fun isValidName(): Boolean {
        skillName = binding.newSkillNameInput.text.toString()

        // Ensure a name was provided for the skill
        if (skillName == "") {
            val toast = Toast.makeText(context, "Please give the new skill a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillMissingName.visibility = View.VISIBLE
            return false
        }
        else {
            binding.newSkillMissingName.visibility = View.INVISIBLE
            return true
        }
    }

}