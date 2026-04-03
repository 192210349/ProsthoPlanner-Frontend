package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class CompleteProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        setupSpinners()

        findViewById<Button>(R.id.btnContinue).setOnClickListener {
            // Save professional profile data
            val prefs = getSharedPreferences("ProsthoPrefs", android.content.Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("USER_SPECIALIZATION", findViewById<Spinner>(R.id.spinnerSpecialization).selectedItem.toString())
            editor.putString("USER_QUALIFICATION", findViewById<Spinner>(R.id.spinnerQualification).selectedItem.toString())
            editor.putString("USER_EXPERIENCE", findViewById<Spinner>(R.id.spinnerExperience).selectedItem.toString())
            editor.apply()

            // Navigate to Clinic Setup
            val intent = Intent(this, ClinicSetupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupSpinners() {
        // 1. Specialization
        val specializationSpinner = findViewById<Spinner>(R.id.spinnerSpecialization)
        val specializations = arrayOf(
            "Prosthodontist",
            "Implantologist",
            "Oral & Maxillofacial Surgeon",
            "Periodontist",
            "Endodontist",
            "Orthodontist",
            "General Dentist",
            "Oral Radiologist",
            "Cosmetic Dentist",
            "Dental Surgeon",
            "Dental Specialist",
            "Academic / Research Dentist"
        )
        val specAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, specializations)
        specializationSpinner.adapter = specAdapter
        specializationSpinner.setSelection(0) // Default: Prosthodontist

        // 2. Qualification
        val qualificationSpinner = findViewById<Spinner>(R.id.spinnerQualification)
        val qualifications = arrayOf(
            "BDS",
            "MDS – Prosthodontics",
            "MDS – Oral Surgery",
            "MDS – Periodontics",
            "MDS – Endodontics",
            "MDS – Orthodontics",
            "DNB Dentistry",
            "PhD in Dentistry",
            "Fellowship in Implantology",
            "Fellowship in Prosthodontics",
            "PG Diploma in Implantology",
            "Other"
        )
        val qualAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qualifications)
        qualificationSpinner.adapter = qualAdapter

        // 3. Experience
        val experienceSpinner = findViewById<Spinner>(R.id.spinnerExperience)
        val experiences = arrayOf(
            "0 – 1 years",
            "1 – 3 years",
            "3 – 5 years",
            "5 – 10 years",
            "10 – 15 years",
            "15 – 20 years",
            "20+ years"
        )
        val expAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, experiences)
        experienceSpinner.adapter = expAdapter
    }
}
