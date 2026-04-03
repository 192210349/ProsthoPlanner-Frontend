package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class CaseReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_review)

        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "John Doe"
        val patientId = intent.getStringExtra("PATIENT_ID") ?: "PP99283"
        findViewById<TextView>(R.id.tvPatientHeader).text = "$patientName | #$patientId"

        findViewById<CardView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<CardView>(R.id.cardAiDashboard).setOnClickListener {
            val intent = Intent(this, AiDashboardActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardTreatmentPlan).setOnClickListener {
            val intent = Intent(this, TreatmentPlanOverviewActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardReports).setOnClickListener {
            val intent = Intent(this, CaseReportActivity::class.java)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.cardNotes).setOnClickListener {
            val intent = Intent(this, ClinicalNotesActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAiPlan).setOnClickListener {
            val intent = Intent(this, AiProcessingActivity::class.java).apply {
                // Pass all data to the real AI engine
                putExtra("PATIENT_NAME", intent.getStringExtra("PATIENT_NAME"))
                putExtra("PATIENT_ID", intent.getStringExtra("PATIENT_ID"))
                putExtra("PATIENT_AGE", intent.getStringExtra("PATIENT_AGE"))
                putExtra("PATIENT_GENDER", intent.getStringExtra("PATIENT_GENDER"))
                putExtra("PATIENT_MOBILE", intent.getStringExtra("PATIENT_MOBILE"))
                putExtra("PATIENT_CONDITIONS", intent.getStringArrayListExtra("PATIENT_CONDITIONS"))
                putExtra("PATIENT_HABITS", intent.getStringArrayListExtra("PATIENT_HABITS"))
                putExtra("opg_uri", intent.getStringExtra("opg_uri"))
                putExtra("cbct_uri", intent.getStringExtra("cbct_uri"))
                putExtra("KENNEDY_CLASS", intent.getStringExtra("KENNEDY_CLASS"))
                putExtra("TISSUE_CONDITION", intent.getStringExtra("TISSUE_CONDITION"))
                putExtra("OCCLUSION_TYPE", intent.getStringExtra("OCCLUSION_TYPE"))
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnSaveCase).setOnClickListener {
            Toast.makeText(this, "Case Saved Successfully!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
