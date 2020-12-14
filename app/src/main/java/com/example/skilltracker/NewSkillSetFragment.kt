package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillSetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Used to create a new Skill Set or update an existing Skill Set
 *
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 * @property spinner A custom multi-selection spinner to display all skills
 * @property skillsListView A list view that contains all of the skills already added to the skill set
 * @property adapter An ArrayAdapter for the skillsListView
 * @property skillSetName The name of the skill set
 * @property skillSetDescription The description of the skill set
 * @property skillSet data that can be passed in for an update
 * @property allSkills A list of all the skills currently stored in the database
 * @property currentSkills A list of all the skills already added to the skill set
 * @property currentSkillNames A list of all of the skill names already added to the skill set
 */

class NewSkillSetFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillSetBinding
    private lateinit var vm: SkillsViewModel
    private lateinit var spinner: MultiSelectionSpinner
    private lateinit var skillsListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var skillSetName: String
    private lateinit var skillSetDescription: String

    private var skillSet: SkillSet? = null
    private var allSkills: ArrayList<Skill> = ArrayList()
    private var currentSkills: ArrayList<Skill> = ArrayList()
    private var currentSkillNames: ArrayList<String?> = ArrayList<String?>()

    /**
     * Inflates the layout for this fragment, initializes the viewModel variable vm, gets all skills and
     *  sets them to the multi-select spinner, and sets an onClickListener for the createNewSkillSet button
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The view of the fragment's UI or null
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_skill_set, container, false
        )

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)

        // Get all of the skills from the database & add them to the multi-select spinner
        vm.getSkills().observe(viewLifecycleOwner, { skills ->
            allSkills = skills as ArrayList<Skill>

            // Initialize the multi select spinner and set its items/skills
            spinner = binding.skillMultiSelectList
            spinner.setItems(allSkills)

            // If the user is editing an existing skill set, get the skill set's current skills and display
            //  them in a list view
            if (skillSet != null) {
                binding.createNewSkillSetButton.text = getString(R.string.update_skill_set)
                // Get the skills that are currently part of the skill set and set them to selected in the multi-select spinner
                vm.getSkillsFromJoin(skillSet!!.skillSetId).observe(viewLifecycleOwner, { skillsFromJoin: List<Skill> ->
                    currentSkills = skillsFromJoin as ArrayList<Skill>
                    Timber.i("Current skills size: %s", currentSkills.size)
                    spinner.setSelection(currentSkills)

                    for (i in 0 until currentSkills.size) {
                        currentSkillNames.add(currentSkills[i].skillName)
                    }

                    skillsListView = binding.currentSkillsListView
                    adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, currentSkillNames)
                    skillsListView.adapter = adapter
                })
            }
            else {
                binding.cardFour.visibility = View.INVISIBLE
            }
        })

        // Get the skill set if it was passed in and set its values to the corresponding inputs
        skillSet = arguments?.let { NewSkillSetFragmentArgs.fromBundle(it).skillSet }
        if(skillSet != null) {
            binding.newSkillSetNameInput.setText(skillSet!!.name)
            binding.newSkillSetDescriptionInput.setText(skillSet!!.description)

            binding.currentSkillsLabel.visibility = View.VISIBLE
            binding.currentSkillsListView.visibility = View.VISIBLE
        }

        // Set an onClickListener for the createNewSkillSet button
        binding.createNewSkillSetButton.setOnClickListener { view: View ->
            // Add or update the skill set to the database if it is valid
            if (isValidNameAndDescription()) {
                // Get the skills the user selected to add to the skill set right away
                val selectedSkills = spinner.getSelectedItems()

                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        // If the skillSet is null, the user is creating a new skill set; otherwise they are editing
                        //  an existing skill set
                        if (skillSet == null) {
                            // Create & insert the skill set and return its row id
                            skillSet = SkillSet(skillSetName, skillSetDescription)
                            val newSkillSetId = vm.insertSkillSet(skillSet!!)
                            skillSet!!.skillSetId = newSkillSetId

                            // Insert the selected skills into the join table
                            for (item in selectedSkills) {
                                vm.insertNewSkillWithJoin(skillSet!!, item)
                            }
                        }
                        else {
                            // If any skills were added and/or removed, update the SkillSet to match the change(s)
                            if (spinner.selectionChanged) {
                                // Remove any skills that were newly un-checked
                                for (skill in currentSkills) {
                                    if (selectedSkills.indexOf(skill) == -1) {
                                        vm.deleteSkillSetSkillCrossRef(skillSet!!, skill)
                                    }
                                }

                                // Add any skills that were newly checked
                                for (skill in selectedSkills) {
                                    if (currentSkills.indexOf(skill) == -1) {
                                        vm.insertNewSkillWithJoin(skillSet!!, skill)
                                    }
                                }
                            }

                            // Update the skill set's information
                            skillSet!!.name = skillSetName
                            skillSet!!.description = skillSetDescription
                            vm.updateSkillSet(skillSet!!)
                        }

                        // Navigate back to the SkillSet fragment
                        withContext(Dispatchers.Main) {
                            view.findNavController().navigate(NewSkillSetFragmentDirections.actionNewSkillSetFragmentToSkillSetFragment())
                        }
                    }
                } // end GlobalScope.launch Coroutine

                // Hide the user's keyboard
                (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
            }
        }

        // Set an onClickListener for the Create New Skills button
        binding.addNewSkillsButton.setOnClickListener { view: View ->
            // Add or update the skill set to the database if it is valid
            if (isValidNameAndDescription()) {
                // Get the skills the user selected to add to the skill set right away
                val selectedSkills: ArrayList<Skill> = spinner.getSelectedItems()

                // If the skillSet is null, the user is creating a new skill set; otherwise they are editing
                //  an existing skill set
                if (skillSet == null) {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            // Create & insert the skill set and return its row id
                            skillSet = SkillSet(skillSetName, skillSetDescription)
                            val newSkillSetId = vm.insertSkillSet(skillSet!!)
                            skillSet!!.skillSetId = newSkillSetId

                            // Insert the selected skills into the join table
                            for (item in selectedSkills) {
                                vm.insertNewSkillWithJoin(skillSet!!, item)
                            }

                            // Navigate to the SkillFragment
                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillSetFragmentDirections.actionNewSkillSetFragmentToSkillFragment(skillSet!!))
                            }
                        }
                    } // end GlobalScope.launch Coroutine
                }
                else {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            // If any skills were added and/or removed, update the SkillSet to match the change(s)
                            if (spinner.selectionChanged) {
                                // Remove any skills that were newly un-checked
                                for (skill in currentSkills) {
                                    if (selectedSkills.indexOf(skill) == -1) {
                                        Timber.i("Removing skill: %s", skill.skillName)
                                        vm.deleteSkillSetSkillCrossRef(skillSet!!, skill)
                                    }
                                }

                                // Add any skills that were newly checked
                                for (skill in selectedSkills) {
                                    if (currentSkills.indexOf(skill) == -1) {
                                        Timber.i("Adding skill: %s", skill.skillName)
                                        vm.insertNewSkillWithJoin(skillSet!!, skill)
                                    }
                                }
                            }

                            // Update the skill set's information
                            skillSet!!.name = skillSetName
                            skillSet!!.description = skillSetDescription
                            vm.updateSkillSet(skillSet!!)

                            // Navigate to the SkillFragment
                            withContext(Dispatchers.Main) {
                                view.findNavController().navigate(NewSkillSetFragmentDirections.actionNewSkillSetFragmentToSkillFragment(skillSet!!))
                            }
                        }
                    } // end GlobalScope.launch Coroutine
                }
            }

            // Hide the user's keyboard
            (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
        }

        return binding.root
    }

    /**
     * Ensures the SkillSet has a valid name and description
     * @return True if the name and description are valid, false otherwise
     */
    private fun isValidNameAndDescription(): Boolean {
        skillSetName = binding.newSkillSetNameInput.text.toString()
        skillSetDescription = binding.newSkillSetDescriptionInput.text.toString()
        var validName = false
        var validDescription = false

        // Ensure a name was provided
        if (skillSetName == "") {
            val toast = Toast.makeText(context, "Please give the new skill set a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillSetMissingName.visibility = View.VISIBLE
        }
        else {
            validName = true
            binding.newSkillSetMissingName.visibility = View.INVISIBLE
        }

        // Ensure a description was provided
        if (skillSetDescription == "") {
            val toast = Toast.makeText(context, "Please give the new skill set a description", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillSetMissingDescription.visibility = View.VISIBLE
        }
        else {
            validDescription = true
            binding.newSkillSetMissingDescription.visibility = View.INVISIBLE
        }

        return validName && validDescription
    }
}