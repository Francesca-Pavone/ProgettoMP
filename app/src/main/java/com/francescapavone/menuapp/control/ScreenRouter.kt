package com.francescapavone.menuapp.control

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

object ScreenRouter {
    var currentScreen: MutableState<Int> = mutableStateOf(1)
    var previousScreen: MutableState<Int> = mutableStateOf(1)


    fun navigateTo(source: Int = currentScreen.value, destination: Int) {
        previousScreen.value = source
        currentScreen.value = destination
    }
}