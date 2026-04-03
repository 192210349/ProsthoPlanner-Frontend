package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialization: Set initial states for staggered entrance
        val logoContainer = findViewById<View>(R.id.logoContainer)
        val tvProstho = findViewById<View>(R.id.tvProstho)
        val tvPlanner = findViewById<View>(R.id.tvPlanner)
        val tvSubtitle = findViewById<View>(R.id.tvSubtitle)
        val footerContent = findViewById<View>(R.id.footerContent)
        val bgGlow = findViewById<View>(R.id.bgGlow)

        val views = arrayOf(logoContainer, tvProstho, tvPlanner, tvSubtitle, footerContent)
        for (view in views) {
            view.alpha = 0f
            view.translationY = 40f
        }
        bgGlow.alpha = 0f
        bgGlow.scaleX = 0.5f
        bgGlow.scaleY = 0.5f

        // 1. Background Glow Entrance
        bgGlow.animate()
            .alpha(0.15f)
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(2000)
            .start()

        // 2. Staggered Content Entrance
        views.forEachIndexed { index, view ->
            view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(200L + (index * 150L))
                .setDuration(800)
                .setInterpolator(OvershootInterpolator(1.2f))
                .start()
        }

        // 3. Logo Dynamic Effects (Float & Breathing)
        startPremiumLogoAnimations(logoContainer)

        // Navigation to Welcome Screen (Extra 1s for the "Iconic" feel)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500L)
    }

    private fun startPremiumLogoAnimations(view: View) {
        // Floating Effect (Vertical Oscillation)
        ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, -15f, 15f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Breathing Effect (Subtle Scaling)
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.04f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.04f)
        ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 2500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}
