package com.example.personaltrainignapp.presentation.history_details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.Workout
import com.example.personaltrainignapp.databinding.HistoryWorkoutItemBinding

class WorkoutsRvAdapter(
    private var workoutsList: List<Workout>,
    private val clickListener: (workoutId: String) -> Unit
) : RecyclerView.Adapter<WorkoutsRvAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(
        clickListener: (workoutId: String) -> Unit,
        private val binding: HistoryWorkoutItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var workout: Workout? = null

        fun bind(currentWorkout: Workout) {
            Glide.with(binding.root)
                .load(currentWorkout.exercise.gifUrl)
                .error(R.drawable.fitness_logo)
                .into(binding.imageViewHistoryExerciseImage)
            binding.textViewHistoryName.text = currentWorkout.exercise.name
            val weights = currentWorkout.weights.toString() + " (kg)"
            val reps = "X" + currentWorkout.reps.toString()
            binding.textViewHistoryReps.text = reps
            binding.textViewHistoryWeights.text = weights
            workout = currentWorkout
        }

        init {
            binding.root.setOnClickListener { clickListener(workout!!.exercise.id) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            clickListener,
            HistoryWorkoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return workoutsList.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workoutsList[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateExercisesList(filteredList: List<Workout>) {
        workoutsList = filteredList.toMutableList()
        notifyDataSetChanged()
    }

}