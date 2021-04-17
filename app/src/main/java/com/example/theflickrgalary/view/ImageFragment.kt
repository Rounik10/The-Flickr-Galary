package com.example.theflickrgalary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.theflickrgalary.R
import com.example.theflickrgalary.databinding.FragmentImageBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ImageFragment(private val url:String) : Fragment() {

    private var _binding:FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        context?.let {
            Glide.with(it)
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.photoView)
        }
        binding.photoView.setOnViewTapListener { _: View, _: Float, _: Float ->
            toggleFabVisibility()
        }
        return binding.root
    }

    private fun toggleFabVisibility() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.share_btn)!!
        val actionBar = (requireActivity() as AppCompatActivity)
        if (fab.isVisible) {
            fab.visibility = View.GONE
            actionBar.supportActionBar?.hide()
        } else {
            fab.visibility = View.VISIBLE
            actionBar.supportActionBar?.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}