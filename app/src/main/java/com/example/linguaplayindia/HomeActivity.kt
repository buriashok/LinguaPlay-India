package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.example.linguaplayindia.auth.LoginActivity
import java.util.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val loggedUser = prefs.getString("logged_user", null)

        if (loggedUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Greeting
        val username = loggedUser.substringBefore("@")
        findViewById<TextView>(R.id.tvGreetingBig).text = "Hello, $username!"
        findViewById<TextView>(R.id.tvGreetingSmall).text = getTimeGreeting()

        // Drawer setup
        val drawer = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navigationView)

        findViewById<ImageButton>(R.id.btnMenu).setOnClickListener {
            drawer.open()
        }

        // Set header info
        val header = navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.headerName).text = username
        header.findViewById<TextView>(R.id.headerEmail).text = loggedUser

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navAccount -> {
                    Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
                }

                R.id.navSettings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                R.id.navLogout -> {
                    showLogoutConfirm(prefs)
                }
            }
            drawer.close()
            true
        }

        // Card clicks
        findViewById<CardView>(R.id.cardGames).setOnClickListener {
            startActivity(Intent(this, GamesActivity::class.java))
        }

        findViewById<CardView>(R.id.cardLearning).setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
        }

        findViewById<CardView>(R.id.cardProgress).setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
        }

        findViewById<CardView>(R.id.cardUtility).setOnClickListener {
            startActivity(Intent(this, UtilityActivity::class.java))
        }
    }


    private fun showLogoutConfirm(prefs: android.content.SharedPreferences) {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Do you really want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                prefs.edit().clear().apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getTimeGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
