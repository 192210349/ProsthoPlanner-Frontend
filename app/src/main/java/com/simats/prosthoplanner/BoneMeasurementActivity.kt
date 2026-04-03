package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class BoneMeasurementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bone_measurement)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackAnalysis).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnViewSimulation).setOnClickListener {
            startActivity(Intent(this, ImplantSimulationActivity::class.java))
        }
    }
}
