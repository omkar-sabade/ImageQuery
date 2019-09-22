package com.omkar.imagequery.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.omkar.imagequery.db.Item

class SearchResponse {

    @SerializedName("queries")
    @Expose
    var queries: Queries? = null
    @Expose
    var items: List<Item>? = null

}
