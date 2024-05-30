package com.daniel.todoapp.ui.fragment.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.FragmentPlaceCategoryBinding
import com.daniel.todoapp.databinding.FragmentPlacesBinding
import com.daniel.todoapp.ui.activity.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class PlaceCategoryFragment : Fragment() {
    private lateinit var binding: FragmentPlaceCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlaceCategoryBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_place_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomNavigationView.menu.getItem(2).setChecked(true)

        binding.cardCafe.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("cafe")
            )
        }

        binding.cardMall.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("mall")
            )
        }

        binding.cardMuseum.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("museum")
            )
        }

        binding.cardPark.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("park")
            )
        }

        binding.cardSpa.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("spa")
            )
        }

        binding.cardRestaurant.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToPlacesFragment("restaurant")
            )
        }

        binding.btnAdd.setOnClickListener {
            view.findNavController().navigate(
                PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToSqlTodoFormFragment(null)

            )
        }

        binding.bottomAppBar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    Firebase.auth.signOut()
                    val intent = Intent(requireContext(), WelcomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            true
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.todo -> {
                    view.findNavController().navigate(
                        PlaceCategoryFragmentDirections.actionPlaceCategoryFragmentToTodosFragment()
                    )
                    true
                }
                else -> false
            }
        }
    }
}