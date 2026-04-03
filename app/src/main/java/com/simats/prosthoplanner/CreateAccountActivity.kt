package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.simats.prosthoplanner.network.BaseResponse
import com.simats.prosthoplanner.network.RetrofitClient
import com.simats.prosthoplanner.network.SignupRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { onBackPressed() }

        findViewById<Button>(R.id.btnCreateAccount).setOnClickListener {
            performSignup()
        }

        findViewById<TextView>(R.id.tvSignIn).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
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
            findViewById<View>(R.id.lblFullName),
            findViewById<View>(R.id.etFullName),
            findViewById<View>(R.id.lblEmail),
            findViewById<View>(R.id.etEmail),
            findViewById<View>(R.id.lblMobile),
            findViewById<View>(R.id.etMobile),
            findViewById<View>(R.id.lblPassword),
            findViewById<View>(R.id.tilPassword),
            findViewById<View>(R.id.lblConfirmPassword),
            findViewById<View>(R.id.tilConfirmPassword),
            findViewById<View>(R.id.btnCreateAccount),
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
                .setStartDelay((i * 80).toLong())
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

    private fun performSignup() {
        val fullName = findViewById<EditText>(R.id.etFullName).text.toString()
        val email = findViewById<EditText>(R.id.etEmail).text.toString()
        val mobile = findViewById<EditText>(R.id.etMobile).text.toString()
        val password = findViewById<EditText>(R.id.etPassword).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword).text.toString()

        val nameValidation = com.simats.prosthoplanner.utils.ValidationUtils.validateName(fullName)
        if (!nameValidation.first) {
            Toast.makeText(this, nameValidation.second, Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val phoneValidation = com.simats.prosthoplanner.utils.ValidationUtils.validatePhone(mobile)
        if (!phoneValidation.first) {
            Toast.makeText(this, phoneValidation.second, Toast.LENGTH_SHORT).show()
            return
        }

        if (!com.simats.prosthoplanner.utils.ValidationUtils.isPasswordValid(password)) {
            Toast.makeText(this, com.simats.prosthoplanner.utils.ValidationUtils.getPasswordErrorMessage(), Toast.LENGTH_LONG).show()
            return
        }

        val request = SignupRequest(fullName, email, mobile, password, confirmPassword)
        RetrofitClient.instance.signup(request).enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    // Persist basic info early for dynamic profile
                    val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("USER_FULL_NAME", fullName)
                    editor.putString("USER_EMAIL", email)
                    editor.putString("USER_PHONE", mobile)
                    editor.apply()

                    Toast.makeText(this@CreateAccountActivity, "Account created successfully. Verify OTP.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CreateAccountActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    val errorMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.parseErrorMessage(response)
                    Toast.makeText(this@CreateAccountActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                val failureMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.getFailureMessage(t)
                Toast.makeText(this@CreateAccountActivity, failureMsg, Toast.LENGTH_LONG).show()
            }
        })
    }
}
