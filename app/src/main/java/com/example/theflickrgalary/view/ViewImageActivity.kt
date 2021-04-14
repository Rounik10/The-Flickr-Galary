package com.example.theflickrgalary.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.theflickrgalary.adapters.ViewPagerAdapter
import com.example.theflickrgalary.databinding.ActivityViewImageBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory

class ViewImageActivity : AppCompatActivity() {

    private lateinit var url: String
    private lateinit var binding:ActivityViewImageBinding
    private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        url = intent.getStringExtra("url").toString()
        val position = intent.getIntExtra("position",0)

        val fragmentList = ArrayList<Fragment>()
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getApiModel()

        viewModel.myResponse.observe(this, {
            val photoList = it.photos.photo
            for(item: Photo in photoList) {
                fragmentList.add(ImageFragment(item.url_s))
            }
            val adapter = ViewPagerAdapter(
                fragmentList,
                supportFragmentManager,
                lifecycle
            )
            binding.viewPager.adapter = adapter
            binding.viewPager.currentItem = position
        })

        binding.shareBtn.setOnClickListener { shareImage() }
    }

    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Checkout this image:\n$url")
        val chooser = Intent.createChooser(intent, "Share the Image Via...")
        startActivity(chooser)
    }
}