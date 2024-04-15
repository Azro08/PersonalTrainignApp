package com.example.personaltrainignapp.presentation.history_details

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.databinding.FragmentHistoryDetailsBinding
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryDetailsFragment : Fragment(R.layout.fragment_history_details) {
    private val binding by viewBinding(FragmentHistoryDetailsBinding::bind)
    private val viewModel: HistoryDetailsViewModel by viewModels()
    private var workoutsRvAdapter: WorkoutsRvAdapter? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(Constants.DATE_KEY).let {
            binding.textViewHistoryDate.text = it
        }
        getWorkoutHistory()
    }

    private fun getWorkoutHistory() {
        lifecycleScope.launch {
            viewModel.workoutHistoryState.collect {
                when (it) {
                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        handleError(it.message)
                    }

                    is ScreenState.Success -> {
                        showWorkouts(it.data)
                    }
                }
            }
        }
    }

    private fun showWorkouts(workouts: List<Workout>?) {
        binding.rvWorkouts.visibility = View.VISIBLE
        binding.textViewError.visibility = View.GONE
        workoutsRvAdapter = WorkoutsRvAdapter(workouts!!) {
            navToExerciseDetails(it)
        }
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWorkouts.setHasFixedSize(true)
        binding.rvWorkouts.adapter = workoutsRvAdapter
    }

    private fun navToExerciseDetails(exerciseId: String) {
        findNavController().navigate(
            R.id.nav_history_to_exercise_details,
            bundleOf(Pair(Constants.EXERCISE_KEY, exerciseId))
        )
    }

    private fun handleError(message: String?) {
        binding.rvWorkouts.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message ?: "No workouts on this day"
    }

    override fun onDestroy() {
        super.onDestroy()
        workoutsRvAdapter = null
    }

}