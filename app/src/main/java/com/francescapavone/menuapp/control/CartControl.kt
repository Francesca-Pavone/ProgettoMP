package com.francescapavone.menuapp.control

import androidx.compose.runtime.MutableState
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.model.Order
import com.google.gson.GsonBuilder

class CartControl {
    fun sendOrder(
        orderList: MutableList<Course>,
        deliveryAddress: String,
        referentName: String,
        subtotal: MutableState<Double>,
        deliveryPrice: Double,
        deliveryDate: String,
        deliveryTime: String
    ) {
        /* create order and assign values */
        val myOrder = Order()
        myOrder.deliveryAddress = deliveryAddress
        myOrder.referentName = referentName
        myOrder.total = "€ ${String.format("%.2f", subtotal.value + deliveryPrice)}"
        myOrder.courses = orderList
        myOrder.restaurantId = orderList[0].restaurantId
        myOrder.deliveryTime = deliveryTime
        myOrder.deliveryDate = deliveryDate

        /* create the corresponding json */
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val resultJson: String = gsonPretty.toJson(myOrder)

        /* simulate send result to server API*/
        println(resultJson)
        println("******* SEND ORDER TO SERVER *******")
    }
}