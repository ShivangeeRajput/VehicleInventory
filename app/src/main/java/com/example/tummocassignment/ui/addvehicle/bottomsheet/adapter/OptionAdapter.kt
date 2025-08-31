package com.example.tummocassignment.ui.addvehicle.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.R
import com.example.tummocassignment.databinding.ItemOptionBinding

class OptionAdapter(
    private val options: List<OptionItem>,
    private val onItemClick: (OptionItem) -> Unit
) : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    data class OptionItem(val name: String, val iconRes: Int? = null)

    inner class OptionViewHolder(val binding: ItemOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OptionItem, isSelected: Boolean) {
            binding.tvName.text = item.name

            if (item.iconRes != null) {
                binding.imgIcon.setImageResource(item.iconRes)
                binding.imgIcon.visibility = View.VISIBLE
            } else {
                binding.imgIcon.visibility = View.GONE
            }

            // radio icon - filled when selected, empty when not selected
            binding.imgSelected.setImageResource(
                if (isSelected) R.drawable.ic_radio_checked else R.drawable.ic_radio_unchecked
            )

            // background - blue border when selected, gray border when not selected
            binding.root.background = ContextCompat.getDrawable(
                binding.root.context,
                if (isSelected) R.drawable.bg_option_selected else R.drawable.bg_option_unselected
            )

            // Add margin between items
            val layoutParams = binding.root.layoutParams as RecyclerView.LayoutParams
            if (adapterPosition == 0) {
                layoutParams.topMargin = 16.dpToPx()
            } else {
                layoutParams.topMargin = 8.dpToPx()
            }
            layoutParams.bottomMargin = 8.dpToPx()
            binding.root.layoutParams = layoutParams

            // click listener
            binding.root.setOnClickListener {
                val previousPos = selectedPosition
                val currentPos = adapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    selectedPosition = currentPos
                    if (previousPos != RecyclerView.NO_POSITION) {
                        notifyItemChanged(previousPos)
                    }
                    notifyItemChanged(selectedPosition)
                    onItemClick(item)
                }
            }
        }

        private fun Int.dpToPx(): Int {
            val density = binding.root.context.resources.displayMetrics.density
            return (this * density).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = options[position]
        holder.bind(item, position == selectedPosition)
    }

    override fun getItemCount() = options.size
}


