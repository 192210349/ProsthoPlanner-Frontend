package com.simats.prosthoplanner.network

import com.google.gson.Gson
import retrofit2.Response

object NetworkErrorHandler {
    fun parseErrorMessage(response: Response<*>): String {
        // 1. Try to get message from successful body (case status: "error")
        val body = response.body()
        if (body is BaseResponse && body.message != null) {
            return body.message
        }
        
        // 2. Try to get message from error body (JSON error)
        try {
            val errorBodyString = response.errorBody()?.string()
            if (errorBodyString != null) {
                if (errorBodyString.trim().startsWith("<!doctype html", ignoreCase = true) || 
                    errorBodyString.trim().startsWith("<html", ignoreCase = true)) {
                    return "Server error (HTML response received). Please contact support."
                }
                
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBodyString, BaseResponse::class.java)
                return errorResponse.message ?: "An unexpected error occurred."
            }
        } catch (e: Exception) {
            // Parsing failed
        }
        
        return "Network communication error. Please try again."
    }

    fun getFailureMessage(t: Throwable): String {
        val msg = t.message ?: ""
        return when {
            msg.contains("failed to connect", ignoreCase = true) -> "Cannot reach server. Ensure both devices are on the same Wi-Fi/Hotspot."
            msg.contains("timeout", ignoreCase = true) -> "Server response timed out. Please try again later."
            msg.contains("host", ignoreCase = true) -> "Unable to resolve server address. Check your internet connection."
            else -> "Connection error: ${t.localizedMessage ?: "Unknown error"}"
        }
    }
}
