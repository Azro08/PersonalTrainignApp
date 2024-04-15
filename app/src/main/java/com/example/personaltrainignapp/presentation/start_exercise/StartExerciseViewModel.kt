package com.example.personaltrainignapp.presentation.start_exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.data.repository.WorkoutRepository
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartExerciseViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _workoutSavedState = MutableStateFlow<ScreenState<String>>(ScreenState.Loading())
    val workoutSavedState get() = _workoutSavedState

    fun saveWorkout(workout: Workout) = viewModelScope.launch {
        workoutRepository.saveWorkout(workout).let {
            if (it == "Done") {
                _workoutSavedState.value = ScreenState.Success("Saved!")
            } else _workoutSavedState.value = ScreenState.Error(it)
        }
    }

}