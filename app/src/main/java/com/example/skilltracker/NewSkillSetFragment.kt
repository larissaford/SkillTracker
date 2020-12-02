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
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillSetBinding

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
            inflater, R.layout.fragment_new_skill_set, container, false
        )

        skillSet = arguments?.let { NewSkillSetFragmentArgs.fromBundle(it).skillSet }
        if(skillSet != null) {
            binding.newSkillSetNameInput.setText(skillSet!!.name)
            binding.newSkillSetDescriptionInput.setText(skillSet!!.description)
            binding.createNewSkillSetButton.text = getString(R.string.update_skillSet)
        }

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
            if(skillSet == null) {
                val skillSet = SkillSet(name, description)
                vm.insertSkillSet(skillSet)
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