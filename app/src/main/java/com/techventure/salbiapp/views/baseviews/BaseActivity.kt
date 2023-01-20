package com.techventure.salbiapp.views.baseviews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.techventure.salbiapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun loadFragment(containerId: Int, newFragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(containerId, newFragment)
            addToBackStack(null)
            commit()
        }
    }
}