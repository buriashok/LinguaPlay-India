package com.example.linguaplayindia.auth

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.linguaplayindia.HomeActivity
import com.example.linguaplayindia.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var executor: Executor
    private lateinit var prompt: BiometricPrompt

    companion object {
        private const val DEVICE_CREDENTIAL_REQUEST = 2001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LOAD XML
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Go to signup
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Email/password login
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val passwordChars = binding.etPassword.text?.toString()?.toCharArray() ?: charArrayOf()

            if (email.isBlank() || passwordChars.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val res = UserManager.validateUser(applicationContext, email, passwordChars)
                passwordChars.fill('\u0000')

                res.fold(onSuccess = { user ->
                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("logged_user", user.email)
                        .apply()

                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()

                }, onFailure = { ex ->
                    Toast.makeText(
                        this@LoginActivity,
                        ex.message ?: "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
        }

        // Biometric
        binding.btnBiometric.setOnClickListener {
            setupBiometric()
        }
    }

    private fun setupBiometric() {
        val bm = BiometricManager.from(this)

        if (bm.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            ) != BiometricManager.BIOMETRIC_SUCCESS
        ) {
            Toast.makeText(this, "Biometric not available", Toast.LENGTH_SHORT).show()
            return
        }

        executor = ContextCompat.getMainExecutor(this)
        prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val saved = prefs.getString("logged_user", null)
                if (saved != null) {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "No saved user. Login manually.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@LoginActivity, errString, Toast.LENGTH_SHORT).show()
            }
        })

        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric or Device Credential")
            .setSubtitle("Unlock using fingerprint or device PIN")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        prompt.authenticate(info)
    }
}
