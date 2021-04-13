package com.example.theflickrgalary.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.theflickrgalary.R
import com.example.theflickrgalary.adapters.ImageItemClicked
import com.example.theflickrgalary.adapters.ImgRecyclerAdapter
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity(), ImageItemClicked {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getApiModel()

        val recyclerView = findViewById<RecyclerView>(R.id.image_recycler_view)

        viewModel.myResponse.observe(this, { response ->
            recyclerView.adapter = ImgRecyclerAdapter(this, response.photos.photo, this)
        })
    }

    override fun onItemClick(url: String) {
        val intent = Intent(this, ViewImageActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}