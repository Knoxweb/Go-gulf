package com.gogulf.passenger.app.data.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FAQModel(
    @SerializedName("faq_category_id")
    val faqCategoryID: String,

    val name: String,
    val faqs: ArrayList<FAQ>
) : Serializable

data class FAQ(
    @SerializedName("faq_id")
    val faqID: String,

    val title: String,
    val description: String
) : Serializable