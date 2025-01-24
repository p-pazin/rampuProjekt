package com.example.carchive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.carchive.databinding.ActivityCarchiveBinding
import com.example.carchive.services.TokenManager
import com.google.android.material.navigation.NavigationView

class CarchiveActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggleButton: ImageButton
    private var _binding: ActivityCarchiveBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, CarchiveActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        setContentView(R.layout.activity_carchive)

        setSupportActionBar(Toolbar(this))
        supportActionBar?.hide()

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        // Set up the Carchive navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.navigation_carchive)

        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        val tokenManager = TokenManager()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigationView.menu.setGroupCheckable(0, true, true)
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    navController.navigate(R.id.companyUserFragment)
                }

                R.id.nav_contact_catalog -> {
                    navController.navigate(R.id.contactsFragment)
                }

                R.id.nav_vehicle_catalog -> {
                    navController.navigate(R.id.katalogVozilaFragment)
                }

                R.id.nav_documents -> {
                    navController.navigate(R.id.offersFragment)
                }

                R.id.nav_statistics -> {
                    navController.navigate(R.id.statsFragment)
                }

                R.id.nav_ads -> {
                    navController.navigate(R.id.adFragment)
                }

                R.id.nav_map -> {
                    navController.navigate(R.id.locationFragment)
                }

                R.id.nav_contracts -> {
                    navController.navigate(R.id.contractsFragment)
                }

                R.id.nav_reservation -> {
                    navController.navigate(R.id.reservationFragment)
                }

                R.id.nav_logout -> {
                    tokenManager.clearToken()
                    switchToMainNavigationGraph()
                }
            }
            true
        }
    }

    /**
     * Switch to the `navigation_main.xml` navigation graph and navigate to `loadingFragment`.
     */
    private fun switchToMainNavigationGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set the main navigation graph
        val navGraph: NavGraph = navController.navInflater.inflate(R.navigation.navigation_main)
        navController.graph = navGraph

        // Navigate to the loadingFragment
        navController.navigate(R.id.loadingFragment)
    }

    fun setDrawerEnabled(enabled: Boolean) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
