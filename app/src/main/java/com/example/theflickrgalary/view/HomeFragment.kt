package com.example.theflickrgalary.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.theflickrgalary.adapters.FlickrLoadStateAdapter
import com.example.theflickrgalary.adapters.PagingAdapter
import com.example.theflickrgalary.adapters.RecyclerItemClicked
import com.example.theflickrgalary.databinding.FragmentHomeBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory

class HomeFragment : Fragment(), RecyclerItemClicked {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Home"
        setHasOptionsMenu(true)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        recyclerView = binding.imageRecyclerView
        adapter = PagingAdapter(this)
        recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FlickrLoadStateAdapter { adapter.retry() },
            footer = FlickrLoadStateAdapter { adapter.retry() }
        )

        viewModel.photos.observe(viewLifecycleOwner, { response ->
            adapter.submitData(viewLifecycleOwner.lifecycle, response)
        })

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
            Handler(Looper.myLooper()!!).postDelayed(1000) {
                binding.swipeRefresh.isRefreshing = false
            }
        }

        adapter.addLoadStateListener {
            binding.progressBar2.isVisible = it.source.refresh is LoadState.Loading
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: Photo, position: Int) {
        val text = "recent"
        val action =
            HomeFragmentDirections.actionHomeFragmentToViewImgFragment(photo.url_s, position, text)
        Navigation.findNavController(binding.root).navigate(action)
    }
}