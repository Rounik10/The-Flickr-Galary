package com.example.theflickrgalary.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.theflickrgalary.R
import com.example.theflickrgalary.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableFullScreen()

        navController = findNavController(R.id.fragment_container)
        binding.navView.setupWithNavController(navController)
        appBarConfig = AppBarConfiguration(navController.graph, binding.drawerLayout)

        setupActionBarWithNavController(navController, appBarConfig)

        if(!isOnline()) {
            Snackbar.make(binding.root, "No Internet!", Snackbar.LENGTH_LONG)
                .setAction("RETRY") {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }.show()
        }

    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        enableFullScreen()
    }

    @Suppress("DEPRECATION")
    fun enableFullScreen() {
        Handler(Looper.myLooper()!!).post {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (navController.currentDestination?.id){
            R.id.homeFragment -> {
                return super.onOptionsItemSelected(item)
            }
            R.id.viewImgFragment -> {
                navController.popBackStack()
            }
            R.id.searchFragment -> {
                if(item.itemId != R.id.action_search) {
                    navController.navigate(R.id.action_searchFragment_to_homeFragment)
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.homeFragment -> {
                super.onBackPressed()
            }
            R.id.viewImgFragment -> {
                navController.popBackStack()
            }
            R.id.searchFragment -> {
                navController.navigate(R.id.action_searchFragment_to_homeFragment)
            }
        }
    }
}