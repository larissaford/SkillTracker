package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentSkillBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Displays all of the skills under a skill set
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 */
class SkillFragment : Fragment() {
    private lateinit var binding: FragmentSkillBinding
    private lateinit var vm: SkillsViewModel

    /**
     * Inflates the layout for this fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The view of the fragment's UI or null
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_skill, container, false
        )

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

        // Ensure the FAB is visible
        val fab: FloatingActionButton = this.requireActivity().findViewById(R.id.fab)
        fab.visibility = View.VISIBLE

        //To-Do: Add the recycler view for skills
    }

    /**
     * Navigates to the NewSkillFragments and makes the FAB invisible
     * @param view: The view displayed when the FAB was clicked
     */
    fun onFABClicked(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(SkillFragmentDirections.actionSkillFragmentToNewSkillFragment())

            // Set the fab visibility to false so it does not display while the user is creating a new skill set
            (activity as MainActivity).hideFAB()

            //clear the database for testing
            //vm.nuke()
        }
    }
}