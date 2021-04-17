package com.example.theflickrgalary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.theflickrgalary.R
import com.example.theflickrgalary.databinding.ImageRecyclerItemBinding
import com.example.theflickrgalary.model.Photo

class PagingAdapter(private val listener: RecyclerItemClicked) :
    PagingDataAdapter<Photo, PagingAdapter.PagingViewHolder>(PHOTO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val binding =
            ImageRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
        holder.itemView.setOnClickListener {
            if (currentItem != null) {
                listener.onItemClick(currentItem, position)
            } else {
                Toast.makeText(it.context, "Error!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class PagingViewHolder(private val binding: ImageRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.apply {
                Glide.with(itemView)
                    .load(photo.url_s)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(itemImageView)
            }

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.root)
            constraintSet.setDimensionRatio(
                binding.imgCard.id,
                "${photo.width_s}:${photo.height_s}"
            )
            constraintSet.applyTo(binding.root)
        }
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }

}

interface RecyclerItemClicked {
    fun onItemClick(photo: Photo, position: Int)
}