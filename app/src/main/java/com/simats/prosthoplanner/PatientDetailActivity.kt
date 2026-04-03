package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PatientDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail)
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        findViewById<View>(R.id.btnBackDetail).setOnClickListener { onBackPressed() }

        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "John Doe"
        val caseId = intent.getStringExtra("CASE_ID") ?: "#PP99283"
        val patientInfo = intent.getStringExtra("PATIENT_INFO") ?: "MALE, 45Y"

        findViewById<TextView>(R.id.tvPatientNameDetail).text = patientName
        findViewById<TextView>(R.id.tvCaseIdDetail).text = "CASE ID: $caseId"
        
        findViewById<Button>(R.id.btnOpenCaseCommandDetail).setOnClickListener {
            val intent = Intent(this, CaseReviewActivity::class.java).apply {
                putExtra("PATIENT_NAME", patientName)
                putExtra("PATIENT_ID", caseId.removePrefix("#"))
            }
            startActivity(intent)
        }
        
        // Start Multi-Stage Cinematic Sequence
        startCinematicSequence()
    }

    private fun startCinematicSequence() {
        val hero = findViewById<View>(R.id.heroCardDetail)
        val title = findViewById<View>(R.id.tvStatusTitleDetail)
        val grid = findViewById<View>(R.id.gridDiagnosticsDetail)
        val summary = findViewById<View>(R.id.summaryBlockDetail)
        val button = findViewById<View>(R.id.btnOpenCaseCommandDetail)

        val viewsToAnimate = arrayOf(hero, title, grid, summary, button)
        
        // Initial state
        for (view in viewsToAnimate) {
            view?.alpha = 0f
            view?.translationY = 40f
        }

        // Staggered sequence
        for (i in viewsToAnimate.indices) {
            viewsToAnimate[i]?.animate()
                ?.alpha(1f)
                ?.translationY(0f)
                ?.setDuration(800)
                ?.setStartDelay(100 + (i * 120).toLong())
                ?.setInterpolator(OvershootInterpolator(1.1f))
                ?.start()
        }
    }
}
