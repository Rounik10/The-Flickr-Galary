package com.example.theflickrgalary

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.theflickrgalary.adapters.FlickrLoadStateAdapter
import com.example.theflickrgalary.adapters.PagingAdapter
import com.example.theflickrgalary.adapters.RecyclerItemClicked
import com.example.theflickrgalary.databinding.FragmentSearchBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.view.MainActivity
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class SearchFragment : Fragment(), RecyclerItemClicked {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    private lateinit var adapter: PagingAdapter
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Search"
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        adapter = PagingAdapter(this)
        binding.searchRecycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = FlickrLoadStateAdapter { adapter.retry() },
            footer = FlickrLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                searchRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                errorText.isVisible = loadState.source.refresh is LoadState.Error

                // if no result is found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    nothingFoundText.isVisible = true
                    searchRecycler.isVisible = false
                } else {
                    nothingFoundText.isVisible = false
                }

                buttonRetry.setOnClickListener { adapter.refresh() }
            }
        }

        return binding.root
    }

    private fun loadData() {
        viewModel.photos.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun debounceSearch(searchView: SearchView) {
        var uiHidden = false
        var prevQuery = ""
        val observableQueryText: Observable<String?> = Observable
            .create(ObservableOnSubscribe<String?> { emitter ->
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        searchView.clearFocus()
                        updateFullScreen()
                        if(query == prevQuery) return true
                        else prevQuery = query

                        if (!emitter.isDisposed) {
                            emitter.onNext(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        if(newText == prevQuery) return true
                        else prevQuery = newText
                        if (!emitter.isDisposed) {
                            emitter.onNext(newText) // Pass the query to the emitter
                        }
                        if (!uiHidden) {
                            hideUi()
                            uiHidden = true
                        }
                        return true
                    }
                })
            })
            .debounce(800, TimeUnit.MILLISECONDS) // Apply Debounce() operator to limit requests
            .subscribeOn(Schedulers.io())

        observableQueryText.subscribe(object : Observer<String?> {
            override fun onSubscribe(d: Disposable?) {
                disposables.add(d)
            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

            override fun onComplete() {}

            override fun onNext(text: String?) {
                if (text != null) {
                    GlobalScope.launch(Dispatchers.Main) {
                        viewModel.getPagedResults(text)
                        loadData()
                    }
                }
            }
        })
    }

    private fun updateFullScreen() {
        (requireActivity() as MainActivity).enableFullScreen()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    private fun hideUi() {
        binding.apply {
            searchSomething.visibility = View.GONE
            searchLogo.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        debounceSearch(searchView)
        searchView.queryHint = "Search Here"
        searchView.requestFocus()
    }

    override fun onItemClick(photo: Photo, position: Int) {
        val action = SearchFragmentDirections.actionSearchFragmentToViewImgFragment(
            photo.url_s, position,
            searchView.query.toString()
        )
        findNavController().navigate(action)
    }
}