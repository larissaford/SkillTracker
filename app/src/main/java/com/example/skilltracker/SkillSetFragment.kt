package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skilltracker.database.adpater.SkillSetRecyclerAdapter
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentSkillSetBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SkillSetFragment : Fragment(), FABclicker {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ensure the FAB is visible
        val fab: FloatingActionButton = this.requireActivity().findViewById(R.id.fab)
        fab.visibility = View.VISIBLE

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        binding.skillSetList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getSkillSet().observe(viewLifecycleOwner, Observer {
            binding.skillSetList.adapter = context?.let {
                vm.getSkillSet().value?.let { it1 ->
                    SkillSetRecyclerAdapter(it,
                        it1
                    )
                }
            }
        })

        binding.fab.setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(SkillSetFragmentDirections.actionSkillSetFragmentToNewSkillSetFragment(null))
        }
        //set on click listeners here
        /*view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
    }

    /**
     * Navigates to the NewSkillSetFragment and makes the FAB invisible
     * @param view: The view displayed when the FAB was clicked
     */
    override fun onFABClicked(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            // Navigate to the NewSkillSet Fragment
            val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(SkillSetFragmentDirections.actionSkillSetFragmentToNewSkillSetFragment(null))

            //clear the database for testing
            //vm.nuke()
        }
    }
}