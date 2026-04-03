package com.simats.prosthoplanner

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CaseReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_case_report)

        findViewById<androidx.cardview.widget.CardView>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnDownload).setOnClickListener {
            Toast.makeText(this, "Preparing PDF for download...", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.btnShare).setOnClickListener {
            Toast.makeText(this, "Opening share options...", Toast.LENGTH_SHORT).show()
        }
    }
}
