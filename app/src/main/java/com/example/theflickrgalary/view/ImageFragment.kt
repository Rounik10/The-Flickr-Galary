package com.example.theflickrgalary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
                .into(binding.photoView)
        }
        binding.photoView.setOnViewTapListener { view: View, fl: Float, fl1: Float ->
            toggleFabVisibility()
        }
        return binding.root
    }

    private fun toggleFabVisibility() {
        val fab = activity?.findViewById<FloatingActionButton>(R.id.share_btn)!!
        if (fab.isVisible) {
            fab.visibility = View.GONE
        } else {
            fab.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}