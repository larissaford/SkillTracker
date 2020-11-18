package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillBinding

/**
 * Used to create a new Skill
 * @property binding The binding variable for this fragment
 * @property vm The view model for skills
 */
class NewSkillFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillBinding
    private lateinit var vm: SkillsViewModel

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

        binding.createNewSkillButton.setOnClickListener {
            if (addNewSkill()) {
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
        // To-Do: Use the right view model
        //vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
    }

    /**
     * Adds a new skill to the database
     */
    private fun addNewSkill(): Boolean {
        val name: String = binding.newSkillNameInput.text.toString()

        // Ensure a name was provided for the skill
        if (name == null || name == "") {
            val toast = Toast.makeText(context, "Please give the new skill a name", Toast.LENGTH_SHORT)
            toast.show()
            binding.newSkillMissingName.visibility = View.VISIBLE
            return false
        }
        else {
            //val skill = Skill(name)
            //vm.insertSkill(skill)
            binding.newSkillMissingName.visibility = View.INVISIBLE
            return true
        }
    }
}