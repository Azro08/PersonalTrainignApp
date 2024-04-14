package com.example.personaltrainignapp.presentation.exercises.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Exercise
import com.example.personaltrainignapp.databinding.ExerciseItemBinding

class ExercisesRvAdapter(
    private var exercisesList: List<Exercise>,
    private val clickListener: (exercise: Exercise) -> Unit
) : RecyclerView.Adapter<ExercisesRvAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(
        clickListener: (exercise: Exercise) -> Unit,
        private val binding: ExerciseItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var exercise: Exercise? = null

        fun bind(currentExercise: Exercise) {
            Glide.with(binding.root)
                .load(currentExercise.gifUrl)
                .error(R.drawable.fitness_logo)
                .into(binding.imageViewExerciseImage)
            binding.textViewExerciseName.text = currentExercise.name
            exercise = currentExercise
        }

        init {
            binding.root.setOnClickListener { clickListener(exercise!!) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(
            clickListener,
            ExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return exercisesList.size
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.bind(exercisesList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateExercisesList(filteredList: List<Exercise>) {
        exercisesList = filteredList.toMutableList()
        notifyDataSetChanged()
    }

}