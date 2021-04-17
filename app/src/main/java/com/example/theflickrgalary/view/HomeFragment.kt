package com.example.theflickrgalary.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.theflickrgalary.R
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
    private lateinit var searchView: SearchView

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
        recyclerView.adapter = adapter

        observeApi()

        binding.swipeRefresh.setOnRefreshListener {
            observeApi()
            Handler(Looper.myLooper()!!).postDelayed(1000) {
                binding.swipeRefresh.isRefreshing = false
            }
        }
        return binding.root
    }

    private fun observeApi() {
        viewModel.photos.observe(viewLifecycleOwner, { response ->
            adapter.submitData(viewLifecycleOwner.lifecycle, response)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: Photo, position: Int) {
        var text = "recent"
        if(searchView.isActivated) {
            text = searchView.query.toString()
        }
        val action =
            HomeFragmentDirections.actionHomeFragmentToViewImgFragment(photo.url_s, position, text)
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (requireActivity() as MainActivity).enableFullScreen()
                if (query != null) {
                    binding.imageRecyclerView.scrollToPosition(0)
                    viewModel.getPagedResults(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}