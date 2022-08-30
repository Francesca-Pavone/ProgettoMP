package com.francescapavone.menuapp.view.layout

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.view.components.CourseCard
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow
import com.francescapavone.menuapp.control.ScreenRouter
import com.francescapavone.menuapp.control.MenuScreenControl


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Menu(
    starters: SnapshotStateList<Course>,
    firstCourses: SnapshotStateList<Course>,
    secondCourses: SnapshotStateList<Course>,
    sides: SnapshotStateList<Course>,
    fruits: SnapshotStateList<Course>,
    desserts: SnapshotStateList<Course>,
    drinks: SnapshotStateList<Course>,
    subtotal: MutableState<Double>,
    orderList: MutableList<Course>,
    selectedRestaurantId: MutableState<String>,
    cartRestaurantId: MutableState<Int>,
    menuScreenControl: MenuScreenControl
) {
    val scaffoldState = rememberScaffoldState()


    /* Storage Permission management */
    var hasStoragePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                menuScreenControl.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcherStorage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasStoragePermission = granted
            if(hasStoragePermission) {menuScreenControl.downloadFile(selectedRestaurantId.value)}
        }
    )

    val qrDialog = rememberSaveable { mutableStateOf(false) } /* true if the qr dialog is opened */

    if (qrDialog.value) {
        val qr: Bitmap? =
            menuScreenControl.generateQRCode(selectedRestaurantId.value) /* generate the qr code, based on restaurant's id */
        Dialog(onDismissRequest = { qrDialog.value = false }) {
            Image(
                bitmap = qr!!.asImageBitmap(),
                contentDescription = "some useful description",
            )
        }
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.bg_app_dark else R.drawable.bg_app),
        contentDescription = "bg",
        contentScale = ContentScale.FillBounds
    )
    Scaffold(
        modifier = Modifier.blur(if (qrDialog.value) 20.dp else 0.dp),
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { ScreenRouter.navigateTo(2, 3) },
                modifier = Modifier.size(64.dp),
                backgroundColor = myYellow,
                contentColor = myGreen,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(5.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.shop_bag),
                    contentDescription = "shoppingCart"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { qrDialog.value = true },
                    modifier = Modifier
                        .padding(end = 50.dp)
                        .align(Alignment.CenterVertically)
                        .size(50.dp)
                        .background(color = myYellow, shape = RoundedCornerShape(50))
                )
                {
                    Icon(
                        modifier = Modifier.padding(15.dp),
                        painter = painterResource(id = R.drawable.share),
                        tint = myGreen,
                        contentDescription = "add"
                    )
                }

                IconButton(
                    onClick = {
                        if (!hasStoragePermission) {
                            launcherStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            return@IconButton
                        } else {
                            menuScreenControl.downloadFile(selectedRestaurantId.value)
                        }
                    },
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .align(Alignment.CenterVertically)
                        .size(50.dp)
                        .background(color = myYellow, shape = RoundedCornerShape(50))
                )
                {
                    Icon(
                        modifier = Modifier.padding(13.dp),
                        painter = painterResource(id = R.drawable.download),
                        tint = myGreen,
                        contentDescription = "add"
                    )
                }
            }

            Title(title = stringResource(R.string.starters))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                items(
                    items = starters,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.first))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = firstCourses,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.second))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = secondCourses,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.sides))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = sides,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.fruits))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = fruits,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.Desserts))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = desserts,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }

            Title(title = stringResource(R.string.drinks))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                items(
                    items = drinks,
                    itemContent = {
                        CourseCard(
                            course = it,
                            subtotal = subtotal,
                            orderList = orderList,
                            restaurantId = cartRestaurantId
                        )
                    }
                )
            }
            Spacer(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )
        }

    }
    BackHandler {
        /* first let's clear all the used lists ! */
        starters.clear()
        firstCourses.clear()
        secondCourses.clear()
        sides.clear()
        desserts.clear()
        fruits.clear()
        drinks.clear()

        /* navigate to home page */
        ScreenRouter.navigateTo(2, 1)
    }
}


@Composable
fun Title(title: String) {
    Divider(
        modifier = Modifier.padding(20.dp),
        color = MaterialTheme.colors.onSurface,
        thickness = 1.dp
    )
    Text(
        modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
    )
}
