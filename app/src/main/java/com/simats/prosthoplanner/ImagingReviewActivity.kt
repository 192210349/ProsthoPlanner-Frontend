package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImagingReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imaging_review)

        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "John Doe"
        val patientAge = intent.getStringExtra("PATIENT_AGE") ?: "42"
        val patientGender = intent.getStringExtra("PATIENT_GENDER") ?: "Male"
        val patientId = intent.getStringExtra("PATIENT_ID") ?: "PP-89234"

        findViewById<android.widget.TextView>(R.id.tvPatientName).text = patientName
        findViewById<android.widget.TextView>(R.id.tvCaseId).text = "Case ID: #$patientId | ${patientAge}Y $patientGender"

        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        val opgUri = intent.getStringExtra("opg_uri")
        val cbctUri = intent.getStringExtra("cbct_uri")
        val photoUri = intent.getStringExtra("photo_uri")

        opgUri?.let { Glide.with(this).load(Uri.parse(it)).into(findViewById<ImageView>(R.id.ivOpgPreview)) }
        cbctUri?.let { 
            findViewById<ImageView>(R.id.ivCbctPreview).setPadding(0, 0, 0, 0)
            Glide.with(this).load(Uri.parse(it)).into(findViewById<ImageView>(R.id.ivCbctPreview)) 
        }
        photoUri?.let { 
            findViewById<ImageView>(R.id.ivPhotoPreview).setPadding(0, 0, 0, 0)
            Glide.with(this).load(Uri.parse(it)).into(findViewById<ImageView>(R.id.ivPhotoPreview)) 
        }

        val slideUp = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up_premium)
        findViewById<android.view.View>(R.id.header).startAnimation(slideUp)
        findViewById<android.view.View>(R.id.bottomActions).startAnimation(slideUp)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { onBackPressed() }
        findViewById<Button>(R.id.btnBackUpload).setOnClickListener { onBackPressed() }

        findViewById<Button>(R.id.btnContinueAi).setOnClickListener {
            val intent = Intent(this, AiProcessingActivity::class.java).apply {
                putExtra("opg_uri", opgUri)
                putExtra("cbct_uri", cbctUri)
                putExtra("photo_uri", photoUri)
                putExtra("PATIENT_NAME", intent.getStringExtra("PATIENT_NAME"))
                putExtra("PATIENT_GENDER", intent.getStringExtra("PATIENT_GENDER"))
                putExtra("PATIENT_CONDITIONS", intent.getStringArrayListExtra("PATIENT_CONDITIONS"))
                putExtra("PATIENT_HABITS", intent.getStringArrayListExtra("PATIENT_HABITS"))
            }
            startActivity(intent)
        }
    }
}
