package com.francescapavone.menuapp.control.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RestaurantEntity::class],version = 1)
abstract class DBFavourites: RoomDatabase() {
    companion object{
        private var db: DBFavourites? = null
        fun getInstance(context : Context): DBFavourites {
            val dbName = DBName()
            if(db == null)
                db = Room.databaseBuilder(context.applicationContext,
                    DBFavourites::class.java,dbName.DBNAME)
                    .createFromAsset(dbName.DBNAME).build()
            return db as DBFavourites
        }

    }
    abstract fun favouritesDAO():DAORestaurants
}