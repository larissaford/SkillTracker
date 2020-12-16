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
import com.example.skilltracker.database.adpater.SkillSetRecyclerAdapter
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentSkillSetBinding

/**
 * Used to display all of the user's skill sets
 */
class SkillSetFragment : Fragment(){
    private lateinit var binding: FragmentSkillSetBinding
    private lateinit var vm: SkillsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_skill_set, container, false)

        return binding.root
    }

    /**
     * Gets all of the skill sets and binds them to the page and sets an onClickListener for the FAB
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        binding.skillSetList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getSkillSet().observe(viewLifecycleOwner, {
            binding.skillSetList.adapter = context?.let {
                vm.getSkillSet().value?.let { it1 ->
                    SkillSetRecyclerAdapter(it,
                        it1
                    )
                }
            }
        })

        // Set an onClickListener for the FAB
        binding.fab.setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(SkillSetFragmentDirections.actionSkillSetFragmentToNewSkillSetFragment(null))
        }
    }
}