package com.example.personaltrainignapp.presentation.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.data.model.WorkoutDay
import com.google.gson.Gson

class WorkoutDayAdapter(
    private val context: Context,
    private val workoutDays: List<WorkoutDay>,
    private val currentDay: Int
) : RecyclerView.Adapter<WorkoutDayAdapter.WorkoutDayViewHolder>() {

    class WorkoutDayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.textViewDay)
        val planTextView: TextView = itemView.findViewById(R.id.textViewPlan)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutDayViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_workout_day, parent, false)
        return WorkoutDayViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkoutDayViewHolder, position: Int) {
        val workoutDay = workoutDays[position]
        val dayText = "Day ${workoutDay.dayNumber}"
        holder.dayTextView.text = dayText
        holder.planTextView.text = workoutDay.plan

        // Set different color for current day
        if (workoutDay.dayNumber == currentDay) {
            holder.itemView.setBackgroundColor(
                context.resources.getColor(
                    R.color.dark_blue2,
                    context.theme
                )
            ) // Change to your desired color
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        // Set checkbox state based on completion status
        holder.checkBox.isChecked = workoutDay.completed

        // Set click listener for checkbox to update completion status
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            workoutDay.completed = isChecked
            saveWorkoutDaysToSharedPreferences() // Save workout days after state change
        }
    }

    override fun getItemCount(): Int {
        return workoutDays.size
    }

    private fun saveWorkoutDaysToSharedPreferences() {
        // Serialize the list of WorkoutDay objects to JSON using Gson
        val gson = Gson()
        val json = gson.toJson(workoutDays)

        // Save the JSON string to SharedPreferences
        val sharedPreferences = context.getSharedPreferences("WorkoutDays", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("WorkoutDays", json)
        editor.apply()
    }
}

