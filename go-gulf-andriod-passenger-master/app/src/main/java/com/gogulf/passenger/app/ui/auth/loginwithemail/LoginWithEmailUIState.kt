package  com.gogulf.passenger.app.ui.auth.loginwithemail

import com.gogulf.passenger.app.data.model.Error
import com.gogulf.passenger.app.data.model.auth.LoginWithEmailResponseData
import com.gogulf.passenger.app.data.model.auth.PassengerLoginResponseData

data class LoginWithEmailUIState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: Error? = null,
    val isLoginSuccess: Boolean = false,
    val isFirebaseLoginSuccess: Boolean = false,
    val isNumberLoginSuccess: Boolean = false,
    val loginWithEmailResponseData: LoginWithEmailResponseData? = null,
    val passengerLoginResponseData: PassengerLoginResponseData? = null
)