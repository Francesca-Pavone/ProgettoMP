package com.francescapavone.menuapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestaurantPreview(
    var id: String,
    var type: String,
    val poster: String,
    val name: String,
    var price: String,
    var address: String,
    var city: String,
    var phone: String,
    var delivery: String
) : Parcelable {
    constructor() : this("", "", "","","","","","", "")
}