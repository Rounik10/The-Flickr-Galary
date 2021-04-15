package com.example.theflickrgalary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.theflickrgalary.adapters.ImageItemClicked
import com.example.theflickrgalary.adapters.ImgRecyclerAdapter
import com.example.theflickrgalary.databinding.FragmentHomeBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory

class HomeFragment : Fragment(), ImageItemClicked {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getApiModel()

        recyclerView = binding.imageRecyclerView
        observeApi()

        binding.swipeRefresh.setOnRefreshListener {
            observeApi()
        }

        return binding.root
    }

    private fun observeApi() {
        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            recyclerView.adapter = ImgRecyclerAdapter(
                requireContext(),
                response.photos.photo,
                this
            )
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: Photo, position: Int) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToViewImgFragment(photo.url_s, position)
        Navigation.findNavController(binding.root).navigate(action)
    }
}