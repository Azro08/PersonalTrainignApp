package com.example.personaltrainignapp.presentation.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.azrosk.sell_it.shared.auth.AuthViewPagerAdapter
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    private val binding by viewBinding(ActivityAuthBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setViewPager()
    }

    private fun setViewPager() {

        val adapter = AuthViewPagerAdapter(this)
        binding.authViewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.authViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.login)
                1 -> getString(R.string.register)
                else -> ""
            }
        }.attach()

    }
}