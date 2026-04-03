package com.simats.prosthoplanner.utils

import java.util.regex.Pattern

object ValidationUtils {

    /**
     * Validates password based on:
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one special character (@#$%^&+=!)
     * - At least one number
     * - Minimum 8 characters
     */
    fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun getPasswordErrorMessage(): String {
        return "Password must contain at least one uppercase, one lowercase, one number, one special character, and be at least 8 characters long."
    }

    /**
     * Validates phone number based on:
     * - Exactly 10 digits
     * - Only numeric characters
     */
    fun validatePhone(phone: String): Pair<Boolean, String?> {
        if (phone.isEmpty()) {
            return Pair(false, "Phone number cannot be empty")
        }
        if (!phone.all { it.isDigit() }) {
            return Pair(false, "it contains only numbers")
        }
        if (phone.length != 10) {
            return Pair(false, "phone number must have 10 numbers")
        }
        return Pair(true, null)
    }

    /**
     * Validates name based on:
     * - Only alphabets and spaces
     */
    fun validateName(name: String): Pair<Boolean, String?> {
        if (name.isEmpty()) {
            return Pair(false, "Name is required")
        }
        if (!name.all { it.isLetter() || it.isWhitespace() }) {
            return Pair(false, "Name must contain only alphabets")
        }
        return Pair(true, null)
    }
}
