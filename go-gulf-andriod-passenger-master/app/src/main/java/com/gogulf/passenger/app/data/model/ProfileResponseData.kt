package com.gogulf.passenger.app.data.model

data class ProfileResponseData(
    val billing_address: String? = null,
    val billing_company_name: String? = null,
    val billing_country: String? = null,
    val billing_first_name: String? = null,
    val billing_last_name: String? = null,
    val email: String? = null,
    val first_name: String? = null,
    val language: String? = null,
    val last_name: String? = null,
    val mobile: String? = null,
    val name: String? = null,
    val passenger_type: String? = null,
    val pickup_sign: String? = null,
    var profile_picture_url: String? = null,
    val profile_status: String? = null,
    val ratings: Any? = null
) {
    fun getRatingFloat(): Float {
        // Handle null or non-numeric `ratings` safely
        return when (ratings) {
            is Float -> ratings as Float  // If already a Float, return it directly
            is Double -> (ratings as Double).toFloat()  // Convert Double to Float
            is Int -> (ratings as Int).toFloat()  // Convert Int to Float
            is String -> {
                // Try parsing the String to a Float, default to 0.0f if parsing fails
                ratings?.toFloatOrNull() ?: 0.0f
            }
            else -> 0.0f  // Default value if ratings is null or of an unsupported type
        }
    }
}


