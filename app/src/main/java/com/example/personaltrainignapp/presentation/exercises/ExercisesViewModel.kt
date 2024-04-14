package com.example.personaltrainignapp.presentation.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.data.model.toExercise
import com.example.personaltrainignapp.data.repository.ExercisesRepository
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository
) : ViewModel() {

    private val _exercisesState =
        MutableStateFlow<ScreenState<List<Exercise>>>(ScreenState.Loading())
    val exercisesState get() = _exercisesState

    fun getExercisesListByMuscle(bodyPart: String) = viewModelScope.launch {
        try {
            val exercises = exercisesRepository.getExercises(bodyPart)
            _exercisesState.value = ScreenState.Success(exercises.map { it.toExercise() })
        } catch (e: Exception) {
            _exercisesState.value = ScreenState.Error(e.message ?: "No exercises found!")
        }
    }

    fun refresh(bodyPart: String) {
        getExercisesListByMuscle(bodyPart)
    }

    fun filterExercisesList(query: String): List<Exercise> {
        return when (val currentState = _exercisesState.value) {
            is ScreenState.Success -> {
                currentState.data?.filter { exercise ->
                    exercise.name.contains(query, ignoreCase = true)
                } ?: emptyList()
            }

            else -> emptyList()
        }
    }

}
