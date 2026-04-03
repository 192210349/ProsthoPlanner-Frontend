package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WeeklyTreatmentDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_treatment_detail)

        val weekNum = intent.getIntExtra("WEEK_NUMBER", 1)
        findViewById<TextView>(R.id.tvWeekHeading).text = "Week $weekNum Treatment"

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnCompleteStep).setOnClickListener {
            Toast.makeText(this, "Step Marked as Completed", Toast.LENGTH_SHORT).show()
            // In a real app we would update the DB and unlock the next week
            // For the demo, let's navigate to a success summary if it's the final week
            if (weekNum >= 4) { // Let's pretend it's a short 4 week demo plan for now
                startActivity(Intent(this, TreatmentCompletionSummaryActivity::class.java))
            } else {
                finish()
            }
        }
    }
}
