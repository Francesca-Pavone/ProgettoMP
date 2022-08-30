package com.francescapavone.menuapp.view.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.control.db.RestaurantEntity
import com.francescapavone.menuapp.model.RestaurantPreview
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow
import com.francescapavone.menuapp.control.ScreenRouter
import com.francescapavone.menuapp.control.HomeScreenControl

@Composable
fun RestaurantCard(
    restaurantPreview: RestaurantPreview,
    deliveryPrice: MutableState<Double>,
    portrait: Boolean,
    translationValue: Float,
    selectedRestaurantId: MutableState<String>,
    homeScreenControl: HomeScreenControl
) {

    /* clicked on 'more info' */
    val moreInfo = rememberSaveable { mutableStateOf(false) }

    /* to manage favourite list */
    val allFav by homeScreenControl.allFavourite.observeAsState(listOf())  /* the list of all favourites restaurants */
    val idList = mutableListOf<String>() /* to store id of favourite restaurants */

    val isFav =
        rememberSaveable { mutableStateOf(true) } /* true if the restaurant in the card is in the favourite list */

    /* add the ids of the favourite restaurants to idList */
    for (i in allFav) {
        idList.add(i.id)
    }

    isFav.value = restaurantPreview.id in idList /* to assign isFav value, based on idList*/

    var rotated by rememberSaveable { mutableStateOf(false) }

    val angle by animateFloatAsState(
        targetValue = if (rotated) 360f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    Card(
        modifier = Modifier
            .padding(end = 20.dp)
            .width(if (portrait) 200.dp else 180.dp)
            .wrapContentHeight(Alignment.CenterVertically)
            .graphicsLayer {
                translationY = translationValue
            },
        shape = RoundedCornerShape(14.dp),
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            /* use picasso library to load image from url */
            NetworkImageComponentPicasso(
                url = restaurantPreview.poster,
                modifier = Modifier
                    .clip(RoundedCornerShape(25.dp))
                    .rotate(angle)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        /* to get the menu of the clicked restaurant */
                        homeScreenControl.viewMenu(
                            restaurantPreview,
                            deliveryPrice,
                            selectedRestaurantId
                        )
                        /* navigate to menu page */
                        ScreenRouter.navigateTo(1, 2)
                    },
                size = if (portrait) 190 else 130
            )

            Column(
                modifier = Modifier
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = restaurantPreview.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = restaurantPreview.type,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                if (!moreInfo.value && !portrait) {
                    Text(
                        text = "More info",
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { moreInfo.value = true }
                            .padding(top = 5.dp),
                        fontSize = 14.sp,
                        color = myGreen
                    )
                } else {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = restaurantPreview.price,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = restaurantPreview.address,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = restaurantPreview.city,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = restaurantPreview.phone,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Text(
                        text = "Delivery: € ${restaurantPreview.delivery}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    if (!portrait)
                        Text(
                            text = "Less info",
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .clickable { moreInfo.value = false }
                                .padding(top = 5.dp),
                            fontSize = 16.sp,
                            color = myGreen
                        )
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .wrapContentHeight()
            ) {
                IconButton(
                    onClick = {

                         rotated = !rotated

                        val restaurant = RestaurantEntity()
                        restaurant.id = restaurantPreview.id

                        if (!isFav.value) {
                            /* here if restaurant is not in the favourite list*/
                            homeScreenControl.updateFavoriteOn(restaurant) /* add restaurant to favourites */
                        } else {
                            /* here if restaurant is  in the favourite list*/
                            homeScreenControl.updateFavoriteOff(restaurant) /* remove restaurant from favourites */
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .wrapContentWidth(Alignment.Start),
                ) {
                    Icon(
                        painter = painterResource(id = if (!isFav.value) R.drawable.fav_line else R.drawable.fav_fill),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp),
                        tint = myYellow
                    )
                }

                Spacer(Modifier.weight(1f, true))

                FloatingActionButton(
                    onClick = {

                        /* to get the menu of the clicked restaurant */
                        homeScreenControl.viewMenu(
                            restaurantPreview,
                            deliveryPrice,
                            selectedRestaurantId,
                        )

                        /* navigate to menu page */
                        ScreenRouter.navigateTo(1, 2)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .wrapContentWidth(Alignment.End),
                    backgroundColor = myYellow,
                    contentColor = myGreen,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(3.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.eye2),
                        tint = myGreen,
                        contentDescription = "eye",
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}




