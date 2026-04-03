package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AiTreatmentSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_treatment_summary)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackSimulation).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnFinalize).setOnClickListener {
            startActivity(Intent(this, TreatmentComparisonActivity::class.java))
        }
    }
}
