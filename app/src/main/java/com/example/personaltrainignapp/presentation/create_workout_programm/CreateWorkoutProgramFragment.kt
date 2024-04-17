package com.example.personaltrainignapp.presentation.create_workout_programm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.MainActivity
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.WorkoutDay
import com.example.personaltrainignapp.databinding.FragmentCreateWorkoutProgramBinding
import com.example.personaltrainignapp.util.WorkoutDaySharedPreferencesHelper

class CreateWorkoutProgramFragment : Fragment(R.layout.fragment_create_workout_program) {
    private val binding by viewBinding(FragmentCreateWorkoutProgramBinding::bind)
    private val workoutDays = mutableListOf<WorkoutDay>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadExercises()
        initializeWorkoutDays()
        binding.buttonSaveWorkoutProgram.setOnClickListener {
            if (allFieldsAreFilled()) savePlans()
            else Toast.makeText(
                requireContext(),
                getString(R.string.fill_upFields),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun loadExercises() {
        val exercises = WorkoutDaySharedPreferencesHelper.loadWorkoutDays(requireContext())
        if (exercises.isNotEmpty()){
            binding.editTextDay1.setText(exercises[0].plan)
            binding.editTextDay2.setText(exercises[1].plan)
            binding.editTextDay3.setText(exercises[2].plan)
            binding.editTextDay4.setText(exercises[3].plan)
            binding.editTextDay5.setText(exercises[4].plan)
            binding.editTextDay6.setText(exercises[5].plan)
            binding.editTextDay7.setText(exercises[6].plan)
        }
    }

    private fun initializeWorkoutDays() {
        // Populate the list with workout days (Day 1 to Day 7)
        for (i in 1..7) {
            workoutDays.add(WorkoutDay(dayNumber = i))
        }
    }

    private fun savePlans() = with(binding) {
        workoutDays[0].plan = editTextDay1.text.toString()
        workoutDays[1].plan = editTextDay2.text.toString()
        workoutDays[2].plan = editTextDay3.text.toString()
        workoutDays[3].plan = editTextDay4.text.toString()
        workoutDays[4].plan = editTextDay5.text.toString()
        workoutDays[5].plan = editTextDay6.text.toString()
        workoutDays[6].plan = editTextDay7.text.toString()

        WorkoutDaySharedPreferencesHelper.saveWorkoutDays(requireContext(), workoutDays)
        requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun allFieldsAreFilled(): Boolean = with(binding) {

        return@with editTextDay1.text!!.isNotBlank() &&
                editTextDay2.text!!.isNotBlank() &&
                editTextDay3.text!!.isNotBlank() &&
                editTextDay4.text!!.isNotBlank() &&
                editTextDay5.text!!.isNotBlank() &&
                editTextDay6.text!!.isNotBlank() &&
                editTextDay7.text!!.isNotBlank()
    }
}