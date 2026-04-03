package com.simats.prosthoplanner

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AnalyticsDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_dashboard)

        val slideUp = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.slide_up_premium)
        findViewById<android.view.View>(R.id.scrollView).startAnimation(slideUp)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            onBackPressed()
        }

        // Summary Cards
        findViewById<androidx.cardview.widget.CardView>(R.id.cardTotalCases).setOnClickListener {
            startActivity(Intent(this, CaseAnalyticsDetailActivity::class.java))
        }
        findViewById<androidx.cardview.widget.CardView>(R.id.cardSuccessRate).setOnClickListener {
            startActivity(Intent(this, TreatmentOutcomeSummaryActivity::class.java))
        }
        findViewById<androidx.cardview.widget.CardView>(R.id.cardActiveCases).setOnClickListener {
            startActivity(Intent(this, CaseAnalyticsDetailActivity::class.java))
        }
        findViewById<androidx.cardview.widget.CardView>(R.id.cardAiProcessed).setOnClickListener {
            startActivity(Intent(this, AiModelPerformanceActivity::class.java))
        }

        // Case Distribution
        findViewById<androidx.cardview.widget.CardView>(R.id.cardCaseDistribution).setOnClickListener {
            startActivity(Intent(this, CaseAnalyticsDetailActivity::class.java))
        }

        // Insights
        val insightListener = {
            startActivity(Intent(this, ClinicalDecisionInsightsActivity::class.java))
        }
        findViewById<androidx.cardview.widget.CardView>(R.id.cardInsightBoneQuality).setOnClickListener { insightListener() }
        findViewById<androidx.cardview.widget.CardView>(R.id.cardInsightImplantSite).setOnClickListener { insightListener() }

        // Quick Access Tiles
        findViewById<android.view.View>(R.id.tileCaseAnalytics).setOnClickListener {
            startActivity(Intent(this, CaseAnalyticsDetailActivity::class.java))
        }
        findViewById<android.view.View>(R.id.tileDecisionInsights).setOnClickListener {
            startActivity(Intent(this, ClinicalDecisionInsightsActivity::class.java))
        }
        findViewById<android.view.View>(R.id.tileTreatmentOutcome).setOnClickListener {
            startActivity(Intent(this, TreatmentOutcomeSummaryActivity::class.java))
        }
        findViewById<android.view.View>(R.id.tileModelPerformance).setOnClickListener {
            startActivity(Intent(this, AiModelPerformanceActivity::class.java))
        }
        findViewById<android.view.View>(R.id.tileSystemUpdates).setOnClickListener {
            startActivity(Intent(this, AiSystemUpdatesActivity::class.java))
        }

        // Bottom Navigation
        NavigationHelper.setupPremiumNavigation(this, R.id.navAnalytics)
    }
}
