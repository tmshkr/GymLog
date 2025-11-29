package com.example.gymlog.viewmodels


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gymlog.R
import com.example.gymlog.data.GymLog


class GymLogAdapter : ListAdapter<GymLog, GymLogAdapter.GymLogViewHolder>(GYM_LOG_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GymLogViewHolder {
        return GymLogViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GymLogViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.exerciseName)
    }

    class GymLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gymLogItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            gymLogItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): GymLogViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return GymLogViewHolder(view)
            }
        }
    }

    companion object {
        private val GYM_LOG_COMPARATOR = object : DiffUtil.ItemCallback<GymLog>() {
            override fun areItemsTheSame(oldItem: GymLog, newItem: GymLog): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: GymLog, newItem: GymLog): Boolean {
                //TODO: use udatedAt
                return oldItem.id == newItem.id
            }
        }
    }
}