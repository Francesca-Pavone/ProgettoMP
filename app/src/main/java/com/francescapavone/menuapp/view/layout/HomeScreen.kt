package com.francescapavone.menuapp.view.layout

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.model.RestaurantPreview
import com.francescapavone.menuapp.view.components.RestaurantCard
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow
import com.francescapavone.menuapp.control.ScreenRouter
import com.francescapavone.menuapp.control.HomeScreenControl

@Composable
fun HomePage(
    previewList: SnapshotStateList<RestaurantPreview>,
    deliveryPrice: MutableState<Double>,
    selectedRestaurantId: MutableState<String>,
    homeScreenControl: HomeScreenControl
){

    /* to manage search bar */
    val onSearch = remember { mutableStateOf(false) } /* true if user clicked search */
    val searchBarText = rememberSaveable { mutableStateOf("") } /* user input in search bar*/

    /* to manage favourite list */
    val allFav by homeScreenControl.allFavourite.observeAsState(listOf()) /* the list of all favourites restaurants */
    val onFav = rememberSaveable { mutableStateOf(false) } /* true if users clicked  fav list */
    val idList = rememberSaveable {mutableListOf<String>()} /* to store id of favourite restaurants */

    /* to store first item of the list, for graphic purpose */
    val listState = rememberLazyListState()
    val firstItem by remember { derivedStateOf { listState.firstVisibleItemIndex } } /* to store first item of the list, for graphic purpose */
    var translationValue: Int

    /* to manage different phone orientation */
    val conf = LocalConfiguration.current
    val portrait = when (conf.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            false
        }
        else -> {
            true
        }
    }
    homeScreenControl.getAllPreviews(previewList) /* to get the full list of available restaurants */

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.bg_app_dark else R.drawable.bg_app),
        contentDescription = "bg",
        contentScale = ContentScale.FillBounds
    )
    Scaffold(
        backgroundColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { ScreenRouter.navigateTo(1, 3) },
                modifier = Modifier.size(64.dp) ,
                backgroundColor = myYellow,
                contentColor = myGreen,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(5.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.shop_bag), contentDescription = "shoppingCart")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = { HomeBottomBar(searchBarText, onSearch, onFav, portrait) },
        topBar = { if (portrait) HomeTopBar(searchBarText) },
    ) {paddingValues ->


        /* filter the list with the user input */
        val filteredList: List<RestaurantPreview> = previewList.filter { res ->
            (res.name.uppercase().startsWith(searchBarText.value.uppercase()))
        }

        /* add the ids of the favourite restaurants to idList */
        for (i in allFav) {
            idList.add(i.id)
        }

        /* check if the two lists are consistent with each other*/
        if(idList.size!=allFav.size){
            idList.clear()
            for (i in allFav) {
                idList.add(i.id)
            }
        }

        /* list of RestaurantsPreview favourite restaurants, based on idList */
        val favList: List<RestaurantPreview> = previewList.filter { res ->
            (res.id in idList)
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
        ) {
            LazyRow(
                contentPadding = PaddingValues(
                    start = if((onFav.value and (favList.size==1)) or (filteredList.size==1)) 100.dp else 20.dp,
                    end = 20.dp,
                    top = if (portrait) 50.dp else 0.dp
                ),
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .align(if (portrait) Alignment.TopCenter else Alignment.Center),
                state = listState
            ) {
                items(
                    items = if (!onFav.value) filteredList else favList,
                    itemContent = { res ->
                        translationValue =
                            if (!portrait)
                                0
                            else {
                                if (!onFav.value && (res.id == filteredList[firstItem].id || (firstItem+2 < filteredList.size && res.id == filteredList[firstItem+2].id)))
                                    -80
                                else if ((firstItem < favList.size && onFav.value && (res.id == favList[firstItem].id) || (firstItem+2 < favList.size && res.id == favList[firstItem+2].id)))
                                    -80
                                else
                                    0
                            }

                        RestaurantCard(
                            restaurantPreview = res,
                            deliveryPrice = deliveryPrice,
                            portrait = portrait,
                            translationValue = translationValue.toFloat(),
                            selectedRestaurantId,
                            homeScreenControl
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HomeTopBar(restaurant: MutableState<String>) {
    Box(modifier = Modifier
        .height(110.dp)
    ){
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.bg_topbar_dark else R.drawable.bg_topbar),
            contentDescription = "TopBar Background",
            contentScale = ContentScale.FillBounds
        )
        Row(
            Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .align(Alignment.TopCenter)
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextField(
                value = restaurant.value,
                onValueChange = {restaurant.value = it},
                placeholder = { Text(text = stringResource(R.string.search), fontSize = 16.sp) },
                textStyle = TextStyle(lineHeight = 70.sp),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search"
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = myGreen,
                    textColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            )
        }
    }
}

@Composable
fun HomeBottomBar(restaurant: MutableState<String>, searching: MutableState<Boolean>, onFav: MutableState<Boolean>, portrait: Boolean) {

    BottomAppBar(
        elevation = AppBarDefaults.BottomAppBarElevation,
        cutoutShape = CircleShape,
        backgroundColor = MaterialTheme.colors.primary,
        contentPadding = AppBarDefaults.ContentPadding
    ) {
        IconButton(
            onClick = {
                onFav.value = !onFav.value
            }
        ) {
            Icon(
                painter = painterResource(id = if(!onFav.value) R.drawable.ic_baseline_favorite_24 else R.drawable.arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        }
        if (!portrait) {
            IconButton(
                onClick = { ScreenRouter.navigateTo(1, 4) },
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.qr_code_scanner),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.padding(3.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f, true))

        if (searching.value) {
            TextField(
                value = restaurant.value,
                onValueChange = { restaurant.value = it },
                modifier = Modifier.height(50.dp),
                placeholder = { Text(text = stringResource(R.string.search)) },
                textStyle = TextStyle(lineHeight = 70.sp),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colors.secondary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable { searching.value = false })
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = myGreen,
                    textColor = Color.Gray
                )
            )
        } else {
            if (portrait) {
                IconButton(onClick = { ScreenRouter.navigateTo(1, 4) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier.padding(3.dp)
                    )
                }

            } else {
                IconButton(onClick = { searching.value = !searching.value }) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colors.secondary
                    )
                }
            }
        }
    }
}




