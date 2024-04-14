package com.example.personaltrainignapp.presentation.exercises

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.ExerciseDto
import com.example.personaltrainignapp.data.repository.ExercisesRepository
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exercisesRepository: ExercisesRepository
) : ViewModel(){

    private val _exercisesState = MutableStateFlow<ScreenState<List<ExerciseDto>>>(ScreenState.Loading())
    val exercisesState get() = _exercisesState

    fun getExercisesListByMuscle(bodyPart : String) = viewModelScope.launch {
        try {
            val exercises = exercisesRepository.getExercises(bodyPart)
            _exercisesState.value = ScreenState.Success(exercises)
        } catch (e: Exception) {
            _exercisesState.value = ScreenState.Error(e.message ?: "No exercises found!")
        }
    }

}