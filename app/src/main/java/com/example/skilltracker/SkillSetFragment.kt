package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skilltracker.database.adpater.SkillRecyclerAdapter
import com.example.skilltracker.database.entity.SkillSet
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentSkillSetBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SkillSetFragment : Fragment() {
    private lateinit var binding: FragmentSkillSetBinding
    private lateinit var vm: SkillsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(
            inflater, R.layout.fragment_skill_set, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
//        vm.nuke()
        //vm.insertSkillSet(SkillSet())
        binding.skillSetList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getSkillSet().observe(viewLifecycleOwner, Observer {
            binding.skillSetList.adapter = context?.let { vm.getSkillSet().value?.let { it1 ->
                SkillRecyclerAdapter(it,
                    it1
                )
            } }
        })

        //set on click listeners here
        /*view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
    }

    fun onFABClicked(view: View) {
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            //clear the database for testing
            //vm.nuke()
            val skillSet = SkillSet()
            vm.insertSkillSet(SkillSet())
        }
    }
}