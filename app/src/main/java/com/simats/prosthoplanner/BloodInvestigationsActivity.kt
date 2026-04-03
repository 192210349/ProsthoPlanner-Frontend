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

class BloodInvestigationsActivity : AppCompatActivity() {

    private lateinit var etHb: EditText
    private lateinit var etBloodSugar: EditText
    private lateinit var etHbA1c: EditText
    private lateinit var etBleedingTime: EditText
    private lateinit var etClottingTime: EditText
    private lateinit var etLabNotes: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_investigations)

        // Initialize Views
        etHb = findViewById(R.id.etHb)
        etBloodSugar = findViewById(R.id.etBloodSugar)
        etHbA1c = findViewById(R.id.etHbA1c)
        etBleedingTime = findViewById(R.id.etBleedingTime)
        etClottingTime = findViewById(R.id.etClottingTime)
        etLabNotes = findViewById(R.id.etLabNotes)

        setupStepIndicator()
        setupClickListeners()
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun setupStepIndicator() {
        val tvStepLabel = findViewById<TextView>(R.id.tvStepLabel)
        tvStepLabel?.text = "Step 3: Blood Investigations"
        
        // Find dots from the included layout (StepInv)
        val dot1 = findViewById<View>(R.id.dotStep1)
        val dot2 = findViewById<View>(R.id.dotStep2)
        val dot3 = findViewById<View>(R.id.dotStep3)
        val line1 = findViewById<View>(R.id.lineStep1to2)
        val line2 = findViewById<View>(R.id.lineStep2to3)

        dot1?.setBackgroundResource(R.drawable.circle_indicator_active)
        line1?.alpha = 1.0f
        dot2?.setBackgroundResource(R.drawable.circle_indicator_active)
        line2?.alpha = 1.0f
        dot3?.setBackgroundResource(R.drawable.circle_indicator_active)
        dot3?.scaleX = 1.2f
        dot3?.scaleY = 1.2f
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.headerInvestigations),
            findViewById<View>(R.id.indicatorContainerInv),
            findViewById<View>(R.id.tvInvTitle),
            findViewById<View>(R.id.tvInvSubtitle),
            findViewById<View>(R.id.formCardInv),
            findViewById<View>(R.id.rowHb),
            findViewById<View>(R.id.rowSugar),
            findViewById<View>(R.id.rowHbA1c),
            findViewById<View>(R.id.rowBleeding),
            findViewById<View>(R.id.rowClotting),
            findViewById<View>(R.id.lblLabNotes),
            etLabNotes,
            findViewById<View>(R.id.bottomActionsInv)
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
                ?.setStartDelay((i * 80).toLong())
                ?.setInterpolator(OvershootInterpolator(1.2f))
                ?.start()
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnBackInvestigations).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackInvStep).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnNextInv).setOnClickListener {
            proceedToNextStep()
        }
    }

    private fun proceedToNextStep() {
        val nextIntent = Intent(this, ImagingUploadActivity::class.java).apply {
            // Carry over all previous data
            putExtra("PATIENT_NAME", intent.getStringExtra("PATIENT_NAME"))
            putExtra("PATIENT_GENDER", intent.getStringExtra("PATIENT_GENDER"))
            putExtra("PATIENT_AGE", intent.getStringExtra("PATIENT_AGE"))
            putExtra("PATIENT_MOBILE", intent.getStringExtra("PATIENT_MOBILE"))
            putExtra("PATIENT_ID", intent.getStringExtra("PATIENT_ID"))
            putExtra("PATIENT_CONDITIONS", intent.getStringArrayListExtra("PATIENT_CONDITIONS"))
            putExtra("PATIENT_HABITS", intent.getStringArrayListExtra("PATIENT_HABITS"))
            putExtra("PATIENT_ALLERGIES", intent.getStringExtra("PATIENT_ALLERGIES"))
            putExtra("PATIENT_MEDICATIONS", intent.getStringExtra("PATIENT_MEDICATIONS"))
            
            // Add Step 3 data
            putExtra("PATIENT_HB", etHb.text.toString().trim())
            putExtra("PATIENT_SUGAR", etBloodSugar.text.toString().trim())
            putExtra("PATIENT_HBA1C", etHbA1c.text.toString().trim())
            putExtra("PATIENT_BLEEDING", etBleedingTime.text.toString().trim())
            putExtra("PATIENT_CLOTTING", etClottingTime.text.toString().trim())
            putExtra("PATIENT_LAB_NOTES", etLabNotes.text.toString().trim())
        }
        startActivity(nextIntent)
    }
}
