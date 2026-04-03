package com.simats.prosthoplanner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImagingUploadActivity : AppCompatActivity() {

    private var opgUri: Uri? = null
    private var cbctUri: Uri? = null
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imaging_upload)

        setupPatientContext()
        setupStepIndicator()
        setupClickListeners()
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun setupPatientContext() {
        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "John Doe"
        val patientAge = intent.getStringExtra("PATIENT_AGE") ?: "42"
        val patientGender = intent.getStringExtra("PATIENT_GENDER") ?: "Male"
        val patientId = intent.getStringExtra("PATIENT_ID") ?: "PP-89234"

        findViewById<TextView>(R.id.tvPatientName).text = patientName
        findViewById<TextView>(R.id.tvCaseId).text = "Case ID: #$patientId | ${patientAge}Y $patientGender"
        
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)
    }

    private fun setupStepIndicator() {
        val tvStepLabel = findViewById<TextView>(R.id.tvStepLabel)
        tvStepLabel?.text = "Step 4: Imaging Upload"
        
        // Find dots from the included layout
        val dot1 = findViewById<View>(R.id.dotStep1)
        val dot2 = findViewById<View>(R.id.dotStep2)
        val dot3 = findViewById<View>(R.id.dotStep3)
        val dot4 = findViewById<View>(R.id.dotStep4)
        val line1 = findViewById<View>(R.id.lineStep1to2)
        val line2 = findViewById<View>(R.id.lineStep2to3)
        val line3 = findViewById<View>(R.id.lineStep3to4)

        dot1?.setBackgroundResource(R.drawable.circle_indicator_active)
        line1?.alpha = 1.0f
        dot2?.setBackgroundResource(R.drawable.circle_indicator_active)
        line2?.alpha = 1.0f
        dot3?.setBackgroundResource(R.drawable.circle_indicator_active)
        line3?.alpha = 1.0f
        dot4?.setBackgroundResource(R.drawable.circle_indicator_active)
        dot4?.scaleX = 1.2f
        dot4?.scaleY = 1.2f
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.headerUpload),
            findViewById<View>(R.id.indicatorContainerUpload),
            findViewById<View>(R.id.tvUploadTitle),
            findViewById<View>(R.id.tvUploadSubtitle),
            findViewById<View>(R.id.patientCardUpload),
            findViewById<View>(R.id.frameOpg),
            findViewById<View>(R.id.frameCbct),
            findViewById<View>(R.id.framePhotos),
            findViewById<View>(R.id.bottomActionsUpload)
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
                ?.setStartDelay((i * 90).toLong())
                ?.setInterpolator(OvershootInterpolator(1.2f))
                ?.start()
        }
    }

    private fun setupClickListeners() {
        findViewById<View>(R.id.btnBackUpload).setOnClickListener {
            onBackPressed()
        }

        findViewById<Button>(R.id.btnBackUploadStep).setOnClickListener {
            onBackPressed()
        }

        findViewById<View>(R.id.btnUploadOpg).setOnClickListener { pickOpgLauncher.launch("image/*") }
        findViewById<View>(R.id.btnUploadCbct).setOnClickListener { pickCbctLauncher.launch("image/*") }
        findViewById<View>(R.id.btnUploadPhotos).setOnClickListener { pickPhotosLauncher.launch("image/*") }

        findViewById<Button>(R.id.btnStartAi).setOnClickListener {
            if (opgUri == null && cbctUri == null && photoUri == null) {
                Toast.makeText(this, "Please upload at least one image", Toast.LENGTH_SHORT).show()
            } else {
                startAiAnalysis()
            }
        }
    }

    private fun updateThumbnail(uri: Uri, preview: ImageView, uploadUi: View) {
        uploadUi.visibility = View.GONE
        preview.visibility = View.VISIBLE
        Glide.with(this).load(uri).centerCrop().into(preview)
    }

    private val pickOpgLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            validateAndProcessImage(it, "OPG") {
                opgUri = it
                updateThumbnail(it, findViewById(R.id.ivOpgPreview), findViewById(R.id.uploadUiOpg))
            }
        }
    }

    private val pickCbctLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            validateAndProcessImage(it, "CBCT") {
                cbctUri = it
                updateThumbnail(it, findViewById(R.id.ivCbctPreview), findViewById(R.id.uploadUiCbct))
            }
        }
    }

    private val pickPhotosLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            validateAndProcessImage(it, "Photo") {
                photoUri = it
                updateThumbnail(it, findViewById(R.id.ivPhotosPreview), findViewById(R.id.uploadUiPhotos))
            }
        }
    }

    private fun validateAndProcessImage(uri: Uri, type: String, onSuccess: () -> Unit) {
        val progressToast = Toast.makeText(this, "Analyzing $type content...", Toast.LENGTH_SHORT)
        progressToast.show()
        
        com.simats.prosthoplanner.utils.VisionValidator.checkDentalContent(uri, this) { isValid ->
            progressToast.cancel()
            if (isValid) {
                onSuccess()
            } else {
                Toast.makeText(this, com.simats.prosthoplanner.utils.VisionValidator.getUnrelatedImageWarning(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startAiAnalysis() {
        val intent = Intent(this, ClinicalExamActivity::class.java).apply {
            putExtra("opg_uri", opgUri?.toString())
            putExtra("cbct_uri", cbctUri?.toString())
            putExtra("photo_uri", photoUri?.toString())
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
        }
        startActivity(intent)
    }
}
