package com.example.personaltrainignapp.presentation.exercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.databinding.FragmentExerciseListBinding
import com.example.personaltrainignapp.presentation.exercises.adapter.BodyPartRvAdapter
import com.example.personaltrainignapp.presentation.exercises.adapter.ExercisesRvAdapter
import com.example.personaltrainignapp.util.Constants
import com.example.personaltrainignapp.util.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseListFragment : Fragment(R.layout.fragment_exercise_list) {
    private val binding by viewBinding(FragmentExerciseListBinding::bind)
    private val viewModel: ExercisesViewModel by viewModels()
    private var bodyPartRvAdapter: BodyPartRvAdapter? = null
    private var exercisesRvAdapter: ExercisesRvAdapter? = null
    private var currentBodyPart = "back"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getBodyParts()
        getExercises()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh(currentBodyPart)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        search()
    }

    private fun search() {
        binding.editTextSearchExercise.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().trim()
                performSearch(searchText)
            }
        })
    }

    private fun performSearch(query: String) {
        val filteredList = viewModel.filterExercisesList(query)
        exercisesRvAdapter?.updateExercisesList(filteredList)
    }

    private fun getBodyParts() {
        val bodyParts = Constants.getBodyPartsList()
        bodyPartRvAdapter = BodyPartRvAdapter(bodyParts) {
            currentBodyPart = it
            getExercises()
        }
        binding.rvBodyParts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvBodyParts.setHasFixedSize(true)
        binding.rvBodyParts.adapter = bodyPartRvAdapter
    }

    private fun getExercises() {
        lifecycleScope.launch {
            viewModel.getExercisesListByMuscle(currentBodyPart)
            viewModel.exercisesState.collect { state ->
                when (state) {

                    is ScreenState.Loading -> {}
                    is ScreenState.Error -> {
                        handleError(state.message)
                    }

                    is ScreenState.Success -> {
                        if (!state.data.isNullOrEmpty()) showExercises(state.data)
                        else handleError(state.message)
                    }
                }
            }
        }
    }

    private fun showExercises(exercises: List<Exercise>) {
        Log.d("Exercises list", exercises.toString())
        binding.searchLayout.visibility = View.VISIBLE
        binding.rvBodyParts.visibility = View.VISIBLE
        binding.swipeRefreshLayout.visibility = View.VISIBLE
        binding.textViewError.visibility = View.GONE
        binding.loadingGif.visibility = View.GONE
        exercisesRvAdapter = ExercisesRvAdapter(exercises) {
            navToDetails(it)
        }
        binding.rvExercises.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvExercises.setHasFixedSize(true)
        binding.rvExercises.adapter = exercisesRvAdapter
    }

    private fun navToDetails(it: Exercise) {
        val bundle = bundleOf(Pair(Constants.EXERCISE_KEY, it.id))
        findNavController().navigate(R.id.nav_exercise_to_details, bundle)
    }

    private fun handleError(message: String?) {
        binding.searchLayout.visibility = View.GONE
        binding.rvBodyParts.visibility = View.GONE
        binding.swipeRefreshLayout.visibility = View.VISIBLE
        binding.loadingGif.visibility = View.GONE
        binding.textViewError.visibility = View.VISIBLE
        binding.textViewError.text = message ?: "Failed to load exercise!"
    }

}