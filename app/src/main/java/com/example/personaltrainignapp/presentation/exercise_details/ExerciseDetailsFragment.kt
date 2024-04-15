package com.example.personaltrainignapp.presentation.exercise_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.databinding.FragmentExerciseDetailsBinding
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseDetailsFragment : Fragment(R.layout.fragment_exercise_details) {
    private val binding by viewBinding(FragmentExerciseDetailsBinding::bind)
    private val viewModel: ExercisesDetailsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getDetails()
    }

    private fun getDetails() {
        lifecycleScope.launch {
            viewModel.exerciseDetailsState.collect { state ->
                when (state) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }

                    is ScreenState.Success -> {
                        if (state.data != null) showDetails(state.data)
                        else Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun showDetails(exercise: Exercise) = with(binding) {
        Glide.with(requireContext())
            .load(exercise.gifUrl)
            .error(R.drawable.fitness_logo)
            .into(imageExercise)
        textViewExerciseName.text = exercise.name
        textViewTargetMuscle.text = exercise.target
        textViewSecondaryMuscle.text = exercise.secondaryMuscles.toString()
        textViewEquipment.text = exercise.equipment
        textViewInstructions.text = exercise.instructions.toString()

        binding.buttonStartWorkout.setOnClickListener {
            startExercise(exercise.id)
        }

    }

    private fun startExercise(id: String) {
        val bundle = bundleOf(Pair(Constants.EXERCISE_KEY, id))
        findNavController().navigate(R.id.nav_exercise_details_start_exercise, bundle)
    }

}