package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import com.simats.prosthoplanner.network.LoginResponse
import com.simats.prosthoplanner.network.RetrofitClient
import com.simats.prosthoplanner.network.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { onBackPressed() }

        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            performLogin()
        }

        findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.tvCreateAccount).setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
            finish()
        }

        findViewById<ImageButton>(R.id.btnBiometric).setOnClickListener {
            if (checkBiometricAvailability()) {
                showBiometricPrompt()
            }
        }

        // Start Premium Animations
        startEntranceAnimations()
        startLogoBreathingAnimation()
    }

    private fun startEntranceAnimations() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.ivLogo),
            findViewById<View>(R.id.tvTitle),
            findViewById<View>(R.id.tvSubtitle),
            findViewById<View>(R.id.lblEmail),
            findViewById<View>(R.id.etEmail),
            findViewById<View>(R.id.lblPassword),
            findViewById<View>(R.id.tilPassword),
            findViewById<View>(R.id.tvForgotPassword),
            findViewById<View>(R.id.loginActions),
            findViewById<View>(R.id.footerContainer)
        )

        // Initialize views as invisible and translated down
        for (view in viewsToAnimate) {
            view.alpha = 0f
            view.translationY = 50f
        }

        // Animate each view with a staggered delay
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

    private fun startLogoBreathingAnimation() {
        val ivLogo = findViewById<android.widget.ImageView>(R.id.ivLogo)
        
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.05f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.05f)
        
        ObjectAnimator.ofPropertyValuesHolder(ivLogo, scaleX, scaleY).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun checkBiometricAvailability(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> {
                Toast.makeText(this, "Biometric authentication is not available", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    // Proceeds to dashboard (simulated success)
                    val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your fingerprint")
            .setNegativeButtonText("Use account password")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.etEmail).text.toString()
        val password = findViewById<EditText>(R.id.etPassword).text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(email, password)
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val user = response.body()?.user
                    Toast.makeText(this@SignInActivity, "Welcome, ${user?.full_name}", Toast.LENGTH_SHORT).show()
                    
                    // Persist user data for personalization
                    val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("USER_FULL_NAME", user?.full_name)
                    editor.putString("USER_EMAIL", user?.email)
                    // If backend returns phone, save it. Otherwise, keep existing.
                    // For now, let's assume if it's a new login, the user might want to re-enter phone later or we can fetch it if returned.
                    editor.apply()
                    
                    // Proceed to Dashboard
                    val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else if (response.code() == 403 || response.body()?.requires_otp == true) {
                    Toast.makeText(this@SignInActivity, "Account not verified. Please verify now.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignInActivity, OtpVerificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                } else {
                    val errorMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.parseErrorMessage(response)
                    Toast.makeText(this@SignInActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                val failureMsg = com.simats.prosthoplanner.network.NetworkErrorHandler.getFailureMessage(t)
                Toast.makeText(this@SignInActivity, failureMsg, Toast.LENGTH_LONG).show()
            }
        })
    }
}
