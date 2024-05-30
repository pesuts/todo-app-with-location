package com.daniel.todoapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daniel.todoapp.R
import com.daniel.todoapp.databinding.ActivityFragmentBinding
import com.daniel.todoapp.ui.fragment.HomeFragment

class FragmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mFragmentManager = supportFragmentManager
        val fragment1 = HomeFragment()

        val fragment = mFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        if(fragment !is HomeFragment){
            mFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment1, HomeFragment::class.java.simpleName)
                .commit()
        }
    }
}