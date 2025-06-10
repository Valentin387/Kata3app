package com.kata3.kata3app.ui.login

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
import com.kata3.kata3app.databinding.ActivityLoginBinding
import com.kata3.kata3app.io.AuthService
import com.kata3.kata3app.ui.main.MainActivity
import com.kata3.kata3app.utils.EncryptedPrefsManager
import com.valentinConTilde.onmywayapp.utils.CustomPermissionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var permissionHandler: CustomPermissionHandler
    private val authService: AuthService by lazy {
        AuthService.create(applicationContext)
    }
    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(AuthRepository(authService)) as T
            }
        })[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EncryptedPrefsManager.init(applicationContext)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionHandler = CustomPermissionHandler(this)

        if (checkStoredToken()) {
            if (permissionHandler.checkAndRequestPermissions(this)) {
                goToMainActivity()
            } else {
                goToPermissionsActivity()
            }
            return
        }

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (validateInput(username, password)) {
                showLoadingSpinner()
                loginViewModel.loginUser(username, password)
            } else {
                Toast.makeText(this, "Invalid input. Check username and password.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignupRedirect.setOnClickListener {
            startActivity(Intent(this, com.kata3.kata3app.ui.signup.SignupActivity::class.java))
            finish()
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.length >= 8
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            loginViewModel.loginResult.collect { result ->
                hideLoadingSpinner()
                when (result) {
                    is LoginResult.Success -> {
                        saveToken(result.token)
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        if (permissionHandler.checkAndRequestPermissions(this@LoginActivity)) {
                            goToMainActivity()
                        } else {
                            goToPermissionsActivity()
                        }
                    }
                    is LoginResult.Error -> {
                        Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT).show()
                    }

                    null -> TODO()
                }
            }
        }
    }

    private fun checkStoredToken(): Boolean {
        val preferences = EncryptedPrefsManager.getPreferences()
        val token = preferences.getString("jwt_token", null)
        Log.d("LoginActivity", "Stored token: $token")
        return token != null
    }

    private fun saveToken(token: String) {
        val preferences = EncryptedPrefsManager.getPreferences()
        val editor = preferences.edit()
        editor.putString("jwt_token", token)
        editor.apply()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToPermissionsActivity() {
        //Toast the user that he has no given permissions
        Toast.makeText(this, "Please grant permissions to continue.", Toast.LENGTH_LONG).show()
        //startActivity(Intent(this, PermissionsActivity::class.java))
        finish()
    }

    private fun showLoadingSpinner() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingSpinner() {
        binding.progressBar.visibility = View.GONE
    }
}