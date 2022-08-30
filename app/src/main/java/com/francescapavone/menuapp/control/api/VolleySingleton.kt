package com.francescapavone.menuapp.control.api

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


/* to have a unique queue in all the application  */
class VolleySingleton {
    private var mRequestQueue: RequestQueue? = null

    companion object {
        private var mInstance: VolleySingleton? = null

        @Synchronized
        fun getInstance(): VolleySingleton {
            if (mInstance == null) {
                mInstance = VolleySingleton()
            }
            return mInstance!!
        }
    }

    fun getRequestQueue(context: Context): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context)
        }
        return mRequestQueue
    }
}