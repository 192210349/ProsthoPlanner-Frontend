package com.simats.prosthoplanner.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("api/upload-imaging")
    fun uploadImaging(
        @Part file: MultipartBody.Part,
        @Part("patient_db_id") patientId: RequestBody,
        @Part("image_type") imageType: RequestBody
    ): Call<UploadResponse>

    @POST("api/suggest-treatment")
    fun suggestTreatment(@Body request: SuggestionRequest): Call<SuggestionResponse>

    @POST("api/register-patient")
    fun registerPatient(@Body request: Map<String, @JvmSuppressWildcards Any>): Call<RegistrationResponse>

    @POST("api/select-plan")
    fun selectPlan(@Body request: SelectPlanRequest): Call<BaseResponse>

    @POST("api/forgot-password")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<BaseResponse>

    @POST("api/reset-password")
    fun resetPassword(@Body request: ResetPasswordRequest): Call<BaseResponse>

    @POST("api/resend-otp")
    fun resendOtp(@Body request: ResendOtpRequest): Call<BaseResponse>

    @POST("api/signup")
    fun signup(@Body request: SignupRequest): Call<BaseResponse>

    @GET("api/patients")
    fun getPatients(): Call<PatientListResponse>

    @POST("api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/verify-otp")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<BaseResponse>
}

data class BaseResponse(val status: String, val message: String?)
data class UploadResponse(val status: String, val vision_insights: Map<String, Any>?)
data class RegistrationResponse(val status: String, val patient_db_id: Int)
data class SuggestionRequest(val patient_db_id: Int, val age: Int, val gender: String, val kennedy_classification: String, val tissue_condition: String, val occlusion_type: String, val is_diabetic: Int = 0, val is_smoker: Int = 0)
data class SuggestionResponse(val status: String, val patient_db_id: Int, val plans: Map<String, Plan>)
data class Plan(val treatment: String, val cost: Double, val time: String, val vision_insight: String?)
data class SelectPlanRequest(val patient_db_id: Int, val selection: String)
data class PatientListResponse(val status: String, val patients: List<PatientDB>)
data class PatientDB(val id: Int, val patient_external_id: String, val full_name: String, val gender: String, val age: Int, val selected_plan: String?, val generated_at: String?)
data class SignupRequest(val full_name: String, val email: String, val mobile_number: String, val password: String, val confirm_password: String)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val user: User?, val message: String?, val requires_otp: Boolean?)
data class User(val id: Int, val full_name: String, val email: String)
data class VerifyOtpRequest(val email: String, val otp: String)
data class ForgotPasswordRequest(val email: String)
data class ResetPasswordRequest(val email: String, val otp: String, val new_password: String)
data class ResendOtpRequest(val email: String)
