package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.widget.TextView
import com.simats.prosthoplanner.network.BaseResponse
import com.simats.prosthoplanner.network.ResendOtpRequest
import com.simats.prosthoplanner.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_verification)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        val otp1 = findViewById<EditText>(R.id.etOtp1)
        val otp2 = findViewById<EditText>(R.id.etOtp2)
        val otp3 = findViewById<EditText>(R.id.etOtp3)
        val otp4 = findViewById<EditText>(R.id.etOtp4)

        setupOtpInputs(otp1, otp2, otp3, otp4)

        val email = intent.getStringExtra("email")

        findViewById<Button>(R.id.btnVerify).setOnClickListener {
            val code = otp1.text.toString() + otp2.text.toString() + otp3.text.toString() + otp4.text.toString()
            if (code.length == 4) {
                val intent = Intent(this, SetNewPasswordActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("otp", code)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.tvResend).setOnClickListener {
            if (email != null) {
                resendOtp(email)
            }
        }
    }

    private fun resendOtp(email: String) {
        val request = ResendOtpRequest(email)
        RetrofitClient.instance.resendOtp(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@ResetVerificationActivity, "New OTP sent to email", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.parseErrorMessage(response)
                    Toast.makeText(this@ResetVerificationActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                val failureMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.getFailureMessage(t)
                Toast.makeText(this@ResetVerificationActivity, failureMsg, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupOtpInputs(vararg editTexts: EditText) {
        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (s?.length == 0 && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }
            })
        }
    }
}
