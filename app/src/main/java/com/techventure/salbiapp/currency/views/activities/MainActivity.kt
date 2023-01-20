package com.techventure.salbiapp.currency.views.activities

import android.os.Bundle
import com.techventure.salbiapp.R
import com.techventure.salbiapp.databinding.ActivityMainBinding
import com.techventure.salbiapp.views.baseviews.BaseActivity
import com.techventure.salbiapp.currency.views.fragments.CurrencyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(R.id.fl_fragment, CurrencyFragment.newInstance("Test Param"))
    }
}