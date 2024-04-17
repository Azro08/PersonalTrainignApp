package com.example.personaltrainignapp.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.WorkoutDay
import com.example.personaltrainignapp.databinding.FragmentHomeBinding
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.util.Calendar

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var workoutDays: List<WorkoutDay>
    private var currentDay: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workoutDays = loadWorkoutDaysFromSharedPreferences()

        if (workoutDays.isEmpty()) {
            binding.textViewError.visibility = View.VISIBLE
        }

        val calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_WEEK)

        binding.recyclerViewWorkoutDays.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWorkoutDays.adapter =
            WorkoutDayAdapter(requireContext(), workoutDays, currentDay)
    }

    private fun loadWorkoutDaysFromSharedPreferences(): MutableList<WorkoutDay> {
        val sharedPreferences =
            requireContext().getSharedPreferences("WorkoutDays", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("WorkoutDays", null)

        val type = object : TypeToken<List<WorkoutDay>>() {}.type
        return Gson().fromJson(json, type) ?: mutableListOf()
    }

}