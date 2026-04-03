package com.simats.prosthoplanner

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

object NavigationHelper {

    fun setupPremiumNavigation(activity: Activity, currentTabId: Int) {
        val navHome = activity.findViewById<View>(R.id.navHome)
        val navPatients = activity.findViewById<View>(R.id.navPatients)
        val navAnalytics = activity.findViewById<View>(R.id.navAnalytics)
        val navProfile = activity.findViewById<View>(R.id.navProfile)

        // Highlight active tab
        highlightTab(activity, currentTabId)

        navHome?.setOnClickListener {
            if (currentTabId != R.id.navHome) {
                activity.startActivity(Intent(activity, DashboardActivity::class.java))
                if (activity !is DashboardActivity) activity.finish()
            }
        }

        navPatients?.setOnClickListener {
            if (currentTabId != R.id.navPatients) {
                activity.startActivity(Intent(activity, PatientsListActivity::class.java))
            }
        }

        navAnalytics?.setOnClickListener {
            if (currentTabId != R.id.navAnalytics) {
                activity.startActivity(Intent(activity, AnalyticsDashboardActivity::class.java))
            }
        }

        navProfile?.setOnClickListener {
            if (currentTabId != R.id.navProfile) {
                activity.startActivity(Intent(activity, ProfileSettingsActivity::class.java))
            }
        }
    }

    private fun highlightTab(activity: Activity, currentTabId: Int) {
        val teal = ContextCompat.getColor(activity, R.color.primary_teal)
        val grey = ContextCompat.getColor(activity, R.color.text_grey)

        // Reset all
        resetTab(activity, R.id.ivNavHome, R.id.tvNavHome, grey)
        resetTab(activity, R.id.ivNavPatients, R.id.tvNavPatients, grey)
        resetTab(activity, R.id.ivNavAnalytics, R.id.tvNavAnalytics, grey)
        resetTab(activity, R.id.ivNavProfile, R.id.tvNavProfile, grey)

        // Highlight active
        when (currentTabId) {
            R.id.navHome -> setActive(activity, R.id.ivNavHome, R.id.tvNavHome, teal)
            R.id.navPatients -> setActive(activity, R.id.ivNavPatients, R.id.tvNavPatients, teal)
            R.id.navAnalytics -> setActive(activity, R.id.ivNavAnalytics, R.id.tvNavAnalytics, teal)
            R.id.navProfile -> setActive(activity, R.id.ivNavProfile, R.id.tvNavProfile, teal)
        }
    }

    private fun resetTab(activity: Activity, iconId: Int, textId: Int, color: Int) {
        activity.findViewById<ImageView>(iconId)?.setColorFilter(color)
        activity.findViewById<TextView>(textId)?.setTextColor(color)
        activity.findViewById<TextView>(textId)?.setTypeface(null, android.graphics.Typeface.NORMAL)
    }

    private fun setActive(activity: Activity, iconId: Int, textId: Int, color: Int) {
        activity.findViewById<ImageView>(iconId)?.setColorFilter(color)
        activity.findViewById<TextView>(textId)?.setTextColor(color)
        activity.findViewById<TextView>(textId)?.setTypeface(null, android.graphics.Typeface.BOLD)
    }
}
