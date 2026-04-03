package com.simats.prosthoplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.simats.prosthoplanner.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientsListActivity : AppCompatActivity() {

    private lateinit var llPatients: LinearLayout
    private lateinit var masterList: MutableList<Triple<String, String, String>>
    private var currentFilter = "All"
    private var currentSearchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients_list)
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        llPatients = findViewById(R.id.llPatients)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val chipAll = findViewById<Chip>(R.id.chipAll)
        val chipActive = findViewById<Chip>(R.id.chipActive)
        val chipCompleted = findViewById<Chip>(R.id.chipCompleted)

        findViewById<View>(R.id.btnNotificationPatients).setOnClickListener {
             Toast.makeText(this, "Opening Notifications", Toast.LENGTH_SHORT).show()
        }

        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)

        // Default prototype patients
        masterList = mutableListOf(
            Triple("John Doe", "P-99283", "MALE, 45Y"),
            Triple("Sarah Smith", "P-88210", "FEMALE, 32Y"),
            Triple("Michael Brown", "P-77456", "MALE, 58Y"),
            Triple("Emily Davis", "P-66123", "FEMALE, 29Y"),
            Triple("Ram", "P-11223", "MALE, 28Y")
        )

        // Load dynamic
        val dynamicNames = prefs.getStringSet("PATIENT_NAMES", emptySet()) ?: emptySet()
        dynamicNames.forEach { name ->
            if (masterList.none { it.first == name }) {
                masterList.add(Triple(name, "P-${(10000..99999).random()}", "RECENT ANALYSIS"))
            }
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString().lowercase()
                updateList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        chipAll.setOnClickListener {
            updateFilterSelection(chipAll, chipActive, chipCompleted)
            currentFilter = "All"
            updateList()
        }
        chipActive.setOnClickListener {
            updateFilterSelection(chipActive, chipAll, chipCompleted)
            currentFilter = "Active"
            updateList()
        }
        chipCompleted.setOnClickListener {
            updateFilterSelection(chipCompleted, chipAll, chipActive)
            currentFilter = "Completed"
            updateList()
        }

        updateList()
        fetchPatientsFromBackend()
        
        // Start World-Class Entrance
        startEntranceAnimations()
    }

    private fun startEntranceAnimations() {
        val header = findViewById<View>(R.id.headerPatients)
        val filter = findViewById<View>(R.id.filterAreaPatients)
        
        header.alpha = 0f
        header.translationY = -30f
        header.animate().alpha(1f).translationY(0f).setDuration(800).setInterpolator(OvershootInterpolator()).start()
        
        filter.alpha = 0f
        filter.translationY = 30f
        filter.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(200).setInterpolator(OvershootInterpolator()).start()
    }

    private fun fetchPatientsFromBackend() {
        RetrofitClient.instance.getPatients().enqueue(object : Callback<PatientListResponse> {
            override fun onResponse(call: Call<PatientListResponse>, response: Response<PatientListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val backendPatients = response.body()!!.patients
                    masterList.clear()
                    backendPatients.forEach { p ->
                        val subText = "${p.gender.uppercase()}, ${p.age}Y" + (if (p.selected_plan != null) " | ${p.selected_plan}" else "")
                        masterList.add(Triple(p.full_name, p.patient_external_id, subText))
                    }
                    updateList()
                }
            }
            override fun onFailure(call: Call<PatientListResponse>, t: Throwable) {}
        })
    }

    private fun updateFilterSelection(selected: Chip, vararg others: Chip) {
        selected.isChecked = true
        selected.setChipBackgroundColorResource(R.color.accent_cyan)
        selected.setTextColor(resources.getColor(R.color.slate_bg_start))
        selected.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start()
        
        others.forEach { 
            it.isChecked = false
            it.setChipBackgroundColorResource(android.R.color.transparent)
            it.setTextColor(resources.getColor(R.color.white))
            it.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }
    }

    private fun updateList() {
        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        val filteredList = masterList.filter { patient ->
            val matchesSearch = patient.first.lowercase().contains(currentSearchQuery) || 
                              patient.second.lowercase().contains(currentSearchQuery)
            
            val progress = prefs.getInt("PROGRESS_${patient.first}", 0)
            val matchesFilter = when (currentFilter) {
                "Active" -> progress in 1..99
                "Completed" -> progress == 100
                else -> true
            }
            matchesSearch && matchesFilter
        }
        renderPatients(filteredList)
    }

    private fun renderPatients(patients: List<Triple<String, String, String>>) {
        llPatients.removeAllViews()
        val inflater = LayoutInflater.from(this)
        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)

        patients.forEachIndexed { index, data ->
            val patientView = inflater.inflate(R.layout.layout_patient_item, llPatients, false)
            
            patientView.findViewById<TextView>(R.id.tvPatientName).text = data.first
            patientView.findViewById<TextView>(R.id.tvCaseId).text = "CASE ID: ${data.second}"
            patientView.findViewById<TextView>(R.id.tvLastVisit).text = data.third

            val progress = prefs.getInt("PROGRESS_${data.first}", 0)
            val chipStatus = patientView.findViewById<Chip>(R.id.chipStatus)
            if (progress > 0) {
                chipStatus.text = "$progress% COMPLETE"
            }

            patientView.setOnClickListener {
                val intent = Intent(this, PatientDetailActivity::class.java).apply {
                    putExtra("PATIENT_NAME", data.first)
                    putExtra("CASE_ID", data.second)
                    putExtra("PATIENT_INFO", data.third)
                }
                startActivity(intent)
            }
            
            // Staggered Entrance Animation for items
            patientView.alpha = 0f
            patientView.translationX = 50f
            patientView.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(600)
                .setStartDelay(300 + (index * 80).toLong())
                .setInterpolator(OvershootInterpolator(1.0f))
                .start()
                
            llPatients.addView(patientView)
        }

        val isEmpty = patients.isEmpty()
        findViewById<View>(R.id.viewEmptyState).visibility = if (isEmpty) View.VISIBLE else View.GONE
        findViewById<View>(R.id.rvPatientsScroll).visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}
