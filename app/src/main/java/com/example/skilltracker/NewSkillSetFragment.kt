package com.example.skilltracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.skilltracker.database.viewmodel.SkillsViewModel
import com.example.skilltracker.databinding.FragmentNewSkillSetBinding

/**
 * A simple [Fragment] subclass.
 * Use the [NewSkillSetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSkillSetFragment : Fragment() {
    private lateinit var binding: FragmentNewSkillSetBinding
    private lateinit var vm: SkillsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_skill_set, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProvider(this).get(SkillsViewModel::class.java)
    }
}