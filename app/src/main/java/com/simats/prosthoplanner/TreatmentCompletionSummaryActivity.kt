package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TreatmentCompletionSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_completion_summary)

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            // Return to patient detail hub
            val intent = Intent(this, PatientDetailActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnViewReport).setOnClickListener {
            startActivity(Intent(this, CaseReportActivity::class.java))
        }
    }
}
