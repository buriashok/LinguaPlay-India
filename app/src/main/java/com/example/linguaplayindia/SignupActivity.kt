package com.example.linguaplayindia.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.linguaplayindia.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch
import java.util.UUID

class SignupActivity : ComponentActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val name = binding.etName.text?.toString()?.trim() ?: ""
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.toCharArray() ?: charArrayOf()

            // basic validation
            if (name.isBlank() || email.isBlank() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.size < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val result = UserManager.registerUser(applicationContext, name, email, password)
                // clear password
                password.fill('\u0000')
                result.fold(onSuccess = {
                    Toast.makeText(this@SignupActivity, "Account created. Please login.", Toast.LENGTH_SHORT).show()
                    finish()
                }, onFailure = { ex ->
                    Toast.makeText(this@SignupActivity, ex.message ?: "Signup failed", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}
