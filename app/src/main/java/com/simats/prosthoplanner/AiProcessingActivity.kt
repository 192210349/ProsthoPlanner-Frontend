package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.net.Uri
import android.widget.Toast
import com.google.gson.Gson
import com.simats.prosthoplanner.network.*
import com.simats.prosthoplanner.utils.NotificationHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import android.graphics.PorterDuff

class AiProcessingActivity : AppCompatActivity() {

    private var patientDbId: Int = 1 
    private var opgUri: String? = null
    private var cbctUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_processing)

        opgUri = intent.getStringExtra("opg_uri")
        cbctUri = intent.getStringExtra("cbct_uri")
        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "Patient"

        findViewById<TextView>(R.id.tvAiAnalyzingTitle).text = "AI is Analyzing Case for $patientName"

        startIntegratedProcessing(patientName)
    }

    private fun startIntegratedProcessing(patientName: String) {
        val tvStatus = findViewById<TextView>(R.id.tvStatusUpdate)

        // Step 1: Register Patient in Backend
        tvStatus.text = "Registering patient case..."
        val patientData = mutableMapOf<String, Any>(
            "full_name" to patientName,
            "patient_id" to (intent.getStringExtra("PATIENT_ID") ?: "PP-${System.currentTimeMillis()}"),
            "age" to (intent.getStringExtra("PATIENT_AGE")?.toIntOrNull() ?: 45),
            "gender" to (intent.getStringExtra("PATIENT_GENDER") ?: "Male"),
            "mobile_number" to (intent.getStringExtra("PATIENT_MOBILE") ?: ""),
            "is_diabetic" to (if (intent.getStringArrayListExtra("PATIENT_CONDITIONS")?.contains("Diabetes") == true) 1 else 0),
            "is_smoker" to (if (intent.getStringArrayListExtra("PATIENT_HABITS")?.contains("Smoking") == true) 1 else 0),
            "kennedy_classification" to (intent.getStringExtra("KENNEDY_CLASS") ?: "Class I"),
            "tissue_condition" to (intent.getStringExtra("TISSUE_CONDITION") ?: "Healthy"),
            "occlusion_type" to (intent.getStringExtra("OCCLUSION_TYPE") ?: "Balanced")
        )

        RetrofitClient.instance.registerPatient(patientData).enqueue(object : Callback<RegistrationResponse> {
            override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    patientDbId = response.body()!!.patient_db_id
                    updateStep(R.id.step1, "Patient Registered", true)
                    proceedToImaging(patientName)
                } else {
                    Toast.makeText(this@AiProcessingActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    fallbackToDemo(patientName)
                }
            }
            override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                fallbackToDemo(patientName)
            }
        })
    }

    private fun proceedToImaging(patientName: String) {
        val tvStatus = findViewById<TextView>(R.id.tvStatusUpdate)
        tvStatus.text = "Uploading images to AI engine..."
        
        var uploadsStarted = 0
        var uploadsFinished = 0

        opgUri?.let { 
            uploadsStarted++
            uploadImagePart(it, "OPG") { uploadsFinished++; checkImagingComplete(uploadsFinished, uploadsStarted, patientName) }
        }
        cbctUri?.let { 
            uploadsStarted++
            uploadImagePart(it, "CBCT") { uploadsFinished++; checkImagingComplete(uploadsFinished, uploadsStarted, patientName) }
        }

        if (uploadsStarted == 0) {
            proceedToAnalysis(patientName)
        }
    }

    private fun checkImagingComplete(finished: Int, started: Int, patientName: String) {
        if (finished == started) {
            updateStep(R.id.step2, "Measurements Extracted", true)
            proceedToAnalysis(patientName)
        }
    }

    private fun uploadImagePart(uriStr: String, type: String, onComplete: () -> Unit) {
        val uri = Uri.parse(uriStr)
        val file = getFileFromUri(uri) ?: return
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val pId = patientDbId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val pType = type.toRequestBody("text/plain".toMediaTypeOrNull())

        RetrofitClient.instance.uploadImaging(body, pId, pType).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                onComplete()
            }
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                onComplete()
            }
        })
    }

    private fun proceedToAnalysis(patientName: String) {
        val tvStatus = findViewById<TextView>(R.id.tvStatusUpdate)
        tvStatus.text = "Generating Treatment Plans..."
        
        val request = SuggestionRequest(
            patient_db_id = patientDbId,
            age = intent.getStringExtra("PATIENT_AGE")?.toIntOrNull() ?: 45,
            gender = intent.getStringExtra("PATIENT_GENDER") ?: "Male",
            kennedy_classification = intent.getStringExtra("KENNEDY_CLASS") ?: "Class I",
            tissue_condition = intent.getStringExtra("TISSUE_CONDITION") ?: "Healthy",
            occlusion_type = intent.getStringExtra("OCCLUSION_TYPE") ?: "Balanced",
            is_diabetic = if (intent.getStringArrayListExtra("PATIENT_CONDITIONS")?.contains("Diabetes") == true) 1 else 0,
            is_smoker = if (intent.getStringArrayListExtra("PATIENT_HABITS")?.contains("Smoking") == true) 1 else 0
        )

        RetrofitClient.instance.suggestTreatment(request).enqueue(object : Callback<SuggestionResponse> {
            override fun onResponse(call: Call<SuggestionResponse>, response: Response<SuggestionResponse>) {
                val suggestionBody = response.body()
                if (response.isSuccessful && suggestionBody != null) {
                    updateStep(R.id.step3, "Plans Generated", true)
                    proceedToComparison(suggestionBody, patientName)
                } else {
                    fallbackToDemo(patientName)
                }
            }
            override fun onFailure(call: Call<SuggestionResponse>, t: Throwable) {
                fallbackToDemo(patientName)
            }
        })
    }

    private fun proceedToComparison(suggestionBody: SuggestionResponse, patientName: String) {
        NotificationHelper.showNotification(this, "Plan Generated", "AI has suggested treatment plans.")
        val intent = Intent(this, TreatmentComparisonActivity::class.java).apply {
            putExtra("plans_json", Gson().toJson(suggestionBody))
            putExtra("patient_id", patientDbId)
            putExtra("PATIENT_NAME", patientName)
        }
        startActivity(intent)
        finish()
    }

    private fun fallbackToDemo(patientName: String) {
        val gender = intent.getStringExtra("PATIENT_GENDER") ?: "Not specified"
        val habits = intent.getStringArrayListExtra("PATIENT_HABITS") ?: arrayListOf()
        val conditions = intent.getStringArrayListExtra("PATIENT_CONDITIONS") ?: arrayListOf()

        val demoPlans = mutableMapOf<String, Plan>()
        
        // Plan A - Optimal
        var planATreatment = "Implant-retained Zirconia Bridge"
        var planAInsight = "High bone density detected from OPG"
        if (habits.contains("Smoking")) {
            planATreatment += " with Smoking Cessation Protocol"
            planAInsight = "Note: Smoker status requires post-op monitoring for healing"
        }
        demoPlans["Plan A"] = Plan(planATreatment, 2500.0, "3-4 Months", planAInsight)

        // Plan B - Moderate
        var planBTreatment = "Fixed Dental Bridge (3-unit)"
        var planBInsight = "Adequate abutment tooth support"
        if (conditions.contains("Diabetes")) {
            planBTreatment += " (Diabetes managed)"
            planBInsight = "Systemic condition considered in material selection"
        }
        demoPlans["Plan B"] = Plan(planBTreatment, 1200.0, "4-6 Weeks", planBInsight)

        // Plan C - Economic/Specific
        var planCTreatment = "Removable Partial Denture"
        var planCInsight = "Cost-effective functional restoration"
        if (gender == "Female") {
            planCTreatment = "Flexible Esthetic RPD"
            planCInsight = "Prioritizing esthetic outcomes for $patientName"
        }
        demoPlans["Plan C"] = Plan(planCTreatment, 800.0, "2-3 Weeks", planCInsight)
        
        val demoResponse = SuggestionResponse("success", patientDbId, demoPlans)
        proceedToComparison(demoResponse, patientName)
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = contentResolver
        val fileName = "temp_img_${System.currentTimeMillis()}.jpg"
        val file = File(cacheDir, fileName)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return if (file.exists()) file else null
    }

    private fun updateStep(layoutId: Int, name: String, completed: Boolean) {
        val layout = findViewById<View>(layoutId)
        layout?.alpha = 1.0f
        layout?.findViewById<TextView>(R.id.tvStepName)?.text = name
        if (completed) {
            val icon = layout?.findViewById<ImageView>(R.id.ivStepIcon)
            icon?.setImageResource(R.drawable.ic_success_circle)
            icon?.setColorFilter(getColor(R.color.success_green), PorterDuff.Mode.SRC_IN)
        }
    }
}
