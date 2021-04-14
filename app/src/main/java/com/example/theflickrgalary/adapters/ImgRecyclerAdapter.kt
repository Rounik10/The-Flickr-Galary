package com.example.theflickrgalary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.theflickrgalary.R
import com.example.theflickrgalary.model.Photo

class ImgRecyclerAdapter(
    private val context: Context,
    private val list: List<Photo>,
    private val clickListener: ImageItemClicked
) :
    RecyclerView.Adapter<ImgRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = list[position].url_s
        Glide
            .with(context)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageView)

        holder.imageView.setOnClickListener {
            clickListener.onItemClick(list[position], position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface ImageItemClicked {
    fun onItemClick(photo: Photo, position: Int)
}