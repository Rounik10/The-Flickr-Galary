package com.example.theflickrgalary.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.theflickrgalary.adapters.ViewPagerAdapter
import com.example.theflickrgalary.databinding.FragmentViewImgBinding
import com.example.theflickrgalary.model.Photo
import com.example.theflickrgalary.repository.Repository
import com.example.theflickrgalary.viewmodels.MainViewModel
import com.example.theflickrgalary.viewmodels.MainViewModelFactory

class ViewImgFragment : Fragment() {

    private var _binding: FragmentViewImgBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val args: ViewImgFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewImgBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "View Image"

        val position = args.position
        val fragmentList = ArrayList<Fragment>()
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (args.searchKey == "") viewModel.getApiModel()
        else viewModel.getSearchResult(args.searchKey)

        viewModel.myResponse.observe(viewLifecycleOwner, {
            val photoList = it.photos.photo

            for (item: Photo in photoList) {
                try {
                    fragmentList.add(ImageFragment(item.url_s))
                } catch (e: Exception) {
                    continue
                }
            }
            fragmentList[position] = ImageFragment(url = args.url)
            val adapter = ViewPagerAdapter(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )
            binding.viewPager.adapter = adapter
            binding.viewPager.currentItem = position
        })

        binding.shareBtn.setOnClickListener { shareImage() }
        return binding.root
    }

    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Checkout this image:\n${args.url}")
        val chooser = Intent.createChooser(intent, "Share the Image Via...")
        startActivity(chooser)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}