package com.nazirman.sub1storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nazirman.sub1storyapp.R
import com.nazirman.sub1storyapp.model.Story


class StoryAdapter(private val listStories: ArrayList<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPhoto: ImageView = view.findViewById(R.id.img_photo)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val desc: TextView = view.findViewById(R.id.tv_desc)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.stories_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvName.text = listStories[position].name
        viewHolder.desc.text = listStories[position].description
        Glide.with(viewHolder.itemView.context)
            .load(listStories[position].photo)
            .centerCrop()
            .into(viewHolder.imgPhoto)
    }

    override fun getItemCount(): Int {
        return listStories.size
    }
}