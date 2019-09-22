package com.omkar.imagequery.db

import android.view.View
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items")
class Item(
    val title: String,
    val snippet: String,
    @SerializedName("link")
    val pageLink: String,
    @SerializedName("image")
    @Expose
    val image: Image,
    val displayLink: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString() = title
    fun isValid() = title.isNotEmpty()
            && snippet.isNotEmpty()
            && pageLink.isNotEmpty()
            && image.isValid()
            && displayLink.isNotEmpty()
}

class Image(
    val height: String,
    val width: String,
    val byteSize: String,
    val thumbnailLink: String,
    private val thumbnailHeight: String,
    private val thumbnailWidth: String,
    val contextLink: String
) {
    override fun toString(): String =
        "$height,$width,$byteSize,$thumbnailLink,$thumbnailHeight,$thumbnailWidth,$contextLink"


    fun imageSize() = "$width X $height"

    fun isValid() = height.isEmpty()
        .or(width.isEmpty())
        .or(byteSize.isEmpty())
        .or(thumbnailLink.isEmpty())
        .or(thumbnailHeight.isEmpty())
        .or(thumbnailWidth.isEmpty()).not()

    fun visibility() = when (isValid()) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}