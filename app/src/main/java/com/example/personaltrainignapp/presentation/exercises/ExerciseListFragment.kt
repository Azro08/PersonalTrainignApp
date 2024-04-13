package com.example.personaltrainignapp.presentation.exercises

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.databinding.FragmentExerciseListBinding

class ExerciseListFragment : Fragment(R.layout.fragment_exercise_list) {
    private val binding by viewBinding(FragmentExerciseListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}