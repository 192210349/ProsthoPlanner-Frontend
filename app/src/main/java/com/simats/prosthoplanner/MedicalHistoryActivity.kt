package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MedicalHistoryActivity : AppCompatActivity() {

    private lateinit var etAllergies: EditText
    private lateinit var etMedications: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_history)

        // Initialize Views
        etAllergies = findViewById(R.id.etAllergies)
        etMedications = findViewById(R.id.etMedications)

        setupStepIndicator()
        setupClickListeners()
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun setupStepIndicator() {
        // Redesigned Step Indicator is handled in XML usually but let's ensure Step 2 is active
        val tvStepLabel = findViewById<TextView>(R.id.tvStepLabel)
        tvStepLabel?.text = "Step 2: Medical History"
        
        // Find dots from the included layout
        val dot1 = findViewById<View>(R.id.dotStep1)
        val dot2 = findViewById<View>(R.id.dotStep2)
        val line1 = findViewById<View>(R.id.lineStep1to2)

        dot1?.setBackgroundResource(R.drawable.circle_indicator_active)
        line1?.alpha = 1.0f
        dot2?.setBackgroundResource(R.drawable.circle_indicator_active)
        dot2?.scaleX = 1.2f
        dot2?.scaleY = 1.2f
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.header),
            findViewById<View>(R.id.indicatorArea),
            findViewById<View>(R.id.tvMedicalTitle),
            findViewById<View>(R.id.tvMedicalSubtitle),
            findViewById<View>(R.id.tvSystemicHeader),
            findViewById<View>(R.id.chipGroupConditions),
            findViewById<View>(R.id.tvSocialHeader),
            findViewById<View>(R.id.chipGroupHabits),
            findViewById<View>(R.id.formCardMedical),
            findViewById<View>(R.id.bottomActions)
        )

        // Initial state
        for (view in viewsToAnimate) {
            view?.alpha = 0f
            view?.translationY = 50f
        }

        // Staggered animation
        for (i in viewsToAnimate.indices) {
            viewsToAnimate[i]?.animate()
                ?.alpha(1f)
                ?.translationY(0f)
                ?.setDuration(800)
                ?.setStartDelay((i * 100).toLong())
                ?.setInterpolator(OvershootInterpolator(1.2f))
                ?.start()
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnBackTop).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackStep).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            proceedToNextStep()
        }
    }

    private fun proceedToNextStep() {
        val conditions = mutableListOf<String>()
        val chipGroupConditions = findViewById<ChipGroup>(R.id.chipGroupConditions)
        for (i in 0 until chipGroupConditions.childCount) {
            val chip = chipGroupConditions.getChildAt(i) as Chip
            if (chip.isChecked) conditions.add(chip.text.toString())
        }

        val habits = mutableListOf<String>()
        val chipGroupHabits = findViewById<ChipGroup>(R.id.chipGroupHabits)
        for (i in 0 until chipGroupHabits.childCount) {
            val chip = chipGroupHabits.getChildAt(i) as Chip
            if (chip.isChecked) habits.add(chip.text.toString())
        }

        val intent = Intent(this, BloodInvestigationsActivity::class.java).apply {
            putExtra("PATIENT_NAME", intent.getStringExtra("PATIENT_NAME"))
            putExtra("PATIENT_GENDER", intent.getStringExtra("PATIENT_GENDER"))
            putExtra("PATIENT_AGE", intent.getStringExtra("PATIENT_AGE"))
            putExtra("PATIENT_MOBILE", intent.getStringExtra("PATIENT_MOBILE"))
            putExtra("PATIENT_ID", intent.getStringExtra("PATIENT_ID"))
            putExtra("PATIENT_CONDITIONS", ArrayList(conditions))
            putExtra("PATIENT_HABITS", ArrayList(habits))
            putExtra("PATIENT_ALLERGIES", etAllergies.text.toString())
            putExtra("PATIENT_MEDICATIONS", etMedications.text.toString())
        }
        startActivity(intent)
    }
}
