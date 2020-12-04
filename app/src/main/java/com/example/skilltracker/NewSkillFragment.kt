package com.example.skilltracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.entity.SkillSetWithSkills
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

/**
 * Used to create a new Skill
 * @property binding The binding variable for this fragment
 * @property vm The view model for skills
 */
class NewSkillFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillBinding
    private lateinit var vm: SkillsViewModel

    private var skillSet: SkillSet? = null
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
            inflater, R.layout.fragment_new_skill, container, false
        )

        skillSet = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skillSet }
        skill = arguments?.let { NewSkillFragmentArgs.fromBundle(it).skill }

        // If the skill is not null, the user is editing an existing skill
        if (skill != null) {
            binding.newSkillNameInput.setText(skill!!.skillName)
            binding.skillCompleted.visibility = View.VISIBLE
            binding.skillCompletedCheckbox.visibility = View.VISIBLE
            binding.skillCompletedCheckbox.isChecked = skill!!.completed
            binding.createNewSkillButton.text = getString(R.string.update_skill)

            // If the skill is completed, show the date it was completed on
            if (skill!!.completed) {
                binding.skillCompletedOn.visibility = View.VISIBLE
                binding.skillDateCompletedOn.visibility = View.VISIBLE
                binding.skillDateCompletedOn.text = skill!!.dateCompleted?.toLocalDate().toString()
            }
        }

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
        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
    }

    /**
     * Adds a new skill to the database or updates an existing skill
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
            binding.newSkillMissingName.visibility = View.INVISIBLE
            // If skill is null, the user is adding a new skill, otherwise they are updating an existing skill
            if (skill == null) {
                GlobalScope.launch {
                    // Create skill and insert skill and return it's row id
                    var newSkill = Skill(name, false)
                    var newSkillId = vm.insertSkill(newSkill)

                    // Set new skill's id to row and insert into join table
                    newSkill.skillId = newSkillId
                    vm.insertNewSkillWithJoin(skillSet!!, newSkill)
                }
            }
            else {
                // If the skill was marked as completed, set the dateCompleted
                if (binding.skillCompletedCheckbox.isChecked && !skill!!.completed) {
                    skill!!.dateCompleted = LocalDateTime.now()
                }

                skill!!.skillName = name
                skill!!.completed = binding.skillCompletedCheckbox.isChecked
                vm.updateSkill(skill!!)
            }
            return true
        }
    }
}