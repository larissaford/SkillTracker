package com.example.skilltracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillSetBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

/**
 * Used to create a new Skill Set
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 */
class NewSkillSetFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillSetBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_skill_set, container, false)

        binding.createNewSkillSetButton.setOnClickListener {
            // Add the new skill set to the database if it is valid
            if (addNewSkillSet()) {
                // Navigate back to the SkillSet fragment
                val navController = this.findNavController()
                navController.navigateUp()

                // Make the FAB visible again
                val fab: FloatingActionButton = this.requireActivity().findViewById(R.id.fab)
                fab.visibility = View.VISIBLE

                // Hide the user's keyboard
                it.hideKeyboard()
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
     * Adds a new skill set to the database
     */
    private fun addNewSkillSet(): Boolean {
        val title: String = binding.newSkillSetNameInput.text.toString()
        val description: String = binding.newSkillSetDescriptionInput.text.toString()
        val toast: Toast

        if (title == null || title == "") {
            toast = Toast.makeText(context, "Please give the new skill set a name", Toast.LENGTH_SHORT)

        }
        else if (description == null || description == "") {
            toast = Toast.makeText(context, "Please give the new skill set a description", Toast.LENGTH_SHORT)
        }
        else {
            Timber.i("%s%s", "Title: " + title + " Description: ", description)
            val skillSet = SkillSet(title, description)
            vm.insertSkillSet(skillSet)
            return true
        }
        toast.show()
        return false
    }

    /**
     * Hides the user's keyboard if it is currently visible
     */
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}