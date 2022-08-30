package com.francescapavone.menuapp.control.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RestaurantEntity {
    @PrimaryKey(autoGenerate = false)
    var id: String = ""
}