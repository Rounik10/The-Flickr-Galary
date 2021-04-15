package com.example.theflickrgalary.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.theflickrgalary.R
import com.example.theflickrgalary.model.Photo

class ImgRecyclerAdapter(
    private val context: Context,
    private val list: ArrayList<Photo>,
    private val clickListener: ImageItemClicked
) :
    RecyclerView.Adapter<ImgRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image_view)
        val cardView: CardView = itemView.findViewById(R.id.img_card)
        val root: ConstraintLayout = itemView.findViewById(R.id.constraint_layout)
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
        val constraintSet = ConstraintSet()
        constraintSet.clone(holder.root)
        constraintSet.setDimensionRatio(
            holder.cardView.id,
            "${list[position].width_s}:${list[position].height_s}"
        )
        constraintSet.applyTo(holder.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList:List<Photo>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

}

interface ImageItemClicked {
    fun onItemClick(photo: Photo, position: Int)
}