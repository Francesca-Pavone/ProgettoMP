package com.francescapavone.menuapp.control.db
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepositoryRestaurants (private val daoRestaurants: DAORestaurants){
    var allFavourite: LiveData<List<RestaurantEntity>> = daoRestaurants.getAll()

    /* to insert a restaurant to favourite list*/
    fun changeOnFavorite(restaurantEntity: RestaurantEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            daoRestaurants.insertOne(restaurantEntity)
        }
    }

    /* to remove a restaurant from favourite list*/
    fun changeOffFavorite(restaurantEntity: RestaurantEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            daoRestaurants.removeOne(restaurantEntity)
        }
    }
}