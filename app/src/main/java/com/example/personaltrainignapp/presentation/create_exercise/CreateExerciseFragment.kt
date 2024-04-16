package com.example.personaltrainignapp.presentation.create_exercise

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.databinding.FragmentCreateExerciseBinding
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateExerciseFragment : Fragment(R.layout.fragment_create_exercise) {
    private val binding by viewBinding(FragmentCreateExerciseBinding::bind)
    private val viewModel: CreateExerciseViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSaveExercise.setOnClickListener {
            if (allFieldsAreFilled()) saveExercise()
            else Toast.makeText(
                requireContext(),
                getString(R.string.fill_upFields),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.buttonUploadImage.setOnClickListener {
            Glide.with(binding.root)
                .load(binding.editTextImageUrl.text.toString())
                .error(R.drawable.fitness)
                .into(binding.imageViewNewExerciseImage)
        }

    }

    private fun saveExercise() {
        binding.buttonSaveExercise.visibility = View.GONE
        binding.imageViewLoadingGif.visibility = View.VISIBLE
        lifecycleScope.launch {
            val exerciseId = Constants.generateRandomId()
            val imageUrl = binding.editTextImageUrl.text.toString()
            val name = binding.editTextExerciseName.text.toString()
            val targetMuscle = binding.editTextExerciseTargetMuscle.text.toString()
            val secondaryMuscle = listOf(binding.editTextExerciseSecondary.text.toString())
            val equipment = binding.editTextExerciseEquipment.text.toString()
            val instructions = listOf(binding.editTextExerciseInstructions.text.toString())
            val newExercise = Exercise(
                id = exerciseId,
                name = name,
                gifUrl = imageUrl,
                target = targetMuscle,
                secondaryMuscles = secondaryMuscle,
                equipment = equipment,
                instructions = instructions
            )

            viewModel.saveExercise(newExercise)
            viewModel.saveExerciseState.collect {
                when (it) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.buttonSaveExercise.visibility = View.VISIBLE
                        binding.imageViewLoadingGif.visibility = View.GONE
                    }

                    is ScreenState.Success -> {
                        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }

        }
    }

    private fun allFieldsAreFilled(): Boolean = with(binding) {
        return@with !(editTextImageUrl.text.isEmpty() || editTextExerciseEquipment.text.isEmpty()
                || editTextExerciseName.text.isEmpty() || editTextExerciseInstructions.text.isEmpty()
                || editTextExerciseTargetMuscle.text.isEmpty() || editTextExerciseSecondary.text.isEmpty())
    }

}