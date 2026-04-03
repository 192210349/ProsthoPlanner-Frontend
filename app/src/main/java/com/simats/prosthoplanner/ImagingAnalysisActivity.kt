package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ImagingAnalysisActivity : AppCompatActivity() {

    private lateinit var skeletonLayout: View
    private lateinit var cardsGrid: View
    private lateinit var btnNext: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imaging_analysis)

        // Initialize Views
        skeletonLayout = findViewById(R.id.skeletonLayout)
        cardsGrid = findViewById(R.id.cardsGrid)
        btnNext = findViewById(R.id.btnNextStep)
        btnBack = findViewById(R.id.btnBackReview)

        val btnBackHeader = findViewById<ImageButton>(R.id.btnBack)
        btnBackHeader.setOnClickListener { onBackPressed() }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnNext.setOnClickListener {
            startActivity(Intent(this, BoneMeasurementActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewBoneMeasurement).setOnClickListener {
            startActivity(Intent(this, BoneMeasurementActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewSimulation).setOnClickListener {
            startActivity(Intent(this, ImplantSimulationActivity::class.java))
        }

        // Mock Navigation for interactive elements
        findViewById<ImageButton>(R.id.btnZoom).setOnClickListener {
            Toast.makeText(this, "Zoom feature coming soon", Toast.LENGTH_SHORT).show()
        }
        findViewById<ImageButton>(R.id.btnMarker).setOnClickListener {
            Toast.makeText(this, "Marker overlay enabled", Toast.LENGTH_SHORT).show()
        }

        // Setup AI Analysis Cards
        setupAnalysisCards()

        // Simulate Loading
        loadData()
    }

    private fun setupAnalysisCards() {
        // Bone Height
        updateCard(R.id.cardBoneHeight, "Bone Height", "12.5 mm", "92%", "Sufficient bone height for implant placement", R.drawable.ic_imaging)
        
        // Bone Width
        updateCard(R.id.cardBoneWidth, "Bone Width", "6.8 mm", "88%", "Adequate width for standard diameter implant", R.drawable.ic_imaging)
        
        // Bone Density
        updateCard(R.id.cardBoneDensity, "Bone Density", "Type D2", "85%", "Good cortical bone with dense trabecular core", R.drawable.ic_analytics)
        
        // Implant Site
        updateCard(R.id.cardImplantSite, "Implant Site", "Site #46", "95%", "Optimal placement identified at first molar site", R.drawable.ic_add_case)
    }

    private fun updateCard(cardId: Int, title: String, value: String, confidence: String, explanation: String, iconRes: Int) {
        val root = findViewById<View>(cardId)
        root.findViewById<TextView>(R.id.tvTitle).text = title
        root.findViewById<TextView>(R.id.tvValue).text = value
        root.findViewById<TextView>(R.id.tvConfidence).text = confidence
        root.findViewById<TextView>(R.id.tvExplanation).text = explanation
        root.findViewById<ImageView>(R.id.ivIcon).setImageResource(iconRes)
    }

    private fun loadData() {
        // Show skeleton
        skeletonLayout.visibility = View.VISIBLE
        cardsGrid.visibility = View.GONE

        // Mock API Call Delay
        Handler(Looper.getMainLooper()).postDelayed({
            skeletonLayout.visibility = View.GONE
            cardsGrid.visibility = View.VISIBLE
            
            // Fade-in animation
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 800
            cardsGrid.startAnimation(fadeIn)
            
            Toast.makeText(this, "AI Analysis Complete", Toast.LENGTH_SHORT).show()
        }, 2000)
    }
}
