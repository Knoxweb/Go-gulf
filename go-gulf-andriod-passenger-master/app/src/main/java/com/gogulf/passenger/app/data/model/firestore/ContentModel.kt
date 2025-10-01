package com.gogulf.passenger.app.data.model.firestore

import java.io.Serializable

data class ContentModel(
    val title: String? = "",
    val description: String? = "",
    val name: String? = "",
    val user: String? = ""
) : Serializable
