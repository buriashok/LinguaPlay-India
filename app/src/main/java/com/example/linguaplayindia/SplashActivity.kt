package com.example.linguaplayindia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val loggedUser = prefs.getString("logged_user", null)

            if (loggedUser == null) {
                startActivity(Intent(this, com.example.linguaplayindia.auth.LoginActivity::class.java))
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
            }

            finish()

        }, 1200) // 1.2 sec delay
    }
}
