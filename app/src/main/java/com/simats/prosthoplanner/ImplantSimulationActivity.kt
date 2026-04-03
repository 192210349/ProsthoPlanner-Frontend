package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider

class ImplantSimulationActivity : AppCompatActivity() {

    private lateinit var ivImplantMarker: ImageView
    private lateinit var tvDiameterLabel: TextView
    private lateinit var tvLengthLabel: TextView
    private lateinit var tvAngleLabel: TextView
    private lateinit var tvBoneContact: TextView
    
    private var currentDiameter = 4.5f
    private var currentLength = 11.5f
    private var currentAngle = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_implant_simulation)

        ivImplantMarker = findViewById(R.id.ivImplantMarker)
        tvDiameterLabel = findViewById(R.id.tvDiameterLabel)
        tvLengthLabel = findViewById(R.id.tvLengthLabel)
        tvAngleLabel = findViewById(R.id.tvAngleLabel)
        tvBoneContact = findViewById(R.id.tvBoneContact)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        setupSliders()
        setupButtons()
    }

    private fun setupSliders() {
        val sliderDiameter = findViewById<Slider>(R.id.sliderDiameter)
        val sliderLength = findViewById<Slider>(R.id.sliderLength)
        val sliderAngle = findViewById<Slider>(R.id.sliderAngle)

        sliderDiameter.addOnChangeListener { _, value, _ ->
            currentDiameter = value
            tvDiameterLabel.text = "Diameter: ${String.format("%.1f", value)} mm"
            updateImplantVisual()
        }

        sliderLength.addOnChangeListener { _, value, _ ->
            currentLength = value
            tvLengthLabel.text = "Length: ${String.format("%.1f", value)} mm"
            updateImplantVisual()
        }

        sliderAngle.addOnChangeListener { _, value, _ ->
            currentAngle = value
            tvAngleLabel.text = "Angle: ${String.format("%.1f", value)}°"
            updateImplantVisual()
        }
    }

    private fun updateImplantVisual() {
        // Simple scaling and rotation to simulate visual feedback
        val baseWidth = 40 // dp-ish scaling
        val baseHeight = 120
        
        val scaleX = currentDiameter / 4.5f
        val scaleY = currentLength / 11.5f
        
        ivImplantMarker.scaleX = scaleX
        ivImplantMarker.scaleY = scaleY
        ivImplantMarker.rotation = currentAngle
        
        // Mocking bone contact calculation
        val contact = 80 + (currentDiameter * 2) - (Math.abs(currentAngle) / 5)
        tvBoneContact.text = "${Math.min(98.0, contact.toDouble()).toInt()}%"
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            Toast.makeText(this, "Simulation data saved", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnGeneratePlan).setOnClickListener {
            Toast.makeText(this, "Generating Treatment Options...", Toast.LENGTH_SHORT).show()
            handler.postDelayed({
                startActivity(Intent(this, TreatmentComparisonActivity::class.java))
            }, 1000)
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            startActivity(Intent(this, AiTreatmentSummaryActivity::class.java))
        }
    }

    private val handler = Handler(Looper.getMainLooper())
}
