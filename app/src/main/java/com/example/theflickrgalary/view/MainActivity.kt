package com.example.theflickrgalary.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.theflickrgalary.adapters.ImageItemClicked
import com.example.theflickrgalary.adapters.ImgRecyclerAdapter
import com.example.theflickrgalary.databinding.ActivityMainBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory
import java.text.FieldPosition

class MainActivity : AppCompatActivity(), ImageItemClicked {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getApiModel()

        val recyclerView = binding.imageRecyclerView

        viewModel.myResponse.observe(this, { response ->
            recyclerView.adapter = ImgRecyclerAdapter(this, response.photos.photo, this)
        })
    }

    override fun onItemClick(photo: Photo, position: Int) {
        val intent = Intent(this, ViewImageActivity::class.java)
        intent.putExtra("url", photo.url_s)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}