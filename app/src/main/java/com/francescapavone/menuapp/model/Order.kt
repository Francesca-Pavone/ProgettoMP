package com.francescapavone.menuapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var restaurantId: String,
    var deliveryAddress: String,
    var referentName: String,
    var deliveryTime: String,
    var deliveryDate: String,
    var total: String,
    var courses: List<Course>
): Parcelable {
    constructor() : this("", "", "","", "","", emptyList<Course>())
}