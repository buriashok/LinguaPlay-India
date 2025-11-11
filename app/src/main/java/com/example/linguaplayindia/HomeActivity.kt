package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.linguaplayindia.auth.LoginActivity
import java.util.*
import java.util.concurrent.Executor

class HomeActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val selectedLanguage = prefs.getString("selected_language", "English")
        val loggedUser = prefs.getString("logged_user", null)

        // Require login
        if (loggedUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Optional biometric lock
        setupBiometricIfAvailable(loggedUser)

        // View bindings
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val tvSubGreeting = findViewById<TextView>(R.id.tvSubGreeting)
        val btnGames = findViewById<Button>(R.id.btnGames)
        val btnLearning = findViewById<Button>(R.id.btnLearning)
        val btnProgress = findViewById<Button>(R.id.btnProgress)
        val btnUtility = findViewById<Button>(R.id.btnUtility)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Greeting text
        val greeting = getTimeGreeting()
        tvSubGreeting.text = "$greeting, ${loggedUser.substringBefore('@')}!"

        // Display selected language greeting
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

        // Navigation buttons
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

        // 🔹 Logout button logic
        btnLogout.setOnClickListener {
            showLogoutConfirmation(prefs)
        }
    }

    // ✅ Confirmation dialog before logout
    private fun showLogoutConfirmation(prefs: android.content.SharedPreferences) {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                prefs.edit().clear().apply() // Clear all saved session data
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ✅ Biometric unlock on entry
    private fun setupBiometricIfAvailable(loggedUser: String) {
        val bm = BiometricManager.from(this)
        if (bm.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt =
                BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(
                            this@HomeActivity,
                            "Welcome back, ${loggedUser.substringBefore('@')}!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@HomeActivity, errString, Toast.LENGTH_SHORT).show()
                        // If user cancels biometric, log out for safety
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        finish()
                    }
                })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Learn Indian Languages")
                .setSubtitle("Use your fingerprint or device PIN")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    // ✅ Dynamic greeting by time
    private fun getTimeGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }
    }
}
