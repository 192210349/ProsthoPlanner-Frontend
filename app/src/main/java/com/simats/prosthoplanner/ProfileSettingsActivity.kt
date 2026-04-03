package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.view.animation.AnimationUtils
import android.view.View
import android.view.animation.OvershootInterpolator
import android.view.animation.AccelerateDecelerateInterpolator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper

class ProfileSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        // Bottom Navigation
        NavigationHelper.setupPremiumNavigation(this, R.id.navProfile)

        setupClickListeners()
        
        // Start World-Class Animations
        startStaggeredEntrance()
        startAvatarMicroAnimations()
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.topBar),
            findViewById<View>(R.id.heroSection),
            findViewById<View>(R.id.badgeVerified),
            findViewById<View>(R.id.professionalCard),
            findViewById<View>(R.id.tvQuickActionsHeader),
            findViewById<View>(R.id.quickActionsList),
            findViewById<View>(R.id.tvAppVersion)
        )

        // Initial state
        for (view in viewsToAnimate) {
            view?.alpha = 0f
            view?.translationY = 50f
        }

        // Staggered animation
        for (i in viewsToAnimate.indices) {
            viewsToAnimate[i]?.animate()
                ?.alpha(1f)
                ?.translationY(0f)
                ?.setDuration(800)
                ?.setStartDelay((i * 120).toLong())
                ?.setInterpolator(OvershootInterpolator(1.2f))
                ?.start()
        }
    }

    private fun startAvatarMicroAnimations() {
        val avatar = findViewById<View>(R.id.avatarContainer)
        
        // Breathing Effect
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.05f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.05f)
        ObjectAnimator.ofPropertyValuesHolder(avatar, scaleX, scaleY).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Floating Effect
        ObjectAnimator.ofFloat(avatar, View.TRANSLATION_Y, -15f, 15f).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun setupClickListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        // Click Listeners for all rows
        findViewById<ImageButton>(R.id.btnEditTop).setOnClickListener {
            startActivity(Intent(this, EditProfessionalProfileActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.actionEdit).setOnClickListener {
            startActivity(Intent(this, EditProfessionalProfileActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.actionPassword).setOnClickListener {
            startActivity(Intent(this, SecurityPasswordActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.actionPrivacy).setOnClickListener {
            startActivity(Intent(this, PrivacyTermsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.actionAbout).setOnClickListener {
            startActivity(Intent(this, AboutAppActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.actionLogout).setOnClickListener {
            showLogoutDialog()
        }

        findViewById<LinearLayout>(R.id.actionDelete).setOnClickListener {
            showDeleteConfirmation()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        // Dynamic User Info
        val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
        val name = prefs.getString("USER_FULL_NAME", "Profile Name") ?: "Profile Name"
        val specialization = prefs.getString("USER_SPECIALIZATION", "Dental Professional") ?: "Dental Professional"
        val qualification = prefs.getString("USER_QUALIFICATION", "Not Specified") ?: "Not Specified"
        val email = prefs.getString("USER_EMAIL", "Not Specified") ?: "Not Specified"
        val phone = prefs.getString("USER_PHONE", "Not Specified") ?: "Not Specified"
        val exp = prefs.getString("USER_EXPERIENCE", "Not Specified") ?: "Not Specified"

        findViewById<TextView>(R.id.tvDoctorName).text = name
        findViewById<TextView>(R.id.tvDetailName).text = name
        findViewById<TextView>(R.id.tvSpecialty).text = specialization.uppercase()
        findViewById<TextView>(R.id.tvDetailSpec).text = specialization
        findViewById<TextView>(R.id.tvDetailQual).text = qualification
        findViewById<TextView>(R.id.tvDetailEmail).text = email
        findViewById<TextView>(R.id.tvDetailPhone).text = phone
        findViewById<TextView>(R.id.tvDetailExp).text = exp
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_premium_confirm, null)
        val dialog = AlertDialog.Builder(this, R.style.PremiumDialogTheme)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Logout Account"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = "Are you sure you want to sign out? You will need to enter your credentials to log back in."
        
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        btnConfirm.text = "Logout"
        btnConfirm.setBackgroundColor(getColor(R.color.error))
        
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            
            // Clear all user session data
            val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showDeleteConfirmation() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_premium_confirm, null)
        val dialog = AlertDialog.Builder(this, R.style.PremiumDialogTheme)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.dialogTitle).text = "Delete Account"
        dialogView.findViewById<TextView>(R.id.dialogMessage).text = "This action is PERMANENT. All your patient data and clinical records will be lost. Are you absolutely sure?"
        
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        btnConfirm.text = "Delete Forever"
        btnConfirm.setBackgroundColor(getColor(R.color.error))
        
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            
            // Clear all user session data
            val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // In real app, call API here
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
