package com.nikitabolshakov.recyclerviewtoviewpager.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikitabolshakov.recyclerviewtoviewpager.R
import com.nikitabolshakov.recyclerviewtoviewpager.model.Sports

class SportsAdapter(
    private val sportsList: List<Sports>,
    private val onClickListener: OnClickListener
) :
    ListAdapter<Sports, SportsAdapter.SportsViewHolder>(
        DiffCallback
    ) {

    class SportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.social_item_image_view)
        private val titleTextView: TextView = itemView.findViewById(R.id.title_item_text_view)
        private val subtitleTextView: TextView = itemView.findViewById(R.id.subtitle_item_text_view)

        fun bind(
            sports: Sports,
            onClickListener: OnClickListener
        ) {
            iconImageView.setImageResource(sports.icon)
            titleTextView.text = sports.title
            subtitleTextView.text = sports.subtitle
            itemView.setOnClickListener {
                onClickListener.onClick(sports)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsViewHolder {
        return SportsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SportsViewHolder, position: Int) {
        val sports: Sports = sportsList[position]
        holder.bind(sports, onClickListener)
    }

    override fun getItemCount() = sportsList.size

    class OnClickListener(val clickListener: (sports: Sports) -> Unit) {
        fun onClick(sports: Sports) = clickListener(sports)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Sports>() {
        override fun areItemsTheSame(oldItem: Sports, newItem: Sports): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Sports, newItem: Sports): Boolean {
            return oldItem.title == newItem.title
        }
    }
}