package com.francescapavone.menuapp.control
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.control.api.RestaurantApi
import com.francescapavone.menuapp.control.db.DBFavourites
import com.francescapavone.menuapp.control.db.RestaurantEntity
import com.francescapavone.menuapp.control.db.RepositoryRestaurants
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.model.RestaurantPreview
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

class HomeScreenControl(
    var starters: SnapshotStateList<Course>,
    var firstCourses: SnapshotStateList<Course>,
    var secondCourses: SnapshotStateList<Course>,
    var sides: SnapshotStateList<Course>,
    var fruits: SnapshotStateList<Course>,
    var desserts: SnapshotStateList<Course>,
    var drinks: SnapshotStateList<Course>,
    var context: Context
) {
    var allFavourite: LiveData<List<RestaurantEntity>> /* the list of favourite restaurants*/
    private val rep: RepositoryRestaurants

    init{
        val db = DBFavourites.getInstance(context as Application)
        val dao = db.favouritesDAO()
        rep = RepositoryRestaurants(dao)

        allFavourite = rep.allFavourite
    }

    /* add restaurant to favourite */
    fun updateFavoriteOn(restaurantEntity: RestaurantEntity){
        rep.changeOnFavorite(restaurantEntity)
    }

    /* remove restaurant from favourite */
    fun updateFavoriteOff(restaurantEntity: RestaurantEntity){
        rep.changeOffFavorite(restaurantEntity)
    }

    /* to get all restaurant previews */
    fun getAllPreviews(list: MutableList<RestaurantPreview>){
        val s = RestaurantApi(context)
        s.listAllPreviews(
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("Restaurants")
                    val sType = object : TypeToken<List<RestaurantPreview>>() {}.type
                    val gson = Gson()
                    val l = gson.fromJson<List<RestaurantPreview>>(ja.toString(), sType)
                    println(l)
                    list.clear()
                    list.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
            },
            {
                Log.v("JSON", "Request Restaurant Previews")
            }
        )
    }

    /* to view menu, based on id*/
    fun viewMenu(
        restaurantPreview: RestaurantPreview,
        deliveryPrice: MutableState<Double>,
        selectedRestaurantId: MutableState<String>
    ){
        val s = RestaurantApi(context)

        selectedRestaurantId.value = restaurantPreview.id
        deliveryPrice.value = restaurantPreview.delivery.toDouble()

        /* get all starters */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("starters")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    starters.clear()
                    starters.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }

                /* set restaurant id*/
                for (i in starters) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading starters")
            }
        )
        /* get all first courses */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("firstCourses")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    firstCourses.clear()
                    firstCourses.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in firstCourses) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading first courses")
            }
        )

        /* get all second courses */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("secondCourses")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    secondCourses.clear()
                    secondCourses.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in secondCourses) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading second courses")
            }
        )

        /* get all sides*/
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("sides")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    sides.clear()
                    sides.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in sides) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading sides")
            }
        )

        /* get all fruits */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("fruits")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    fruits.clear()
                    fruits.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in fruits) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading fruits")
            }
        )

        /* get all desserts */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("desserts")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    desserts.clear()
                    desserts.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in desserts) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading desserts")
            }
        )

        /* get all drinks */
        s.getMenu(
            restaurantPreview.id,
            {
                val jo = JSONObject(it)
                try {
                    val ja = jo.getJSONArray("drinks")
                    val sType = object : TypeToken<List<Course>>() {}.type

                    val gson = Gson()
                    val l = gson.fromJson<List<Course>>(ja.toString(), sType)
                    println(l)
                    drinks.clear()
                    drinks.addAll(l)
                } catch (e: JSONException) {
                    Toast.makeText(context, context.getString(R.string.NF), Toast.LENGTH_SHORT).show()
                }
                /* set restaurant id to courses*/
                for (i in drinks) {
                    i.restaurantId = restaurantPreview.id
                }
            },
            {
                Log.v("JSON", "Error on loading drinks")
            }
        )
    }

}