package com.example.personaltrainignapp.presentation.history_details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.data.repository.WorkoutRepository
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _workoutHistoryState =
        MutableStateFlow<ScreenState<List<Workout>>>(ScreenState.Loading())
    val workoutHistoryState get() = _workoutHistoryState

    init {
        savedStateHandle.get<String>(Constants.DATE_KEY)?.let { date ->
            Log.d("History", date)
            getHistoryByDate(date)
        }
    }

    private fun getHistoryByDate(date: String) = viewModelScope.launch {
        workoutRepository.getWorkoutHistoryByDate(date).let { workoutList ->
            if (workoutList.isNotEmpty()) _workoutHistoryState.value =
                ScreenState.Success(workoutList)
            else _workoutHistoryState.value = ScreenState.Error("No workouts on this day")
        }
    }

}