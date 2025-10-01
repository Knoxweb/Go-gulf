package com.gogulf.passenger.app.data.apidata

object UrlName {
    const val firebaseOtpCheck = "firebase-otp-check"
    const val get_status = "status"

    /** Dashboard api */
    const val get_dashboard = "passenger/dashboard"

    /** Authentication api */
    const val post_sendVerification = "passenger/verification"
    const val post_authentication = "passenger/authentication"
    const val post_CheckSession = "passenger/session-check"
    const val post_register = "passenger/register"
    const val post_reSendVerification = "passenger/resend-verification"
    const val post_verifyCode = "passenger/verify-code"
    const val get_logout = "passenger/logout"
    const val post_deviceToken = "passenger/device-token" //device_type, device_token

    /** Accept terms api */
    const val post_acceptTerms = "passenger/accept-terms"

    /** Profile api */
    const val post_updateProfile = "passenger/update-profile"
    const val get_profile = "passenger/profile"

    /** Card api */
    const val post_addCard = "passenger/add-card"
    const val post_updateCard = "passenger/update-card"
    const val get_card = "passenger/card"
    const val post_deleteCard = "passenger/delete-card"

    /** Get quote and request job api */
    const val post_flightStatus = "quote/flight-status"
    const val post_quote = "quote"
    const val post_requestJob = "quote/request-job"
    const val post_re_requestJob = "quote/re-request-job"
    const val post_cancelRequestedJob = "quote/cancel-requested-job"


    /** Get Current Ride api */
    const val get_currentRide = "quote/current-ride"
    const val post_dodCancelBooking = "passenger/dod-cancel-booking"
    const val post_rating = "passenger/rating"
    const val post_nearByDriver = "passenger/near-by-driver"

    /** Booking History & Schedule api */
    const val get_bookings = "passenger/bookings?type=" //bookings?type=completed
    const val get_schedule = "passenger/schedule?type=" ///schedule?type=confirmed
    const val get_bookingDetail = "passenger/booking-detail?booking_id="
    const val post_bookingCancel = "passenger/booking-cancel"

    /** Get Invoice api */
    const val get_invoice = "passenger/invoice"

    /** Get Notification api */
    const val get_notification = "passenger/notification"
    const val post_readNotification = "passenger/read-notification"
    const val post_makePayment = "passenger/make-payment"

    /** Get Support api */
    const val post_support = "passenger/support"
    const val post_accountDelete = "passenger/account-delete"
    const val post_downloadInvoice = "passenger/invoice/download"
    const val post_capturePayment = "passenger/capture-payment"


    /** Rohan Paudel */
    /** Register API */
    const val REGISTER = "register"
    const val EMAIL_LOGIN = "email-login"
    const val LOGIN = "login"
    const val FORGET_PASSWORD = "forget-password"
    const val FORGET_PASSWORD_OTP_CHECK = "forget-password-opt-check"
    const val FORGET_PASSWORD_RESET = "forget-password-reset"
    const val STATUS = "status"
    const val UPDATE = "update"
    const val ADD_CARD = "add-card"
    const val ACTIVATE_CARD = "activate-card"
    const val DELETE_CARD = "delete-card"
    const val SUPPORT = "support"
    const val FIRST_UPDATE = "first-update"
    const val LOGOUT = "logout"

    const val DELETE_ACCOUNT = "delete-account"
    const val CALCULATE_FARE = "calculate-fare"
    const val ADD_ADDRESS = "add-address"
    const val EDIT_ADDRESS = "edit-address"
    const val DELETE_ADDRESS = "delete-address"

    const val CONFIRM_BOOKING = "confirm-booking"
    const val RETRY_BOOKING = "retry-booking"
    const val CANCEL_BOOKING = "cancel-booking-request"
    const val CANCEL_BOOKING_REQUEST = "cancel-booking"

    const val BOOKING_REVIEW = "booking-review"
    const val BOOKING_SKIP_REVIEW = "booking-skip-review"


    const val INVOICE_PDF = "invoice-pdf"


}
