package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

import android.widget.EditText
import android.widget.Toast
import com.simats.prosthoplanner.network.BaseResponse
import com.simats.prosthoplanner.network.ForgotPasswordRequest
import com.simats.prosthoplanner.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        val etEmail = findViewById<EditText>(R.id.etEmail)

        findViewById<Button>(R.id.btnSendOtp).setOnClickListener {
            val email = etEmail.text.toString()
            if (email.isNotEmpty()) {
                sendResetOtp(email)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        // Start Premium Animations
        startEntranceAnimations()
        startLogoAnimations()
    }

    private fun startEntranceAnimations() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.ivLogo),
            findViewById<View>(R.id.tvTitle),
            findViewById<View>(R.id.tvSubtitle),
            findViewById<View>(R.id.lblEmail),
            findViewById<View>(R.id.etEmail),
            findViewById<View>(R.id.btnSendOtp),
            findViewById<View>(R.id.footerContainer)
        )

        // Initial state
        for (view in viewsToAnimate) {
            view.alpha = 0f
            view.translationY = 50f
        }

        // Staggered animation
        for (i in viewsToAnimate.indices) {
            viewsToAnimate[i].animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay((i * 100).toLong())
                .setInterpolator(OvershootInterpolator(1.2f))
                .start()
        }
    }

    private fun startLogoAnimations() {
        val ivLogo = findViewById<android.widget.ImageView>(R.id.ivLogo)
        
        // Breathing Effect
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.05f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.05f)
        ObjectAnimator.ofPropertyValuesHolder(ivLogo, scaleX, scaleY).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Floating Effect
        ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_Y, -10f, 10f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun sendResetOtp(email: String) {
        val request = ForgotPasswordRequest(email)
        RetrofitClient.instance.forgotPassword(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    Toast.makeText(this@ForgotPasswordActivity, "Reset code sent to email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ForgotPasswordActivity, ResetVerificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    val errorMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.parseErrorMessage(response)
                    Toast.makeText(this@ForgotPasswordActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                val failureMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.getFailureMessage(t)
                Toast.makeText(this@ForgotPasswordActivity, failureMsg, Toast.LENGTH_LONG).show()
            }
        })
    }
}
