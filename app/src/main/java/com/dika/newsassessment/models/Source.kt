package com.dika.newsassessment.models

import com.google.gson.annotations.SerializedName

class Source(@field:SerializedName("name") val name: String) {
    @SerializedName("id")
    val id: String? = null

}