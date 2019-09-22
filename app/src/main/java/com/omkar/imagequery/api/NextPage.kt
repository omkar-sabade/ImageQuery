package com.omkar.imagequery.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NextPage {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("startIndex")
    @Expose
    var startIndex: Int? = null
}