package com.simats.prosthoplanner

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class SecurityPasswordActivity : AppCompatActivity() {

    private lateinit var etCurrentPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var tilCurrentPassword: TextInputLayout
    private lateinit var tilNewPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var btnUpdatePassword: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_password)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword)
        tilNewPassword = findViewById(R.id.tilNewPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        btnUpdatePassword.setOnClickListener {
            validateAndUpdate()
        }
    }

    private fun validateAndUpdate() {
        // Reset errors
        tilCurrentPassword.error = null
        tilNewPassword.error = null
        tilConfirmPassword.error = null

        val current = etCurrentPassword.text.toString().trim()
        val new = etNewPassword.text.toString().trim()
        val confirm = etConfirmPassword.text.toString().trim()

        var isValid = true

        if (current.isEmpty()) {
            tilCurrentPassword.error = "Current password is required"
            isValid = false
        }

        if (!com.simats.prosthoplanner.utils.ValidationUtils.isPasswordValid(new)) {
            tilNewPassword.error = com.simats.prosthoplanner.utils.ValidationUtils.getPasswordErrorMessage()
            isValid = false
        }

        if (confirm != new) {
            tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        } else if (confirm.isEmpty()) {
             tilConfirmPassword.error = "Please confirm your password"
             isValid = false
        }

        if (isValid) {
            performUpdate()
        }
    }

    private fun performUpdate() {
        // Show loading state
        btnUpdatePassword.isEnabled = false
        btnUpdatePassword.text = "Updating..."

        // Simulate network delay for premium feel
        Handler(Looper.getMainLooper()).postDelayed({
            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }, 1500)
    }
}
