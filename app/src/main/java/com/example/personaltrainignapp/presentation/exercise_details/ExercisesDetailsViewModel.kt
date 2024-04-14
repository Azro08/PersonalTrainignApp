package com.example.personaltrainignapp.presentation.exercise_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.toExercise
import com.example.personaltrainignapp.data.repository.ExercisesRepository
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesDetailsViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _exerciseDetailsState =
        MutableStateFlow<ScreenState<Exercise>>(ScreenState.Loading())
    val exerciseDetailsState get() = _exerciseDetailsState

    init {
        savedStateHandle.get<String>(Constants.EXERCISE_KEY)?.let { exerciseId ->
            getExerciseById(exerciseId)
        }
    }

    private fun getExerciseById(exerciseId: String) = viewModelScope.launch {
        try {
            exercisesRepository.getExerciseById(exerciseId).toExercise().let {
                _exerciseDetailsState.value = ScreenState.Success(it)
            }
        } catch (e: Exception) {
            _exerciseDetailsState.value = ScreenState.Error(e.message ?: "Failed to load details")
        }
    }

}