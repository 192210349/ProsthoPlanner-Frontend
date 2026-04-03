package com.simats.prosthoplanner

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class TreatmentOutcomeSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_outcome_summary)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }
    }
}
