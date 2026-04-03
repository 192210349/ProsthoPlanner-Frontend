package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Bottom Navigation
        NavigationHelper.setupPremiumNavigation(this, R.id.navHome)

        setupNavigation()
        applyAnimations()
    }

    override fun onResume() {
        super.onResume()
        setupGreeting()
    }

    private fun setupGreeting() {
        val tvGreeting = findViewById<TextView>(R.id.tvGreeting)
        val prefs = getSharedPreferences("ProsthoPrefs", MODE_PRIVATE)
        val userName = prefs.getString("USER_FULL_NAME", "Doctor")
        
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingPrefix = when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
        
        tvGreeting.text = "$greetingPrefix,\n$userName"
    }

    private fun setupNavigation() {
        // Header Notifications
        findViewById<CardView>(R.id.btnNotification).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        // Hero CTAs
        findViewById<MaterialButton>(R.id.btnStartNewCaseHero).setOnClickListener {
            startActivity(Intent(this, PatientInfoActivity::class.java))
        }
        findViewById<CardView>(R.id.btnViewActiveCases).setOnClickListener {
            startActivity(Intent(this, PatientsListActivity::class.java))
        }

        // Quick Action Grid
        findViewById<CardView>(R.id.cardNewCase).setOnClickListener { 
            startActivity(Intent(this, PatientInfoActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardAiAnalysis).setOnClickListener { 
            startActivity(Intent(this, AiDashboardActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardImaging).setOnClickListener { 
            startActivity(Intent(this, ImagingUploadActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardTreatmentPlans).setOnClickListener { 
            startActivity(Intent(this, TreatmentPlanOverviewActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardNotes).setOnClickListener { 
            startActivity(Intent(this, ClinicalNotesActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardPatients).setOnClickListener { 
            startActivity(Intent(this, PatientsListActivity::class.java)) 
        }
        findViewById<CardView>(R.id.cardAnalytics).setOnClickListener { 
            startActivity(Intent(this, AnalyticsDashboardActivity::class.java)) 
        }

        // View All Activity
        findViewById<TextView>(R.id.tvViewAllUpdates).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }
    }

    private fun applyAnimations() {
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_premium)
        val scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up_premium)

        findViewById<View>(R.id.header).startAnimation(scaleUp)
        findViewById<View>(R.id.cardHero).startAnimation(slideUp)

        // Premium Logo Animation
        val ivLogo = findViewById<View>(R.id.ivLogo)
        ivLogo.alpha = 0f
        ivLogo.scaleX = 0.5f
        ivLogo.scaleY = 0.5f
        ivLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
        
        // Staggered reveal for grid
        findViewById<View>(R.id.cardNewCase).startAnimation(slideUp)
        findViewById<View>(R.id.cardAiAnalysis).startAnimation(slideUp)
        findViewById<View>(R.id.cardImaging).startAnimation(slideUp)
        findViewById<View>(R.id.cardTreatmentPlans).startAnimation(slideUp)
    }
}
