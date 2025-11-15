package com.example.linguaplayindia.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.linguaplayindia.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // LOAD XML → VERY IMPORTANT
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Sign up button
        binding.btnCreate.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val username = binding.etName.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val passArray = password.toCharArray()

                val result = UserManager.registerUser(
                    applicationContext,
                    username,
                    email,
                    passArray
                )

                passArray.fill('\u0000')

                result.fold(onSuccess = {
                    Toast.makeText(this@SignupActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                }, onFailure = { ex ->
                    Toast.makeText(this@SignupActivity, ex.message, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}
