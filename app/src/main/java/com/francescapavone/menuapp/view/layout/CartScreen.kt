package com.francescapavone.menuapp.view.layout

import android.content.res.Configuration
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.view.components.OrderedCourseCard
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow
import com.francescapavone.menuapp.control.ScreenRouter
import com.francescapavone.menuapp.control.CartControl


@Composable
fun Cart(
    subtotal: MutableState<Double>,
    deliveryPrice: MutableState<Double>,
    orderList: MutableList<Course>,
    restaurantId: MutableState<Int>,
    cartControl: CartControl
){

    /* to manage dialogs for user input */
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val openDataPicker = rememberSaveable { mutableStateOf(false) }
    val openTimePicker = rememberSaveable { mutableStateOf(false) }

    /* Order's information */
    val deliveryAddress = rememberSaveable { mutableStateOf("") } /* the address inserted by the user*/
    val referentName = rememberSaveable { mutableStateOf("") } /* the name inserted by the user*/
    val deliveryDate = rememberSaveable { mutableStateOf("") } /* the date inserted by the user*/
    val deliveryTime = rememberSaveable { mutableStateOf("") } /* the delivery time inserted by the user*/
    
//    val animationColor = rememberSaveable { mutableStateOf(false) }

    if (openDataPicker.value) {
        Dialog(onDismissRequest = { openDataPicker.value = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .wrapContentWidth()
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "",
                    tint = colorResource(android.R.color.darker_gray),
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clickable { openDataPicker.value = false }
                        .align(Alignment.End)
                )
                /* Date picker */
                AndroidView({ CalendarView(it) },
                    Modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colors.surface, RoundedCornerShape(14.dp)),
                    update = { view ->
                        view.setOnDateChangeListener { _, year, mon, dom ->
                            deliveryDate.value = "${year}-${mon}-${dom}"

                            openDataPicker.value = false
                        }
                    }
                )
            }
        }
    }

    if (openTimePicker.value) {
        Dialog(onDismissRequest = { openTimePicker.value = false }) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .wrapContentWidth()
                    .padding(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    contentDescription = "",
                    tint = colorResource(android.R.color.darker_gray),
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clickable { openTimePicker.value = false }
                        .align(Alignment.End)
                )
                /* Time picker */
                AndroidView({ TimePicker(it) },
                    Modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colors.surface, RoundedCornerShape(14.dp)),
                    update = { view ->
                        view.setOnTimeChangedListener { _, hour, min ->
                            deliveryTime.value = String.format("%2d:%2d", hour, min)

                        }
                    }
                )
            }
        }
    }

    if (openDialog.value) {
        if (orderList.isEmpty()) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = stringResource(id = R.string.emptyCartMess))},
                shape = RoundedCornerShape(14.dp),
                backgroundColor = MaterialTheme.colors.surface,
                buttons = {
                    Row(
                    modifier = Modifier.padding(all = 14.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = myYellow)
                    ) {
                        Text(
                            text = "OK",
                            color = myGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }}
            )

        } else {
            Dialog(onDismissRequest = { openDialog.value = false }) {

                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .wrapContentWidth()
                        .padding(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = "",
                        tint = colorResource(android.R.color.darker_gray),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .clickable { openDialog.value = false }
                            .align(Alignment.End)
                    )
                    InfoOrderTextField(
                        text = deliveryAddress,
                        placeholder = stringResource(id = R.string.address),
                        iconId = R.drawable.place,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(),
                        enabled = true
                    )
                    InfoOrderTextField(
                        text = referentName,
                        placeholder = stringResource(id = R.string.name),
                        iconId = R.drawable.person,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth(),
                        enabled = true
                    )

                    InfoOrderTextField(
                        text = deliveryDate,
                        placeholder = stringResource(id = R.string.date),
                        iconId = R.drawable.calendar,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                            .clickable { openDataPicker.value = true },
                        enabled = false
                    )

                    InfoOrderTextField(
                        text = deliveryTime,
                        placeholder = stringResource(id = R.string.time),
                        iconId = R.drawable.time,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .fillMaxWidth()
                            .clickable { openTimePicker.value = true },
                        enabled = false
                    )


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            /* clicked send, so send the order! */
                            cartControl.sendOrder(orderList,deliveryAddress.value,referentName.value,subtotal,deliveryPrice.value,deliveryDate.value,deliveryTime.value)
                            openDialog.value = false
                            for (i in orderList) {
                                i.count = 0
                            }
                            orderList.removeAll(orderList) /* clear the order list */
                            restaurantId.value = -1
                            subtotal.value = 0.0
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = myYellow)
                    ) {
                        Text(
                            text = stringResource(id = R.string.send),
                            color = myGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.bg_app_dark else R.drawable.bg_app),
        contentDescription = "bg",
        contentScale = ContentScale.FillBounds
    )
    Scaffold(
        backgroundColor = Color.Transparent,
        bottomBar = { CartBottomBar(subtotal = subtotal, deliveryPrice, openDialog/*, animationColor*/) },
        topBar = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                text = stringResource(R.string.Cart),
                color = MaterialTheme.colors.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(orderList.size != 0){
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(20.dp),
                ) {
                    items(
                        items = orderList,
                        itemContent = {
                            OrderedCourseCard(course = it, subtotal = subtotal, orderList = orderList/*, animationColor = animationColor*/)
                        }
                    )
                }
            }
        }
    }

    BackHandler {
        /* navigate to previous screen*/
        ScreenRouter.navigateTo(3, ScreenRouter.previousScreen.value)
    }
}

@Composable
fun CartBottomBar(subtotal: MutableState<Double>, deliveryPrice: MutableState<Double>, openDialog: MutableState<Boolean>/*, animationColor: MutableState<Boolean>*/){

    val conf = LocalConfiguration.current

    val portrait = when (conf.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            false
        }
        else -> {
            true
        }
    }

    /*val startColor = myYellow
    val endColor = myDarkYellow

    val backgroundColor by animateColorAsState(
        if (animationColor.value) endColor else startColor,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(
                durationMillis = 500,
                delayMillis = 30,
                easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )*/

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            )
            .padding(20.dp)
    ) {
        TextAndPrice(text = stringResource(R.string.subTotal), price = subtotal.value, portrait)
        TextAndPrice(text = stringResource(R.string.delivery), price = deliveryPrice.value, portrait)
        Divider(
            modifier = Modifier.padding(vertical = if (portrait) 10.dp else 5.dp),
            color = MaterialTheme.colors.onSurface,
            thickness = 1.dp
        )
        TextAndPrice(text = stringResource(R.string.total), price = subtotal.value + deliveryPrice.value, portrait)
        TextButton(
            modifier = Modifier
                .padding(top = if (portrait) 20.dp else 0.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                openDialog.value = true
            },
            shape = RoundedCornerShape(30),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            colors = ButtonDefaults.textButtonColors(
                backgroundColor = /*backgroundColor*/ myYellow,
                contentColor = Color.Black)
        ) {
            Text(
                text = stringResource(R.string.conf),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 70.dp)
            )
            Icon(Icons.Rounded.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun TextAndPrice(text: String, price: Double, portrait: Boolean){
    Row(
        modifier = Modifier.padding(vertical = if (portrait) 5.dp else 2.dp)
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start),
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.End),
            text = "€ ${String.format("%.2f", price)}" ,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun InfoOrderTextField(text: MutableState<String>, placeholder: String, iconId: Int, modifier: Modifier, enabled: Boolean) {
    OutlinedTextField(
        modifier = modifier,
        value = text.value,
        onValueChange = { text.value = it },
        placeholder = { Text(text = placeholder)},
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface,
            placeholderColor = Color.Gray,
            focusedBorderColor = myYellow,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.Gray,
            disabledPlaceholderColor = Color.Gray,
            disabledTextColor = Color.Gray
        ),
        enabled = enabled
    )
}