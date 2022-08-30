package com.francescapavone.menuapp.control.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest

class RestaurantApi(private var context: Context) {
    fun getMenu(
        id: String,
        onSuccess: (String) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val volley = VolleySingleton.getInstance()
        val url =  "https://raw.githubusercontent.com/al3ssandrocaruso/restaurantsappdata/main/menus/$id"  /* create the right url for the id */
        val stringRequest = StringRequest(   /* create new string request */
            Request.Method.GET,
            url,
            {
                onSuccess(it) /* success */
            },
            {
                onError(it) /* failure */
            })
        volley.getRequestQueue(context)?.add(stringRequest)
    }

    fun listAllPreviews(
        onSuccess: (String) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val volley = VolleySingleton.getInstance()
        val url = "https://raw.githubusercontent.com/al3ssandrocaruso/restaurantsappdata/main/restaurants/allpreviews"  /*  url for all previews */
        val stringRequest = StringRequest( /* create new string request */
            Request.Method.GET,
            url,
            {
                onSuccess(it)  /* success */
            },
            {
                onError(it) /* failure */
            })
        volley.getRequestQueue(context)?.add(stringRequest)
    }

}