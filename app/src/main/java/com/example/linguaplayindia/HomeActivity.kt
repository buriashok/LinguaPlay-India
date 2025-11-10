package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLanguage = prefs.getString("selected_language", "English")

        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvSubGreeting = findViewById<TextView>(R.id.tvSubGreeting)
        val btnGames = findViewById<Button>(R.id.btnGames)
        val btnLearning = findViewById<Button>(R.id.btnLearning)
        val btnProgress = findViewById<Button>(R.id.btnProgress)
        val btnUtility = findViewById<Button>(R.id.btnUtility)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)

        // Dynamic greeting
        val greeting = getTimeGreeting()
        tvSubGreeting.text = greeting

        // Display selected language name
        tvGreeting.text = when (selectedLanguage) {
            "Hindi" -> "नमस्ते!"
            "Telugu" -> "నమస్కారం!"
            "Tamil" -> "வணக்கம்!"
            "Punjabi" -> "ਸਤ ਸ੍ਰੀ ਅਕਾਲ!"
            "Marathi" -> "नमस्कार!"
            "Bengali" -> "নমস্কার!"
            "Gujarati" -> "નમસ્તે!"
            "Kannada" -> "ನಮಸ್ಕಾರ!"
            else -> "Hello!"
        }

        // Buttons navigation
        btnGames.setOnClickListener {
            startActivity(Intent(this, GamesActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnLearning.setOnClickListener {
            startActivity(Intent(this, LearningActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnProgress.setOnClickListener {
            startActivity(Intent(this, ProgressActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnUtility.setOnClickListener {
            startActivity(Intent(this, UtilityActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
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
