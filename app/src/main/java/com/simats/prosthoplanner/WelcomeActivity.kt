package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Navigation Setup
        findViewById<View>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        findViewById<View>(R.id.btnCreateAccount).setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        startWelcomeSequence()
    }

    private fun startWelcomeSequence() {
        val logo = findViewById<View>(R.id.logoContainer)
        val aura = findViewById<View>(R.id.logoAura)
        val title = findViewById<View>(R.id.welcomeTitle)
        val subtitle = findViewById<View>(R.id.welcomeSubtitle)
        val desc = findViewById<View>(R.id.welcomeDescription)
        val btnSign = findViewById<View>(R.id.btnSignIn)
        val btnCreate = findViewById<View>(R.id.btnCreateAccount)

        // Initial setup
        listOf(title, subtitle, desc, btnSign, btnCreate).forEach {
            it.alpha = 0f
            it.translationY = 30f
        }

        // Logo Sequence
        logo.alpha = 0f
        logo.scaleX = 0.5f
        logo.scaleY = 0.5f
        logo.animate()
            .alpha(1f)
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(1200)
            .setInterpolator(OvershootInterpolator(1.2f))
            .withEndAction { startAuraPulse(aura) }
            .start()

        // Text Sequence
        title.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(400).start()
        subtitle.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(600).start()
        desc.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(800).start()

        // Button Sequence
        btnSign.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(1000).start()
        btnCreate.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(1200).start()
    }

    private fun startAuraPulse(view: View) {
        view.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .alpha(0.1f)
            .setDuration(2500)
            .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(0.25f)
                    .setDuration(2500)
                    .withEndAction { startAuraPulse(view) }
                    .start()
            }
            .start()
    }
}
