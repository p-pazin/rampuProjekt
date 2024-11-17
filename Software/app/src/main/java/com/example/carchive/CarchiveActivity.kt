package com.example.carchive


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.carchive.fragments.ContactAddFragment
import com.example.carchive.fragments.ContactDetailsFragment
import com.example.carchive.fragments.ContactUpdateFragment
import com.example.carchive.fragments.ContactsFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.carchive.databinding.ActivityCarchiveBinding
import com.example.carchive.databinding.FragmentCarCatalogBinding
import com.example.carchive.entities.Car
import com.example.carchive.fragments.LoginFragment
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


        val navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(
            R.navigation.navigation_carchive
        )
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigationView.menu.setGroupCheckable(0, true, true)
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when(menuItem.itemId){
                R.id.nav_contact_catalog -> {
                    navController.navigate(R.id.contactsFragment)
                }

                R.id.nav_vehicle_catalog -> {
                    navController.navigate(R.id.katalogVozilaFragment)
                }
            }
            true
        }
    }
    fun setDrawerEnabled(enabled: Boolean) {
        if (enabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }
    fun toggleDrawer(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }


}