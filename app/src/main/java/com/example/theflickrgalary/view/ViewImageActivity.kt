package com.example.theflickrgalary.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.theflickrgalary.R
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ViewImageActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var photoView: PhotoView
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        supportActionBar?.hide()

        url = intent.getStringExtra("url").toString()
        photoView = findViewById(R.id.photo_view)
        Glide
            .with(this)
            .load(url)
            .placeholder(R.drawable.ic_placeholder)
            .into(photoView)

        photoView.setOnViewTapListener { _: View, _: Float, _: Float ->
            toggleFaaVisibility()
        }

        fab = findViewById(R.id.share_btn)
        fab.setOnClickListener { shareImage() }
    }

    private fun toggleFaaVisibility() {
        if (fab.isVisible) {
            fab.visibility = View.GONE
        } else {
            fab.visibility = View.VISIBLE
        }
    }

    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Checkout this image:\n$url")
        val chooser = Intent.createChooser(intent, "Share the Image Via...")
        startActivity(chooser)
    }
}