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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillSetBinding
import timber.log.Timber

/**
 * Used to create a new Skill Set
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 * @property skillSet data that can be passed in for an update
 */
class NewSkillSetFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillSetBinding
    private lateinit var vm: SkillsViewModel
    private var skillSet: SkillSet? = null
    private var allSkills: ArrayList<Skill> = ArrayList()
    private var currentSkills: ArrayList<Skill> = ArrayList()
    private lateinit var spinner: MultiSelectionSpinner
    private lateinit var skillsListView: ListView
    private lateinit var adapter: ArrayAdapter<String>
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
        // Set the fab visibility to false so it does not display while the user is creating a new skill set
        (activity as MainActivity).hideFAB()

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_skill_set, container, false
        )

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)

        // Get all of the skills from the database & add them to the multi-select spinner
        vm.getSkills().observe(viewLifecycleOwner, Observer { skills ->
            allSkills = skills as ArrayList<Skill>

            // Initialize the multi select spinner and set its items/skills
            spinner = binding.skillMultiSelectList
            spinner.setItems(allSkills)

            // If the user is editing an existing skill set, get the skill set's current skills and display
            //  them in a list view
            if (skillSet != null) {
                // Get the skills that are currently part of the skill set and set them to selected in the multi-select spinner
                vm.getSkillsFromJoin(skillSet!!.skillSetId).observe(viewLifecycleOwner, { skills ->
                    currentSkills = skills as ArrayList<Skill>
                    spinner.setSelection(currentSkills)

                    for (i in 0 until currentSkills.size) {
                        currentSkillNames.add(currentSkills[i].skillName)
                    }

                    skillsListView = binding.currentSkillsListView
                    adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, currentSkillNames)
                    skillsListView.adapter = adapter
                })
            }
        })

        // Get the skill set if it was passed on and set its values to the corresponding inputs
        skillSet = arguments?.let { NewSkillSetFragmentArgs.fromBundle(it).skillSet }
        if(skillSet != null) {
            binding.newSkillSetNameInput.setText(skillSet!!.name)
            binding.newSkillSetDescriptionInput.setText(skillSet!!.description)
            binding.createNewSkillSetButton.text = getString(R.string.update_skillSet)
            binding.currentSkillsLabel.visibility = View.VISIBLE
            binding.currentSkillsListView.visibility = View.VISIBLE
        }

        // Set an onClickListener for the createNewSkillSet button
        binding.createNewSkillSetButton.setOnClickListener {
            // Add the new skill set to the database if it is valid
            if (addNewSkillSet()) {
                // Navigate back to the SkillSet fragment
                val navController = this.findNavController()
                navController.navigateUp()

                // Ensure the FAB is visible
                (activity as MainActivity).showFAB()

                // Hide the user's keyboard
                (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
            }
        }

        binding.addNewSkillsButton.setOnClickListener { view: View ->
            if (addNewSkillSet()) {
                view.findNavController().navigate(NewSkillSetFragmentDirections.actionNewSkillSetFragmentToSkillFragment(skillSet!!))
            }

            // Ensure the FAB is visible
            (activity as MainActivity).showFAB()

            // Hide the user's keyboard
            (activity as MainActivity).closeKeyboardFromFragment(activity as MainActivity, this)
        }

        return binding.root
    }

    /**
     * Adds a new skill set to the database or updates an existing skill set
     */
    private fun addNewSkillSet(): Boolean {
        val name: String = binding.newSkillSetNameInput.text.toString()
        val description: String = binding.newSkillSetDescriptionInput.text.toString()
        var validName = false
        var validDescription = false

        // Ensure a name was provided
        if (name == null || name == "") {
            val toast = Toast.makeText(context, "Please give the new skill set a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillSetMissingName.visibility = View.VISIBLE
        }
        else {
            validName = true
            binding.newSkillSetMissingName.visibility = View.INVISIBLE
        }

        // Ensure a description was provided
        if (description == null || description == "") {
            val toast = Toast.makeText(context, "Please give the new skill set a description", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillSetMissingDescription.visibility = View.VISIBLE
        }
        else {
            validDescription = true
            binding.newSkillSetMissingDescription.visibility = View.INVISIBLE
        }

        // If name and description are provided, add the skill set to the database
        if (validName && validDescription) {
            // Get the skills the user selected to add to the skill set right away
            var selectedItems: ArrayList<Skill> = spinner.getSelectedItems()

            Timber.i("Number of selected items: " + selectedItems.size)
            for (item in selectedItems) {
                Timber.i("Selected skill name: " + item.skillName)
            }

            // TODO: Add the skills from the multi-select to the skill set

            if(skillSet == null) {
                skillSet = SkillSet(name, description)

                // TODO: I think we will want to store the new skillSets Id so we have the Id when it is passed to the skill fragment
                // skillSet = vm.insertSkillSet(skillSet)
                vm.insertSkillSet(skillSet!!)
            }
            else {
                // Set values in the SkillSet and call update
                skillSet!!.name = name
                skillSet!!.description = description
                vm.updateSkillSet(skillSet!!)
            }
            return true
        }

        return false
    }
}