package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skilltracker.database.adpater.SkillRecyclerAdapter
import com.example.skilltracker.database.entity.Skill
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentSkillBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber
import java.lang.Exception

/**
 * Displays all of the skills under a skill set
 * @property binding The binding variable for this fragment
 * @property vm The view model for skill sets
 * @property skillSet The skill set that was clicked on to view the skills
 */
class SkillFragment : Fragment(){
    private lateinit var binding: FragmentSkillBinding
    private lateinit var vm: SkillsViewModel
    private lateinit var skillSet: SkillSet

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

        skillSet = arguments?.let { SkillFragmentArgs.fromBundle(it).skillSet }!!
        print("SKILLSET ID: ${skillSet.skillSetId}\n")

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        binding.skillList.layoutManager = LinearLayoutManager(context)

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

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        binding.skillList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getAllSkillWithTasksForSpecificSkillSet(skillSet.skillSetId).observe(viewLifecycleOwner, {
//            for(x in it) {
//                println("SKILL ID: ${x.skillId}")
//                println("SKILL NAME: ${x.skillName}")
//            }
            binding.skillList.adapter = SkillRecyclerAdapter(this.requireContext(), it)
        })

        binding.fab.setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(SkillFragmentDirections.actionSkillFragmentToNewSkillFragment(skillSet, null))
        }
    }
}