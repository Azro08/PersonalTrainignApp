package com.example.personaltrainignapp.presentation.exercises.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.personaltrainignapp.R
import com.example.personaltrainignapp.databinding.BodypartItemBinding

class BodyPartRvAdapter(
    private val bodyPartsList: List<String>,
    private val listener: (bodyPart: String) -> Unit
) : RecyclerView.Adapter<BodyPartRvAdapter.BodyPartViewHolder>() {

    private var lastClickedIndex: Int = -1

    class BodyPartViewHolder(
        private var adapter: BodyPartRvAdapter,
        listener: (bodyPart: String) -> Unit,
        private val binding: BodypartItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var bodyPart: String? = null
        fun bind(curBodyPart: String, position: Int) {
            binding.textViewBodyPart.text = curBodyPart
            Log.d("current bodyPart", curBodyPart)
            bodyPart = curBodyPart

            if (adapter.lastClickedIndex == position) {
                // Set background for the last clicked item
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.dark_orange
                    )
                )
                binding.textViewBodyPart.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.dark_blue
                    )
                )
            } else {
                // Set the default background for other items
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.dark_blue2
                    )
                )
                binding.textViewBodyPart.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }
        }

        init {
            binding.root.setOnClickListener {
                listener(bodyPart!!)
                // Update the background for the last clicked item
                if (adapter.lastClickedIndex != -1) {
                    adapter.notifyItemChanged(adapter.lastClickedIndex)
                }

                // Update the background for the currently clicked item
                adapter.lastClickedIndex = adapterPosition
                adapter.notifyItemChanged(adapterPosition)

                // Notify the listener
                adapter.listener(bodyPart!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyPartViewHolder {
        return BodyPartViewHolder(
            this,
            listener,
            BodypartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return bodyPartsList.size
    }

    override fun onBindViewHolder(holder: BodyPartViewHolder, position: Int) {
        holder.bind(bodyPartsList[position], position)
    }

}