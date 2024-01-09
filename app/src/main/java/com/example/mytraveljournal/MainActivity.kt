package com.example.mytraveljournal

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mytraveljournal.TravelData.TravelViewModel
import com.example.mytraveljournal.databinding.ActivityMainBinding
import com.example.mytraveljournal.ui.home.SettingsUserViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mTravelViewModel: TravelViewModel
    private lateinit var mSettingsUserViewModel: SettingsUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        mSettingsUserViewModel = ViewModelProvider(this).get(SettingsUserViewModel::class.java)
        if (intent.hasExtra("userId")) {
            val userId = intent.getIntExtra("userId", -1)
            val userName = intent.getStringExtra("userName")
            println(userId)

            mSettingsUserViewModel.setUserId(userId)

            if (userName != null) {
                mSettingsUserViewModel.setUsername(userName)
            }

            // Now, you can use the userId as needed in your MainActivity
            Toast.makeText(this, "User ID: $userId", Toast.LENGTH_SHORT).show()
        } else {
            // Handle the case where "userId" extra is not present
            Toast.makeText(this, "User ID not provided", Toast.LENGTH_SHORT).show()
        }


        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_settings, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}


