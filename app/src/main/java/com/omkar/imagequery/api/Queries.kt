package com.omkar.imagequery.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Queries {
    @SerializedName("nextPage")
    @Expose
    var nextPage: List<NextPage>? = null
}
