package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.skilltracker.databinding.FragmentSkillSetBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SkillSetFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSkillSetBinding =  DataBindingUtil.inflate(
            inflater, R.layout.fragment_skill_set, container, false)

        val vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
        vm.insertSkillSet(SkillSet())
        binding.skillSetList.layoutManager = LinearLayoutManager(context)

        // fill the recycler view with most recent data from the database
        vm.getSkillSet().observe(viewLifecycleOwner, Observer {
            binding.skillSetList.adapter = context?.let { vm.getSkillSet().value?.let { it1 ->
                SkillRecyclerAdapter(it,
                    it1
                )
            } }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set on click listeners here
        /*view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }*/
    }
}