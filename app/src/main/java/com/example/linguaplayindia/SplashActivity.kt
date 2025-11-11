package com.example.linguaplayindia

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class SplashActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val isBiometricEnabled = prefs.getBoolean("biometric_enabled", false)
        val loggedUser = prefs.getString("logged_user", null)

        // If user never logged in → go to Login
        if (loggedUser == null) {
            startActivity(Intent(this, com.example.linguaplayindia.auth.LoginActivity::class.java))
            finish()
            return
        }

        // If biometric is enabled → auto authenticate
        if (isBiometricEnabled) {
            startBiometricUnlock()
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun startBiometricUnlock() {
        val bm = BiometricManager.from(this)
        when (bm.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                executor = ContextCompat.getMainExecutor(this)
                biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                        finish()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // Fallback to PIN unlock
                        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                            errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON
                        ) {
                            showDeviceCredentialPrompt()
                        } else {
                            Toast.makeText(this@SplashActivity, errString, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                })

                val info = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Unlock Learn Indian Languages")
                    .setSubtitle("Use your fingerprint or device PIN")
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_STRONG or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                    .build()

                biometricPrompt.authenticate(info)
            }

            else -> {
                // Direct fallback if biometrics unavailable
                showDeviceCredentialPrompt()
            }
        }
    }

    private fun showDeviceCredentialPrompt() {
        val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val intent = km.createConfirmDeviceCredentialIntent(
            "Unlock Learn Indian Languages",
            "Confirm your phone password or pattern to continue"
        )
        if (intent != null) {
            startActivity(intent)
            finish()
        } else {
            startActivity(Intent(this, com.example.linguaplayindia.auth.LoginActivity::class.java))
            finish()
        }
    }
}
