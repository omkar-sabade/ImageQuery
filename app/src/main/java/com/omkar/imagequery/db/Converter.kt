package com.omkar.imagequery.db

import androidx.room.TypeConverter


/**
 * Converter for room.
 * Here image is not an actual image it's an object of class "image".
 * Unfortunate naming but that is what's found in the SearchResponse so wanted to keep it that way.
 */

class Converter {
    @TypeConverter
    fun imageToString(image: Image): String = image.toString()

    @TypeConverter
    fun stringToImage(imageString: String): Image {
        val string = imageString.split(",")
        return Image(string[0], string[1], string[2], string[3], string[4], string[5], string[6])
    }

}