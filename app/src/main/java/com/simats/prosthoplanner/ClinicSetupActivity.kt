package com.simats.prosthoplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ClinicSetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_setup)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnFinish).setOnClickListener {
            // Save Clinic Data for personalization
            val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            
            editor.putString("CLINIC_NAME", findViewById<EditText>(R.id.etClinicName).text.toString())
            editor.putString("CLINIC_LICENSE", findViewById<EditText>(R.id.etLicenseNumber).text.toString())
            editor.putString("CLINIC_ADDRESS", findViewById<EditText>(R.id.etClinicAddress).text.toString())
            editor.putString("CLINIC_CITY", findViewById<EditText>(R.id.etCity).text.toString())
            editor.putString("CLINIC_COUNTRY", findViewById<EditText>(R.id.etCountry).text.toString())
            editor.putString("CLINIC_HOSPITAL", findViewById<EditText>(R.id.etHospital).text.toString())
            editor.putString("CLINIC_FEE", findViewById<EditText>(R.id.etConsultationFee).text.toString())
            
            editor.apply()

            // Navigate to Sign In
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
