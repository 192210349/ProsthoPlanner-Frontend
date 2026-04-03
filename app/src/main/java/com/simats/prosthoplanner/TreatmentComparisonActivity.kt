package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.simats.prosthoplanner.network.Plan
import com.simats.prosthoplanner.network.RetrofitClient
import com.simats.prosthoplanner.network.SelectPlanRequest
import com.simats.prosthoplanner.network.SuggestionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TreatmentComparisonActivity : AppCompatActivity() {

    private var selectedPlan: Plan? = null
    private var selectedPlanTitle: String? = null
    private var patientDbId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_comparison)
        NavigationHelper.setupPremiumNavigation(this, R.id.navPatients)

        findViewById<ImageButton>(R.id.btnBackComparison).setOnClickListener { onBackPressed() }

        val plansJson = intent.getStringExtra("plans_json")
        patientDbId = intent.getIntExtra("patient_id", 1)
        val patientName = intent.getStringExtra("PATIENT_NAME") ?: "Patient"

        findViewById<TextView>(R.id.tvTreatmentPlanTitle).text = "Select Option for $patientName"
        findViewById<TextView>(R.id.tvPatientSub).text = "AI has generated 3 possible clinical paths for $patientName."

        val response = Gson().fromJson(plansJson, SuggestionResponse::class.java)
        
        response?.plans?.let { plans ->
            plans["Plan A"]?.let { updatePlanCard(findViewById(R.id.planA), "Plan A: Optimal", it) }
            plans["Plan B"]?.let { updatePlanCard(findViewById(R.id.planB), "Plan B: Moderate", it) }
            plans["Plan C"]?.let { updatePlanCard(findViewById(R.id.planC), "Plan C: Economic", it) }
            
            // Pre-select Plan A as optimal
            plans["Plan A"]?.let { highlightCard(findViewById(R.id.planA), "Plan A: Optimal", it) }
        }

        findViewById<Button>(R.id.btnProceedCustom).setOnClickListener {
            selectedPlan?.let { plan ->
                val request = SelectPlanRequest(patientDbId, selectedPlanTitle ?: "Plan A")
                RetrofitClient.instance.selectPlan(request).enqueue(object : Callback<com.simats.prosthoplanner.network.BaseResponse> {
                    override fun onResponse(call: Call<com.simats.prosthoplanner.network.BaseResponse>, response: Response<com.simats.prosthoplanner.network.BaseResponse>) {
                        navigateToOverview(plan, patientName)
                    }
                    override fun onFailure(call: Call<com.simats.prosthoplanner.network.BaseResponse>, t: Throwable) {
                        navigateToOverview(plan, patientName)
                    }
                })
            } ?: run {
                Toast.makeText(this, "Please select a plan first", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Start World-Class Animations
        startStaggeredEntrance()
    }

    private fun navigateToOverview(plan: Plan, patientName: String) {
        val intent = Intent(this@TreatmentComparisonActivity, TreatmentPlanOverviewActivity::class.java).apply {
            putExtra("SELECTED_PLAN_JSON", Gson().toJson(plan))
            putExtra("SELECTED_PLAN_TITLE", selectedPlanTitle)
            putExtra("PATIENT_NAME", patientName)
            putExtra("PATIENT_ID", patientDbId)
        }
        startActivity(intent)
    }

    private fun updatePlanCard(view: View, title: String, plan: Plan) {
        view.findViewById<TextView>(R.id.tvPlanLabel).text = title
        view.findViewById<TextView>(R.id.tvPlanSummary).text = plan.treatment
        view.findViewById<TextView>(R.id.tvPlanCost).text = "Est. Cost: $${plan.cost}"
        view.findViewById<TextView>(R.id.tvPlanTime).text = "Est. Time: ${plan.time}"
        
        val tvInsight = view.findViewById<TextView>(R.id.tvPlanInsight)
        if (plan.vision_insight != null) {
            tvInsight.visibility = View.VISIBLE
            tvInsight.text = "AI Observation: ${plan.vision_insight}"
        } else {
            tvInsight.visibility = View.GONE
        }

        view.setOnClickListener {
            highlightCard(view, title, plan)
        }
    }

    private fun highlightCard(view: View, title: String, plan: Plan) {
        // Reset backgrounds
        findViewById<View>(R.id.planA).setBackgroundResource(R.drawable.glass_card_premium)
        findViewById<View>(R.id.planB).setBackgroundResource(R.drawable.glass_card_premium)
        findViewById<View>(R.id.planC).setBackgroundResource(R.drawable.glass_card_premium)

        // Highlight selected with vibrant cyan outline
        view.setBackgroundResource(R.drawable.glass_card_highlighted)
        
        selectedPlan = plan
        selectedPlanTitle = title
    }

    private fun startStaggeredEntrance() {
        val viewsToAnimate = arrayOf(
            findViewById<View>(R.id.headerComparison),
            findViewById<View>(R.id.tvTreatmentPlanTitle),
            findViewById<View>(R.id.tvPatientSub),
            findViewById<View>(R.id.planA),
            findViewById<View>(R.id.planB),
            findViewById<View>(R.id.planC),
            findViewById<View>(R.id.btnProceedCustom)
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
}
