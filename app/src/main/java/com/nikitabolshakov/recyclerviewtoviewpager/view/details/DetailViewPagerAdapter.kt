package com.nikitabolshakov.recyclerviewtoviewpager.view.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nikitabolshakov.recyclerviewtoviewpager.R
import com.nikitabolshakov.recyclerviewtoviewpager.model.Sports


class DetailViewPagerAdapter(private val viewPager2: ViewPager2) :
    ListAdapter<Sports, DetailViewPagerAdapter.DetailViewHolder>(DiffCallback) {


    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val toolbar: Toolbar = itemView.findViewById(R.id.detail_toolbar)
        private val appBarLayout: AppBarLayout = itemView.findViewById(R.id.detail_appbar)
        val collapsingToolbar: CollapsingToolbarLayout =
            itemView.findViewById(R.id.detail_collapsing_toolbar)
        private val icon: ImageView = itemView.findViewById(R.id.detail_image_view)
        private val title: TextView = itemView.findViewById(R.id.title_detail_text_view)
        private val subtitle: TextView = itemView.findViewById(R.id.subtitle_detail_text_view)
        private val about: TextView = itemView.findViewById(R.id.about_detail_text_view)
        val fab: FloatingActionButton = itemView.findViewById(R.id.fab)

        fun bind(sportsArgs: Sports) {
            icon.setImageResource(sportsArgs.icon)
            title.text = sportsArgs.title
            subtitle.text = sportsArgs.subtitle
            about.text = sportsArgs.about
            appBarLayout.addOnOffsetChangedListener(appBarLayoutListener(sportsArgs))
            toolbar.setNavigationOnClickListener { v ->
                Navigation.findNavController(v).navigateUp()
            }
        }

        private fun appBarLayoutListener(sportsArgs: Sports): AppBarLayout.OnOffsetChangedListener =
            object : AppBarLayout.OnOffsetChangedListener {
                var isShow = false
                var scrollRange = -1
                override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        // Show title when completely collapsed.
                        collapsingToolbar.title = sportsArgs.title
                        isShow = true
                    } else if (isShow) {
                        // Hide title when collapsedToolBar is completely visible using empty string.
                        collapsingToolbar.title = ""
                        isShow = false
                    }
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder {
        return DetailViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_detail_adapter,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val sportsArgs: Sports = getItem(position)
        holder.bind(sportsArgs)

        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                when (state) {
                    SCROLL_STATE_DRAGGING -> {
                        holder.toolbar.navigationIcon = null
                        holder.fab.visibility = View.INVISIBLE
                    }
                    SCROLL_STATE_IDLE -> {
                        holder.toolbar.setNavigationIcon(R.drawable.ic_up_button)
                        holder.fab.visibility = View.VISIBLE
                    }
                    SCROLL_STATE_SETTLING -> {
                        holder.toolbar.navigationIcon = null
                        holder.fab.visibility = View.INVISIBLE
                    }
                }
            }
        })
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