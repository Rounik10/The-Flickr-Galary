package com.example.theflickrgalary.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
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
        if (navController.currentDestination?.id == R.id.viewImgFragment) {
            navController.navigate(R.id.action_viewImgFragment_to_homeFragment)
        } else if (navController.currentDestination?.id == R.id.homeFragment) {
            return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.homeFragment) super.onBackPressed()
        else navController.navigate(R.id.action_viewImgFragment_to_homeFragment)
    }

}