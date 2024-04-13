package com.example.personaltrainignapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.databinding.ActivityMainBinding
import com.example.personaltrainignapp.presentation.auth.AuthActivity
import com.example.personaltrainignapp.util.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    @Inject
    lateinit var authManager: AuthManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!authManager.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}