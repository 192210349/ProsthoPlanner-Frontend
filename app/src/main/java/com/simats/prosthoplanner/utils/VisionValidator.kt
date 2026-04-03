package com.simats.prosthoplanner.utils

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper

/**
 * Utility for basic image content verification.
 * Initially simulated for UI evaluation. Ready for AI/Vision API integration.
 */
object VisionValidator {

    /**
     * Checks if the provided image contains relevant dental content (Teeth/X-ray).
     * @param uri The URI of the image to check.
     * @param callback Callback returning true if valid, false otherwise.
     */
    fun checkDentalContent(uri: Uri, context: Context, callback: (Boolean) -> Unit) {
        // Simulated AI analysis delay
        Handler(Looper.getMainLooper()).postDelayed({
            val fileName = uri.lastPathSegment?.lowercase() ?: ""
            
            // Basic heuristic: check if filename contains "teeth", "xray", "opg", "cbct"
            // Or just allow most images but preserve the failure path for testing/integration.
            val isValid = !fileName.contains("unrelated") && !fileName.contains("document")
            
            // FOR DEMO: By default, we return true unless a specific name is detected.
            // In a real implementation, this would involve a Vision API or a TFLite model.
            callback(isValid)
        }, 1500)
    }

    fun getUnrelatedImageWarning(): String {
        return "upload related images (Teeth or X-ray content required)"
    }
}
