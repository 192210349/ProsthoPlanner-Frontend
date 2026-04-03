package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class AiDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_dashboard)

        findViewById<androidx.cardview.widget.CardView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<CardView>(R.id.actionImaging).setOnClickListener {
             startActivity(Intent(this, ImagingAnalysisActivity::class.java))
        }

        findViewById<CardView>(R.id.actionMeasurement).setOnClickListener {
             startActivity(Intent(this, BoneMeasurementActivity::class.java))
        }

        findViewById<CardView>(R.id.actionSimulation).setOnClickListener {
             startActivity(Intent(this, ImplantSimulationActivity::class.java))
        }

        findViewById<CardView>(R.id.actionTreatmentSummary).setOnClickListener {
             startActivity(Intent(this, AiTreatmentSummaryActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewTreatmentPlans).setOnClickListener {
            startActivity(Intent(this, TreatmentPlanOverviewActivity::class.java))
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.btnShare).setOnClickListener {
            Toast.makeText(this, "Generating Report...", Toast.LENGTH_SHORT).show()
        }
    }
}
