package com.francescapavone.menuapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val name: String,
    var price: String,
    var poster: String,
    var description: String,
    var restaurantId: String,
    var count: Int = 0
): Parcelable {
    constructor() : this("", "", "","", "")
}
