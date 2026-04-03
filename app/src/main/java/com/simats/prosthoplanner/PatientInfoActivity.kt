package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PatientInfoActivity : AppCompatActivity() {

    private lateinit var etPatientName: EditText
    private lateinit var etPatientAge: EditText
    private lateinit var etPatientMobile: EditText
    private lateinit var etPatientId: EditText
    private lateinit var spinnerGender: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)

        // Initialize Views
        etPatientName = findViewById(R.id.etPatientName)
        etPatientAge = findViewById(R.id.etPatientAge)
        etPatientMobile = findViewById(R.id.etPatientMobile)
        etPatientId = findViewById(R.id.etPatientId)
        spinnerGender = findViewById(R.id.spinnerGender)

        setupGenderSpinner()
        setupClickListeners()
        loadCaseData()
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.header),
            findViewById<View>(R.id.indicatorContainer),
            findViewById<View>(R.id.tvFormTitle),
            findViewById<View>(R.id.tvFormSubtitle),
            findViewById<View>(R.id.formCard),
            findViewById<View>(R.id.lblFullName),
            etPatientName,
            findViewById<View>(R.id.gridAgeGender),
            findViewById<View>(R.id.lblMobile),
            etPatientMobile,
            findViewById<View>(R.id.lblPatientId),
            etPatientId,
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

    private fun setupGenderSpinner() {
        val genderOptions = arrayOf("Select Gender", "Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = adapter
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            validateAndProceed()
        }
    }

    private fun loadCaseData() {
        // Load existing data if editing or returning
        val name = intent.getStringExtra("PATIENT_NAME")
        val age = intent.getStringExtra("PATIENT_AGE")
        val mobile = intent.getStringExtra("PATIENT_MOBILE")
        val patientId = intent.getStringExtra("PATIENT_ID")
        val gender = intent.getStringExtra("PATIENT_GENDER")

        etPatientName.setText(name)
        etPatientAge.setText(age)
        etPatientMobile.setText(mobile)
        etPatientId.setText(patientId)

        if (gender != null) {
            val genderOptions = arrayOf("Select Gender", "Male", "Female", "Other")
            val index = genderOptions.indexOf(gender)
            if (index >= 0) spinnerGender.setSelection(index)
        }
    }

    private fun validateAndProceed() {
        val name = etPatientName.text.toString().trim()
        val age = etPatientAge.text.toString().trim()
        val mobile = etPatientMobile.text.toString().trim()
        val gender = spinnerGender.selectedItem.toString()

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter patient name", Toast.LENGTH_SHORT).show()
            return
        }
        if (gender == "Select Gender") {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
            return
        }
        if (mobile.isNotEmpty() && mobile.length < 10) {
            Toast.makeText(this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, MedicalHistoryActivity::class.java).apply {
            putExtra("PATIENT_NAME", name)
            putExtra("PATIENT_AGE", age)
            putExtra("PATIENT_GENDER", gender)
            putExtra("PATIENT_MOBILE", mobile)
            putExtra("PATIENT_ID", etPatientId.text.toString().trim())
        }
        startActivity(intent)
    }
}
