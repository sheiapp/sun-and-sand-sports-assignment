package com.sunandsandsports.assignment.ui.random_user_ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sunandsandsports.assignment.databinding.ActivityRandomUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomUserActivity : AppCompatActivity() {
    private var _binding: ActivityRandomUserBinding? = null
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRandomUserBinding.inflate(layoutInflater)
        setContentView(_binding?.root)
    }
}