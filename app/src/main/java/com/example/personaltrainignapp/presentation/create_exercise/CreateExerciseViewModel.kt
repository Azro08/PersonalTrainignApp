package com.example.personaltrainignapp.presentation.create_exercise

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
    private val _saveExerciseState = MutableStateFlow<ScreenState<Exercise>>(ScreenState.Loading())
    val saveExerciseState get() = _saveExerciseState

    fun saveExercise(exercise: Exercise) = viewModelScope.launch {
        try {
            exercisesRepository.saveExercise(exercise)
        } catch (e : FirebaseException){
            _saveExerciseState.value = ScreenState.Error(e.message ?: "Unknown error")
        }
    }

}