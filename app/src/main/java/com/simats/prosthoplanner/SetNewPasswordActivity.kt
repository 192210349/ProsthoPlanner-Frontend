package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

import com.simats.prosthoplanner.network.BaseResponse
import com.simats.prosthoplanner.network.ResetPasswordRequest
import com.simats.prosthoplanner.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetNewPasswordActivity : AppCompatActivity() {

    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var tilNewPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private var email: String? = null
    private var otp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_password)

        email = intent.getStringExtra("email")
        otp = intent.getStringExtra("otp")

        initViews()
        setupListeners()
    }

    private fun initViews() {
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        tilNewPassword = findViewById(R.id.tilNewPassword)
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnResetPassword).setOnClickListener {
            validateAndReset()
        }
    }

    private fun validateAndReset() {
        tilNewPassword.error = null
        tilConfirmPassword.error = null

        val newPassword = etNewPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (!com.simats.prosthoplanner.utils.ValidationUtils.isPasswordValid(newPassword)) {
            tilNewPassword.error = com.simats.prosthoplanner.utils.ValidationUtils.getPasswordErrorMessage()
            return
        }

        if (confirmPassword != newPassword) {
            tilConfirmPassword.error = "Passwords do not match"
            return
        }

        if (email != null && otp != null) {
            performReset(email!!, otp!!, newPassword)
        } else {
            Toast.makeText(this, "Session expired, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performReset(email: String, otp: String, pass: String) {
        val request = ResetPasswordRequest(email, otp, pass)
        RetrofitClient.instance.resetPassword(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@SetNewPasswordActivity, "Password reset successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SetNewPasswordActivity, ResetSuccessActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    val errorMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.parseErrorMessage(response)
                    Toast.makeText(this@SetNewPasswordActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                val failureMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.getFailureMessage(t)
                Toast.makeText(this@SetNewPasswordActivity, failureMsg, Toast.LENGTH_LONG).show()
            }
        })
    }
}
