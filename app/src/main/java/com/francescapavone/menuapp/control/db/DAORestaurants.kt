package com.francescapavone.menuapp.control.db
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAORestaurants {
    @Delete
    fun removeOne(restaurantEntity: RestaurantEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOne(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM RestaurantEntity ORDER BY id")
    fun getAll():LiveData<List<RestaurantEntity>>


}
