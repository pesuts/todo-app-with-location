package com.daniel.todoapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.ActivityMainBinding
import com.daniel.todoapp.ui.fragment.HomeFragment
import com.daniel.todoapp.ui.fragment.place.PlacesFragment
import com.daniel.todoapp.ui.fragment.todo.SqlTodoFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

//    fun setLoading(data: Boolean) {
//        if (data) {
//            binding.progressBar.visibility = View.VISIBLE
//        } else {
//            binding.progressBar.visibility = View.GONE
//        }
//    }

//    private fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.frame_layout, fragment)
//            .commit()
//    }
}