package com.example.carchive

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.carchive.fragments.ContactAddFragment
import com.example.carchive.fragments.ContactDetailsFragment
import com.example.carchive.fragments.ContactsFragment
import com.example.carchive.fragments.LoginFragment
import com.google.android.material.navigation.NavigationView

class CarchiveActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggleButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)


        toggleButton = findViewById(R.id.drawer_toggle_buttonn)
        toggleButton.setOnClickListener {
            // Toggle the drawer when the button is clicked
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)  // Close the drawer
            } else {
                drawerLayout.openDrawer(GravityCompat.START)  // Open the drawer
            }
        }


        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigationView.menu.setGroupCheckable(0, true, true)
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when(menuItem.itemId){
                R.id.nav_contact_catalog -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_container, ContactsFragment())
                    transaction.commit()
                }
            }
            true
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}