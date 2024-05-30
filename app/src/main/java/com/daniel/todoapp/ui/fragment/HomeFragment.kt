package com.daniel.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.daniel.todoapp.R
import com.daniel.todoapp.viewmodel.MainViewModel
import com.daniel.todoapp.ui.activity.MainActivity
import com.daniel.todoapp.databinding.FragmentHomeBinding
//import com.daniel.todoapp.ui.fragment.HomeFragmentDirections.ActionHomeFragmentToSqlTodoFormFragment
import com.daniel.todoapp.ui.fragment.login.SignUpFragmentDirections
import com.daniel.todoapp.ui.fragment.place.PlacesFragment
import com.daniel.todoapp.ui.fragment.todo.SqlTodoFragment
import com.google.android.material.bottomappbar.BottomAppBar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var parentActivity: MainActivity
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        bottomAppBar = binding.bottomAppBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentActivity = requireActivity() as MainActivity

        view.findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToTodosFragment()
        )

        bottomAppBar.replaceMenu(R.menu.bottom_nav_menu)

        binding.btnAdd.setOnClickListener {
            view.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTodosFragment()
            )
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.todo -> {
                    view.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToTodosFragment()
                    )
                    true
                }
                R.id.place -> {
                    view.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToPlaceCategoryFragment2()
                    )
                    true
                }
                else -> false
            }
        }

    }
}