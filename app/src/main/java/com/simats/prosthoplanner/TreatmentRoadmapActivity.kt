package com.simats.prosthoplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TreatmentRoadmapActivity : AppCompatActivity() {

    private lateinit var llRoadmapContainer: LinearLayout
    private var completedWeeksSet = mutableSetOf<Int>()
    private var totalWeeks = 0
    private lateinit var patientName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_roadmap)
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        llRoadmapContainer = findViewById(R.id.llRoadmapContainer)
        findViewById<View>(R.id.btnBack).setOnClickListener { onBackPressed() }

        patientName = intent.getStringExtra("PATIENT_NAME") ?: "Patient"
        val planTime = intent.getStringExtra("PLAN_TIME") ?: "4 Weeks"
        val planTreatment = intent.getStringExtra("PLAN_TREATMENT") ?: "General Treatment"

        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        
        // Robust parsing of duration
        totalWeeks = try {
            val parts = planTime.split(" ")
            val numberPart = parts[0]
            val lastNum = numberPart.split("-").last().toInt()
            if (planTime.contains("Month", true)) lastNum * 4 else lastNum
        } catch (e: Exception) { 4 }
        
        val completedString = prefs.getString("COMPLETED_WEEKS_$patientName", "") ?: ""
        if (completedString.isNotEmpty()) {
            completedWeeksSet = completedString.split(",").filter { it.isNotEmpty() }.map { it.toInt() }.toMutableSet()
        }

        generateRoadmap(totalWeeks, planTreatment)

        findViewById<Button>(R.id.btnDone).setOnClickListener {
            saveProgress()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        
        startRoadmapEntrance()
    }

    private fun startRoadmapEntrance() {
        val header = findViewById<View>(R.id.headerRoadmap)
        header.alpha = 0f
        header.translationY = -20f
        header.animate().alpha(1f).translationY(0f).setDuration(800).start()
    }

    private fun generateRoadmap(weeks: Int, treatment: String) {
        llRoadmapContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)
        
        for (i in 1..weeks) {
            val weekView = inflater.inflate(R.layout.layout_roadmap_week_item, llRoadmapContainer, false)
            
            weekView.findViewById<TextView>(R.id.tvWeekTitle).text = "Week $i"
            val statusTag = weekView.findViewById<TextView>(R.id.tvStatusTag)
            val cbComplete = weekView.findViewById<CheckBox>(R.id.cbWeekComplete)
            val llSubTasks = weekView.findViewById<LinearLayout>(R.id.llSubTasks)

            val weekPlan = getWeeklyPlan(i, treatment)
            weekView.findViewById<TextView>(R.id.tvWeekTask).text = weekPlan.first

            llSubTasks.removeAllViews()
            weekPlan.second.forEach { subTask ->
                val subTaskView = inflater.inflate(R.layout.layout_roadmap_sub_task_item, llSubTasks, false)
                subTaskView.findViewById<TextView>(R.id.tvSubTask).text = subTask
                llSubTasks.addView(subTaskView)
            }

            if (completedWeeksSet.contains(i)) {
                cbComplete.isChecked = true
                updateTagUI(statusTag, true)
            }

            cbComplete.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) completedWeeksSet.add(i) else completedWeeksSet.remove(i)
                updateTagUI(statusTag, isChecked)
                saveProgress()
                
                if (isChecked) {
                    weekView.animate().scaleX(1.02f).scaleY(1.02f).setDuration(200).withEndAction {
                        weekView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                    }.start()
                }
            }

            // Staggered Entrance
            weekView.alpha = 0f
            weekView.translationX = 40f
            weekView.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(700)
                .setStartDelay(200 + (i * 100).toLong())
                .setInterpolator(OvershootInterpolator(1.0f))
                .start()

            llRoadmapContainer.addView(weekView)
        }
    }

    private fun updateTagUI(statusTag: TextView, isChecked: Boolean) {
        if (isChecked) {
            statusTag.text = "Completed"
            statusTag.setTextColor(resources.getColor(R.color.white))
            statusTag.setBackgroundResource(R.drawable.glass_card_premium)
            statusTag.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#10B981"))
        } else {
            statusTag.text = "Pending"
            statusTag.setTextColor(resources.getColor(R.color.white))
            statusTag.setBackgroundResource(R.drawable.glass_card_premium)
            statusTag.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#1AFFFFFF"))
        }
    }

    private fun saveProgress() {
        val prefs = getSharedPreferences("ProsthoPrefs", Context.MODE_PRIVATE)
        val completedCount = completedWeeksSet.size
        val percent = if (totalWeeks > 0) (completedCount.toFloat() / totalWeeks.toFloat() * 100).toInt() else 0
        prefs.edit().apply {
            putInt("PROGRESS_$patientName", percent)
            putString("COMPLETED_WEEKS_$patientName", completedWeeksSet.joinToString(","))
            apply()
        }
    }

    private fun getWeeklyPlan(week: Int, treatment: String): Pair<String, List<String>> {
        return when {
            treatment.contains("Implant", true) -> when (week) {
                1 -> "Foundational Prep" to listOf("High-res Digital CBCT Scan", "Surgical Guide Virtual Design", "Pre-op Antibiotic Prophylaxis")
                2 -> "Surgical Phase" to listOf("Atraumatic Single Tooth Extraction", "Immediate Implant Placement", "Primary Stability Verification")
                3 -> "Early Recovery" to listOf("Post-op Hemostasis Check", "Soft Tissue Approximation Check", "Suture Removal")
                else -> "Clinical Monitoring" to listOf("Bone maturation monitoring", "Hygiene maintenance instruction", "Stability verification")
            }
            treatment.contains("Bridge", true) -> when (week) {
                1 -> "Preparation Phase" to listOf("Abutment tooth reduction", "Provisional Fabrication", "Master PVS Impression")
                2 -> "Framework Design" to listOf("Zirconia Framework try-in", "Shade matching", "Interocclusal record refinement")
                3 -> "Final Cementation" to listOf("Static & Dynamic Occlusion Check", "Bonding/Cementation Protocol", "Cleaning demo")
                else -> "Follow-up" to listOf("Clinical success verification", "Adjacent tooth check", "Hygiene protocol")
            }
            else -> "Clinical Phase" to listOf("Routine clinical monitoring", "Adjustment of existing prosthesis", "Hygiene maintenance instruction")
        }
    }
}
