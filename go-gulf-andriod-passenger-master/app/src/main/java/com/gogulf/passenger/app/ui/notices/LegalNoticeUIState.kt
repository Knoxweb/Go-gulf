package com.gogulf.passenger.app.ui.notices

import com.gogulf.passenger.app.data.model.PoliciesResponseData

data class LegalNoticeUIState(
    val legalNotice: PoliciesResponseData? = null,
    val isLoading: Boolean = false
)
