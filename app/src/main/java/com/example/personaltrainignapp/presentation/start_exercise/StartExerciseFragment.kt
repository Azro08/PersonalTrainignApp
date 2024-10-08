package com.example.personaltrainignapp.presentation.start_exercise

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.databinding.FragmentStartExerciseBinding
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartExerciseFragment : DialogFragment() {
    private var _binding: FragmentStartExerciseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StartExerciseViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentStartExerciseBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this.activity)
        builder.run { setView(binding.root) }
        binding.buttonSaveWorkout.setOnClickListener {
            Log.d("WorkoutSave", "Entered")
            lifecycleScope.launch {
                viewModel.exerciseDetailsState.collect {
                    when (it) {
                        is ScreenState.Loading -> {}
                        is ScreenState.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Error loading exercise details",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is ScreenState.Success -> {
                            saveWorkout(it.data)
                        }
                    }
                }
            }
        }
        return builder.create()
    }

    private fun saveWorkout(exercise: Exercise?) {
        Log.d("WorkoutSave", exercise.toString())
        val weights =
            binding.editTextWeights.text?.split(" ")?.mapNotNull { it.toDoubleOrNull() }
                ?: emptyList()
        val reps = binding.editTextReps.text?.split(" ")?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()
        val currentDate = Constants.getCurrentDateString()
        val id = Constants.generateRandomId()
        val workout = Workout(
            id = id,
            exercise = exercise!!,
            weights = weights,
            reps = reps,
            date = currentDate,
        )
        lifecycleScope.launch {
            viewModel.saveWorkout(workout)
            viewModel.workoutSavedState.collect { state ->

                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            state.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ScreenState.Success -> {
                        Toast.makeText(requireContext(), state.data ?: "Saved", Toast.LENGTH_SHORT)
                            .show()
                        dismiss()
                    }
                }

            }
        }
    }
}