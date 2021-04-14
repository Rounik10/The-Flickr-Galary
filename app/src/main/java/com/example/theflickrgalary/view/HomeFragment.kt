package com.example.theflickrgalary.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getApiModel()

        val recyclerView = binding.imageRecyclerView

        viewModel.myResponse.observe(viewLifecycleOwner, { response ->
            recyclerView.adapter = ImgRecyclerAdapter (
                    requireContext(),
                    response.photos.photo,
                    this
                )
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: Photo, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToViewImgFragment(photo.url_s, position)
        Navigation.findNavController(binding.root).navigate(action)
    }
}