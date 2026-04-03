package com.simats.prosthoplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class EditProfessionalProfileActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var autoQual: MaterialAutoCompleteTextView
    private lateinit var autoSpec: MaterialAutoCompleteTextView
    private lateinit var autoExp: MaterialAutoCompleteTextView
    
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_professional_profile)

        initViews()
        setupDropdowns()
        setupListeners()
        applyEntryAnimations()
    }

    private fun initViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        autoQual = findViewById(R.id.autoQual)
        autoSpec = findViewById(R.id.autoSpec)
        autoExp = findViewById(R.id.autoExp)

        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        
        loadProfileData()
    }

    private fun setupDropdowns() {
        val qualifications = resources.getStringArray(R.array.qualifications)
        val qualAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, qualifications)
        autoQual.setAdapter(qualAdapter)

        val experience = resources.getStringArray(R.array.years_of_experience)
        val expAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, experience)
        autoExp.setAdapter(expAdapter)
    }

    private fun loadProfileData() {
        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        etFullName.setText(prefs.getString("USER_FULL_NAME", "Dr. Sarah Johnson"))
        etEmail.setText(prefs.getString("USER_EMAIL", "sarah.j@clinic.com"))
        etPhone.setText(prefs.getString("USER_PHONE", "+91 98765 43210"))
        autoQual.setText(prefs.getString("USER_QUALIFICATION", "MDS"), false)
        autoSpec.setText(prefs.getString("USER_SPECIALIZATION", "Prosthodontist"), false)
        autoExp.setText(prefs.getString("USER_EXPERIENCE", "10-15 Years"), false)
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        btnCancel.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            validateAndSave()
        }
        
        findViewById<androidx.cardview.widget.CardView>(R.id.btnChangeImage)?.setOnClickListener {
            Toast.makeText(this, "Opening Image Picker...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyEntryAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.scale_up_premium)
        findViewById<androidx.core.widget.NestedScrollView>(R.id.formScroll)?.startAnimation(fadeIn)
    }

    private fun validateAndSave() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val exp = autoExp.text.toString().trim()

        var isValid = true

        val nameValidation = com.simats.prosthoplanner.utils.ValidationUtils.validateName(name)
        if (!nameValidation.first) {
            Toast.makeText(this, nameValidation.second ?: "Invalid Name", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        val phoneValidation = com.simats.prosthoplanner.utils.ValidationUtils.validatePhone(phone)
        if (!phoneValidation.first) {
            Toast.makeText(this, phoneValidation.second ?: "Invalid Phone", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (exp.isEmpty()) {
            Toast.makeText(this, "Experience is required", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (isValid) {
            performSave(name, email, phone, exp)
        }
    }

    private fun performSave(name: String, email: String, phone: String, exp: String) {
        btnSave.isEnabled = false
        btnSave.text = "Saving Changes..."

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("USER_FULL_NAME", name)
            editor.putString("USER_EMAIL", email)
            editor.putString("USER_PHONE", phone)
            editor.putString("USER_QUALIFICATION", autoQual.text.toString())
            editor.putString("USER_SPECIALIZATION", autoSpec.text.toString())
            editor.putString("USER_EXPERIENCE", exp)
            editor.apply()

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            finish()
        }, 1000)
    }
}
