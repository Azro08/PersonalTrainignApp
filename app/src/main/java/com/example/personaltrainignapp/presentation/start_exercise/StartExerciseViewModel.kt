package com.example.personaltrainignapp.presentation.start_exercise

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.data.model.toExercise
import com.example.personaltrainignapp.data.repository.ExercisesRepository
import com.example.personaltrainignapp.data.repository.WorkoutRepository
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartExerciseViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exercisesRepository: ExercisesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _workoutSavedState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val workoutSavedState get() = _workoutSavedState

    private val _exerciseDetailsState =
        MutableStateFlow<ScreenState<Exercise>>(ScreenState.Loading())
    val exerciseDetailsState get() = _exerciseDetailsState

    init {
        savedStateHandle.get<String>(Constants.EXERCISE_KEY)?.let { exerciseId ->
            Log.d("WorkoutSave", exerciseId)
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

    fun saveWorkout(workout: Workout) = viewModelScope.launch {
        workoutRepository.saveWorkout(workout).let {
            if (it == "Done") {
                _workoutSavedState.value = ScreenState.Success("Saved!")
            } else _workoutSavedState.value = ScreenState.Error(it)
        }
    }

}