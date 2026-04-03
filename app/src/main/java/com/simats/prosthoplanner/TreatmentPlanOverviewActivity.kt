package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TreatmentPlanOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_plan_overview)
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        findViewById<View>(R.id.btnBack).setOnClickListener { onBackPressed() }

        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "John Doe"
        val treatmentType = intent.getStringExtra("TREATMENT_TYPE") ?: "FLEXIBLE ESTHETIC RPD"

        findViewById<TextView>(R.id.tvPatientName).text = patientName
        findViewById<TextView>(R.id.tvTreatmentType).text = treatmentType
        
        findViewById<Button>(R.id.btnViewRoadmap).setOnClickListener {
            // Adjust Treatment Parameters navigation
            val intent = Intent(this, CaseReviewActivity::class.java).apply {
                putExtra("PATIENT_NAME", patientName)
            }
            startActivity(intent)
        }
        
        // Start Cinematic Roadmap Sequence
        startRoadmapSequence()
    }

    private fun startRoadmapSequence() {
        val overview = findViewById<View>(R.id.overviewCardPlan)
        val roadmapTitle = findViewById<View>(R.id.tvRoadmapTitle)
        val roadmapCard = findViewById<View>(R.id.roadmapCardPlan)
        val phase1 = findViewById<View>(R.id.phase1Node)
        val phase2 = findViewById<View>(R.id.phase2Node)
        val phase3 = findViewById<View>(R.id.phase3Node)
        val line1 = findViewById<View>(R.id.linePhase1)
        val line2 = findViewById<View>(R.id.linePhase2)
        val button = findViewById<View>(R.id.btnViewRoadmap)

        // Initial state
        val allViews = arrayOf(overview, roadmapTitle, roadmapCard, phase1, phase2, phase3, line1, line2, button)
        for (view in allViews) {
            view?.alpha = 0f
            view?.translationY = 30f
        }

        // Staggered sequence
        overview.animate().alpha(1f).translationY(0f).setDuration(800).setInterpolator(OvershootInterpolator(1.0f)).start()
        
        roadmapTitle.animate().alpha(1f).translationY(0f).setDuration(600).setStartDelay(300).start()
        roadmapCard.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(400).start()
        
        // Nodes drop-in
        phase1.animate().alpha(1f).scaleX(1.1f).scaleY(1.1f).setDuration(400).setStartDelay(600).withEndAction {
            phase1.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }.start()
        
        line1.animate().alpha(1f).scaleY(1f).setDuration(400).setStartDelay(800).start()
        
        phase2.animate().alpha(1f).scaleX(1.1f).scaleY(1.1f).setDuration(400).setStartDelay(1000).withEndAction {
            phase2.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }.start()
        
        line2.animate().alpha(1f).scaleY(1f).setDuration(400).setStartDelay(1200).start()
        
        phase3.animate().alpha(1f).scaleX(1.1f).scaleY(1.1f).setDuration(400).setStartDelay(1400).withEndAction {
            phase3.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }.start()
        
        button.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(1600).setInterpolator(OvershootInterpolator(1.2f)).start()
    }
}
