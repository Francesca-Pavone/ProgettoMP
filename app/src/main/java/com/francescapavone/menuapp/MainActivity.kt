package com.francescapavone.menuapp

import android.app.DownloadManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.model.RestaurantPreview
import com.francescapavone.menuapp.view.layout.Cart
import com.francescapavone.menuapp.view.layout.HomePage
import com.francescapavone.menuapp.view.layout.Menu
import com.francescapavone.menuapp.view.layout.QrCodeScanner
import com.francescapavone.menuapp.view.theme.MenuAppTheme
import com.francescapavone.menuapp.view.components.SaverForMutable.Companion.rememberMutableStateListOf
import com.francescapavone.menuapp.control.ScreenRouter
import com.francescapavone.menuapp.control.CartControl
import com.francescapavone.menuapp.control.HomeScreenControl
import com.francescapavone.menuapp.control.MenuScreenControl
import com.francescapavone.menuapp.control.QrControl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuAppTheme {

                val context = LocalContext.current.applicationContext /* actual application context */

                /* Restaurant List */
                val previewList = rememberMutableStateListOf<RestaurantPreview>()  /*  to display all restaurant previews */

                /* Menu */
                val starters = rememberMutableStateListOf<Course>()  /*  to display starters */
                val firstCourses = rememberMutableStateListOf<Course>() /*  to display firstCourses */
                val secondCourses = rememberMutableStateListOf<Course>() /*  to display secondCourses */
                val sides = rememberMutableStateListOf<Course>() /*  to display sides */
                val fruits = rememberMutableStateListOf<Course>() /*  to display fruits */
                val desserts = rememberMutableStateListOf<Course>()  /*  to display desserts */
                val drinks = rememberMutableStateListOf<Course>()  /*  to display drinks */

                /* Order */
                val subtotal = rememberSaveable { mutableStateOf(0.0) } /* for order subtotal */
                val deliveryPrice = rememberSaveable { mutableStateOf(0.0) } /* for restaurant delivery price*/
                val orderList = rememberSaveable { mutableListOf<Course>() } /* for all the courses in the order */
                val actualCartRestaurantId = rememberSaveable { mutableStateOf(-1) } /* the id of the restaurant whose products are in the cart*/

                val selectedRestaurantId = rememberSaveable { mutableStateOf("") } /* the id of the clicked restaurant */

                val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager /* for download*/

                /* control for home page */
                val homeScreenControl = HomeScreenControl(
                    starters,
                    firstCourses,
                    secondCourses,
                    sides,
                    fruits,
                    desserts,
                    drinks,
                    context
                )

                /* control for menu page */
                val menuScreenControl = MenuScreenControl(context, dm)

                /* control for cart page */
                val cartControl = CartControl()

                /* control for qr page */
                val qrControl=QrControl(context,dm)

                when (ScreenRouter.currentScreen.value) {
                    1 -> HomePage(
                        previewList,
                        deliveryPrice,
                        selectedRestaurantId,
                        homeScreenControl
                    )
                    2 -> Menu(
                        starters,
                        firstCourses,
                        secondCourses,
                        sides,
                        fruits,
                        desserts,
                        drinks,
                        subtotal,
                        orderList,
                        selectedRestaurantId,
                        actualCartRestaurantId,
                        menuScreenControl
                    )
                    3 -> Cart(
                        subtotal,
                        deliveryPrice,
                        orderList,
                        actualCartRestaurantId,
                        cartControl
                    )
                    4 -> QrCodeScanner(
                        qrControl
                    )
                }
            }
        }
    }
}