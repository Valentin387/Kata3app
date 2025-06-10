package com.kata3.kata3app.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.kata3.kata3app.data.repositories.AuthRepository
import com.kata3.kata3app.databinding.ActivitySignupBinding
import com.kata3.kata3app.io.AuthService
import com.kata3.kata3app.ui.login.LoginActivity
import com.kata3.kata3app.utils.EncryptedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val authService: AuthService by lazy {
        AuthService.create(applicationContext)
    }
    private val signupViewModel: SignupViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SignupViewModel(AuthRepository(authService)) as T
            }
        })[SignupViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EncryptedPrefsManager.init(applicationContext)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btSignup.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInput(username, password, confirmPassword)) {
                showLoadingSpinner()
                signupViewModel.registerUser(username, password)
            } else {
                Toast.makeText(this, "Invalid input. Check username and password.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLoginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        return username.isNotEmpty() &&
                password.length >= 8 &&
                password == confirmPassword
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            signupViewModel.registerResult.collect { result ->
                hideLoadingSpinner()
                when (result) {
                    is RegisterResult.Success -> {
                        Toast.makeText(this@SignupActivity, "Registration successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    }
                    is RegisterResult.Error -> {
                        Toast.makeText(this@SignupActivity, result.message, Toast.LENGTH_SHORT).show()
                    }

                    null -> TODO()
                }
            }
        }
    }

    private fun showLoadingSpinner() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingSpinner() {
        binding.progressBar.visibility = View.GONE
    }
}