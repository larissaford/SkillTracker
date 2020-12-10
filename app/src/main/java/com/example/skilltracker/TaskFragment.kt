package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skilltracker.database.adpater.TaskRecyclerAdapter
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentTaskBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Displays all of the skills under a skill set
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 * @property skillSet The skill set that was clicked on to view the skills
 */
class TaskFragment : Fragment(), FABclicker {
    private lateinit var binding: FragmentTaskBinding
    private lateinit var vm: SkillsViewModel
    private lateinit var skill: Skill

    /**
     * Inflates the layout for this fragment and gets the skillSet from the passed in arguments
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The view of the fragment's UI or null
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task, container, false
        )

        skill = arguments?.let { TaskFragmentArgs.fromBundle(it).skill }!!
        print("SKILL ID: ${skill.skillId}\n")

        return binding.root
    }

    /**
     * Initializes the view model variable vm and fills the recycler view with the skills
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

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        binding.taskList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getSkills().observe(viewLifecycleOwner, {
            binding.taskList.adapter = context?.let { vm.getTasks().value?.let { it1 ->
                TaskRecyclerAdapter(it,
                    it1
                )
            } }
        })

        binding.fab.setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(null, skill))
        }
    }

    /**
     * Navigates to the NewSkillFragment and makes the FAB invisible
     * @param view: The view displayed when the FAB was clicked
     */
    override fun onFABClicked(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(TaskFragmentDirections.actionTaskFragmentToNewTaskFragment(null, skill))

            //clear the database for testing
            //vm.nuke()
        }
    }
}