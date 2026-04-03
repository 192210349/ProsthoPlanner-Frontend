package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ClinicalExamActivity : AppCompatActivity() {

    private lateinit var etEdentulousArea: EditText
    private lateinit var spinKennedy: Spinner
    private lateinit var spinTissue: Spinner
    private lateinit var spinOcclusion: Spinner
    private lateinit var etClinicalNotes: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinical_exam)

        // Initialize Views
        etEdentulousArea = findViewById(R.id.etEdentulousArea)
        spinKennedy = findViewById(R.id.spinnerKennedy)
        spinTissue = findViewById(R.id.spinnerTissue)
        spinOcclusion = findViewById(R.id.spinnerOcclusion)
        etClinicalNotes = findViewById(R.id.etClinicalNotes)

        setupSpinners()
        setupStepIndicator()
        setupClickListeners()
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun setupSpinners() {
        val kennedyOptions = arrayOf("Select Class", "Class I", "Class II", "Class III", "Class IV")
        val tissueOptions = arrayOf("Select Condition", "Firm", "Flabby", "Inflamed", "Normal")
        val occlusionOptions = arrayOf("Select Type", "Normal", "Overbite", "Underbite", "Crossbite")

        setupSpinner(spinKennedy, kennedyOptions)
        setupSpinner(spinTissue, tissueOptions)
        setupSpinner(spinOcclusion, occlusionOptions)
    }

    private fun setupSpinner(spinner: Spinner, options: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupStepIndicator() {
        val tvStepLabel = findViewById<TextView>(R.id.tvStepLabel)
        tvStepLabel?.text = "Step 5: Clinical Examination"
        
        // Find dots from the included layout
        val dot1 = findViewById<View>(R.id.dotStep1)
        val dot2 = findViewById<View>(R.id.dotStep2)
        val dot3 = findViewById<View>(R.id.dotStep3)
        val dot4 = findViewById<View>(R.id.dotStep4)
        val dot5 = findViewById<View>(R.id.dotStep5)
        val line1 = findViewById<View>(R.id.lineStep1to2)
        val line2 = findViewById<View>(R.id.lineStep2to3)
        val line3 = findViewById<View>(R.id.lineStep3to4)
        val line4 = findViewById<View>(R.id.lineStep4to5)

        dot1?.setBackgroundResource(R.drawable.circle_indicator_active)
        line1?.alpha = 1.0f
        dot2?.setBackgroundResource(R.drawable.circle_indicator_active)
        line2?.alpha = 1.0f
        dot3?.setBackgroundResource(R.drawable.circle_indicator_active)
        line3?.alpha = 1.0f
        dot4?.setBackgroundResource(R.drawable.circle_indicator_active)
        line4?.alpha = 1.0f
        dot5?.setBackgroundResource(R.drawable.circle_indicator_active)
        
        // Final Step Special Pulse Animation
        dot5?.animate()?.scaleX(1.3f)?.scaleY(1.3f)?.setDuration(1000)?.setInterpolator(OvershootInterpolator())?.start()
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.headerClinical),
            findViewById<View>(R.id.indicatorContainerClinical),
            findViewById<View>(R.id.tvClinicalTitle),
            findViewById<View>(R.id.tvClinicalSubtitle),
            findViewById<View>(R.id.formCardClinical),
            findViewById<View>(R.id.lblEdentulous),
            etEdentulousArea,
            findViewById<View>(R.id.lblKennedy),
            spinKennedy,
            findViewById<View>(R.id.lblTissue),
            spinTissue,
            findViewById<View>(R.id.lblOcclusion),
            spinOcclusion,
            findViewById<View>(R.id.lblClinicalNotes),
            etClinicalNotes,
            findViewById<View>(R.id.bottomActionsClinical)
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
                ?.setStartDelay((i * 60).toLong())
                ?.setInterpolator(OvershootInterpolator(1.2f))
                ?.start()
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnBackClinical).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackClinicalStep).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnCompleteAnalysis).setOnClickListener {
            proceedToCompletion()
        }
    }

    private fun proceedToCompletion() {
        val intent = Intent(this, CaseReviewActivity::class.java).apply {
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
            putExtra("PATIENT_HB", intent.getStringExtra("PATIENT_HB"))
            putExtra("PATIENT_SUGAR", intent.getStringExtra("PATIENT_SUGAR"))
            putExtra("PATIENT_HBA1C", intent.getStringExtra("PATIENT_HBA1C"))
            putExtra("PATIENT_BLEEDING", intent.getStringExtra("PATIENT_BLEEDING"))
            putExtra("PATIENT_CLOTTING", intent.getStringExtra("PATIENT_CLOTTING"))
            putExtra("PATIENT_LAB_NOTES", intent.getStringExtra("PATIENT_LAB_NOTES"))
            putExtra("opg_uri", intent.getStringExtra("opg_uri"))
            putExtra("cbct_uri", intent.getStringExtra("cbct_uri"))
            putExtra("photo_uri", intent.getStringExtra("photo_uri"))
            
            // Add Step 5 data
            putExtra("EDENTULOUS_AREA", etEdentulousArea.text.toString().trim())
            putExtra("KENNEDY_CLASS", spinKennedy.selectedItem.toString())
            putExtra("TISSUE_CONDITION", spinTissue.selectedItem.toString())
            putExtra("OCCLUSION_TYPE", spinOcclusion.selectedItem.toString())
            putExtra("CLINICAL_NOTES", etClinicalNotes.text.toString().trim())
        }
        startActivity(intent)
    }
}
