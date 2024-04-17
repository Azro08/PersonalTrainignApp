package com.example.personaltrainignapp.presentation.create_exercise

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.repository.ExercisesRepository
import com.example.personaltrainignapp.util.ScreenState
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateExerciseViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {
    private val _saveExerciseState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val saveExerciseState get() = _saveExerciseState

    private val _imageUploaded = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val imageUploaded get() = _imageUploaded

    fun uploadImageAndGetUri(userId: String, imageUri: Uri) = viewModelScope.launch {
        try {
            exercisesRepository.uploadImageAndGetUri(userId, imageUri).let {
                Log.d("SaveState Vm im", it.toString())
                _imageUploaded.value = ScreenState.Success(it)
            }
        } catch (e: Exception) {
            _imageUploaded.value = ScreenState.Error(e.message.toString())
        }
    }

    fun saveExercise(exercise: Exercise) = viewModelScope.launch {
        try {
            exercisesRepository.saveExercise(exercise).let {
                Log.d("SaveState Vm sa", it)
                if (it == "Done") _saveExerciseState.value = ScreenState.Success(it)
                else _saveExerciseState.value = ScreenState.Error(it)
            }
        } catch (e: FirebaseException) {
            _saveExerciseState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

}