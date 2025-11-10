package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val languageSpinner = findViewById<Spinner>(R.id.spinnerLanguage)
        val btnSave = findViewById<Button>(R.id.btnSaveLanguage)

        val languages = listOf(
            "English", "Hindi", "Telugu", "Tamil",
            "Punjabi", "Marathi", "Bengali", "Gujarati", "Kannada"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        val currentLang = prefs.getString("selected_language", "English")
        languageSpinner.setSelection(languages.indexOf(currentLang))
        btnSave.setOnClickListener {
            val selected = languageSpinner.selectedItem.toString()
            prefs.edit().putString("selected_language", selected).apply()

            // 🌐 Update locale immediately
            LanguageHelper.setLocale(this, selected)

            // Restart activity to apply new UI language
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }

    }
}
