package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLanguage = prefs.getString("selected_language", null)

        // ✅ If already selected, go directly to HomeActivity
        if (selectedLanguage != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Otherwise, show language selection screen
        setContentView(R.layout.activity_language_select)

        val languageContainer = findViewById<LinearLayout>(R.id.languageContainer)

        val languages = listOf(
            "English", "Hindi", "Telugu", "Tamil",
            "Punjabi", "Marathi", "Bengali", "Gujarati", "Kannada"
        )

        for (lang in languages) {
            val btn = Button(this).apply {
                text = lang
                textSize = 18f
                setAllCaps(false)
                setPadding(16, 16, 16, 16)
                setBackgroundResource(android.R.drawable.btn_default)
            }

            btn.setOnClickListener {
                // Save selected language
                prefs.edit().putString("selected_language", lang).apply()

                // 🌐 Set locale dynamically
                LanguageHelper.setLocale(this@LanguageSelectActivity, lang)

                // Move to HomeActivity
                val intent = Intent(this@LanguageSelectActivity, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }


            languageContainer.addView(btn)
        }
    }
}
