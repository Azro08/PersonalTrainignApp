package com.example.personaltrainignapp.presentation.create_exercise

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateExerciseFragment : Fragment(R.layout.fragment_create_exercise) {
    private val binding by viewBinding(FragmentCreateExerciseBinding::bind)
    private val viewModel: CreateExerciseViewModel by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri = Uri.parse("")
    private var imageUrl = ""

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    override fun onAttach(context: Context) {
        super.onAttach(context)
        setMediaPicker()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonSaveExercise.setOnClickListener {
            if (allFieldsAreFilled()) {
                if (imageUri != Uri.parse("")) uploadImage()
                else saveExercise()
            } else Toast.makeText(
                requireContext(),
                getString(R.string.fill_upFields),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.imageViewNewExerciseImage.setOnClickListener {
            addImages()
        }

        setSpinner()

    }

    private fun setSpinner() {
        val bodyParts = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            Constants.getBodyPartsList()
        )
        binding.spinnerBodyPart.adapter = bodyParts
    }

    private fun uploadImage() {
        lifecycleScope.launch {
            Log.d("ImageUri", imageUri.toString())
            viewModel.uploadImageAndGetUri(firebaseAuth.currentUser?.uid!!, imageUri)
            viewModel.imageUploaded.collect { state ->
                Log.d("SaveStateImage", state.message.toString() + " " + state.data.toString())
                when (state) {
                    is ScreenState.Loading -> {
                        binding.buttonSaveExercise.visibility = View.GONE
                        binding.imageViewLoadingGif.visibility = View.VISIBLE
                    }

                    is ScreenState.Success -> {
                        imageUrl = state.data!!
                        saveExercise()
                    }

                    is ScreenState.Error -> {
                        binding.buttonSaveExercise.visibility = View.VISIBLE
                        binding.imageViewLoadingGif.visibility = View.GONE
                        Log.d("ImageUriError", state.message.toString())
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun addImages() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setMediaPicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(binding.root).load(uri)
                    .error(R.drawable.fitness)
                    .into(binding.imageViewNewExerciseImage)
                imageUri = uri
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context?.contentResolver?.takePersistableUriPermission(uri, flag)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    private fun saveExercise() {
        lifecycleScope.launch {
            val exerciseId = Constants.generateRandomId()
            val name = binding.editTextExerciseName.text.toString()
            val bodyPart = binding.spinnerBodyPart.selectedItem.toString()
            val targetMuscle = binding.editTextExerciseTargetMuscle.text.toString()
            val secondaryMuscle = listOf(binding.editTextExerciseSecondary.text.toString())
            val equipment = binding.editTextExerciseEquipment.text.toString()
            val instructions = listOf(binding.editTextExerciseInstructions.text.toString())
            val newExercise = Exercise(
                id = exerciseId,
                name = name,
                gifUrl = imageUrl,
                target = targetMuscle,
                bodyPart = bodyPart,
                secondaryMuscles = secondaryMuscle,
                equipment = equipment,
                instructions = instructions
            )

            viewModel.saveExercise(newExercise)
            viewModel.saveExerciseState.collect {
                Log.d("SaveState", it.message.toString() + " " + it.data.toString())
                when (it) {
                    is ScreenState.Loading -> {Toast.makeText(requireContext(), getString(R.string.upload), Toast.LENGTH_SHORT).show()}
                    is ScreenState.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.buttonSaveExercise.visibility = View.VISIBLE
                        binding.imageViewLoadingGif.visibility = View.GONE
                    }

                    is ScreenState.Success -> {
                        Log.d("SaveState is Saved", it.message.toString() + " " + it.data.toString())

                        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }

        }
    }

    private fun allFieldsAreFilled(): Boolean = with(binding) {
        return@with !(editTextExerciseEquipment.text.isEmpty()
                || editTextExerciseName.text.isEmpty() || editTextExerciseInstructions.text.isEmpty()
                || spinnerBodyPart.selectedItem.toString()
            .isEmpty() || editTextExerciseSecondary.text.isEmpty() || editTextExerciseTargetMuscle.text.isEmpty())
    }

}