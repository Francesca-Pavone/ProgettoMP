package com.francescapavone.menuapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.model.Course
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow

@Composable
fun CourseCard(course: Course, subtotal: MutableState<Double>, orderList: MutableList<Course>, restaurantId: MutableState<Int> ) {

    var (count, updateCount) = rememberSaveable { mutableStateOf(course.count) }
    val openDialogDescription = rememberSaveable { mutableStateOf(false) }
    val openDialogNewRestaurant = rememberSaveable { mutableStateOf(false) }

    for(i in orderList){
        if(i.name==course.name && i.restaurantId==course.restaurantId){
            course.count=i.count
            orderList.remove(i)
            orderList.add(course)
            break
        }
    }
    if (openDialogDescription.value) {
        AlertDialog(
            shape = RoundedCornerShape(14.dp),
            onDismissRequest = {
                openDialogDescription.value = false
            },
            title = {
                Text(text = course.name, fontSize = 16.sp, color = MaterialTheme.colors.onSurface, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = course.description, fontSize = 14.sp, color = MaterialTheme.colors.onSurface)
            },
            buttons = {
                Row(
                modifier = Modifier.padding(all = 14.dp),
                horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialogDescription.value = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = myYellow)
                    ) {
                        Text(
                            text = "OK",
                            color = myGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }

    if (openDialogNewRestaurant.value) {
        AlertDialog(
            shape = RoundedCornerShape(14.dp),
            onDismissRequest = {
                openDialogNewRestaurant.value = false
            },

            text = { 
                Text(
                text = stringResource(id = R.string.orderMessage),
                color = MaterialTheme.colors.onSurface
            ) },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(all = 14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 50.dp),
                        onClick = {
                            for (i in orderList) {
                                i.count = 0
                            }
                            orderList.removeAll(orderList)
                            orderList.add(course)
                            updateCount(1)
                            course.count = course.count + 1
                            subtotal.value = course.price.toDouble()
                            restaurantId.value = course.restaurantId.toInt()
                            openDialogNewRestaurant.value = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = myYellow)
                    ) {
                        Text(
                            text = stringResource(id = R.string.accept),
                            color = myGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = {
                            openDialogNewRestaurant.value = false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = myYellow)
                    ) {
                        Text(
                            text = stringResource(id = R.string.dismiss),
                            color = myGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(start = 20.dp)
        ) {
        Card(
            modifier = Modifier
                .padding(top = 30.dp)
                .width(180.dp),
            shape = RoundedCornerShape(14.dp),
            elevation = 5.dp
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(top = 100.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            ) {
                Text(
                    text = course.name,
                    modifier = Modifier.padding(top = 5.dp),
                    fontSize = 16.sp
                )
                Text(
                    text = "€ ${String.format("%.2f", course.price.toDouble())}",
                    color = myYellow,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                QuantityCounter(
                    modifier = Modifier
                        .align(Alignment.End),
                    count = course.count,
                    remove = {
                        if (count == 1) {
                            orderList.remove(course)
                            if (orderList.isEmpty())
                                restaurantId.value = -1
                        }
                        if (count > 0) {
                            updateCount(count - 1)
                            course.count = course.count - 1
                            subtotal.value = subtotal.value - course.price.toDouble()
                        }
                    },
                    add = {
                        if (orderList.isEmpty())
                            restaurantId.value = course.restaurantId.toInt()
                        if (count == 0 && course.restaurantId.toInt() == restaurantId.value)
                            orderList.add(course)
                        if (course.restaurantId.toInt() == restaurantId.value) {
                            updateCount(count + 1)
                            course.count = course.count + 1
                            subtotal.value = subtotal.value + course.price.toDouble()
                        }else{
                            openDialogNewRestaurant.value = true
                        }

                    }
                )
            }

        }
        NetworkImageComponentPicasso(
            url = course.poster,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { openDialogDescription.value = true },
            130
        )
    }
}

@Composable
fun OrderedCourseCard(course: Course, subtotal: MutableState<Double>, orderList: MutableList<Course>/*, animationColor: MutableState<Boolean>*/){

    val (count, updateCount) = rememberSaveable { mutableStateOf(course.count) }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(start = 20.dp)
    ){
        Card(
            modifier = Modifier
                .padding(end = 30.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(14.dp),
            elevation = 5.dp
        ){
           Row(
               horizontalArrangement = Arrangement.Start,
               modifier = Modifier
                   .padding(end = 100.dp, start = 20.dp, top = 10.dp, bottom = 10.dp)

           ) {
               NetworkImageComponentPicasso(url = course.poster, modifier = Modifier.clip(CircleShape), 90)
               Column(
                   horizontalAlignment = Alignment.Start,
                   modifier = Modifier.padding(start = 10.dp)
               ) {
                   Text(
                       text = course.name,
                       modifier = Modifier.padding(top = 5.dp),
                       fontSize = 16.sp
                   )
                   Text(
                       modifier = Modifier.padding(top = 10.dp),
                       text = "€ ${String.format("%.2f", course.price.toDouble())}",
                       color = myYellow,
                       fontSize = 18.sp,
                       fontWeight = FontWeight.Bold
                   )
               }
           }
        }
        QuantityCounter(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(color = myYellow, shape = RoundedCornerShape(50))
                .padding(5.dp),
            count = course.count,
            remove = {
//                animationColor.value = !animationColor.value
                if (count == 1)
                    orderList.remove(course)
                if (count > 0){
                    updateCount(count - 1)
                    course.count = course.count - 1
                    subtotal.value = subtotal.value - course.price.toDouble()
                }
            },
            add = {
//                animationColor.value = !animationColor.value
                if(!orderList.contains(course))
                    orderList.add(course)
                updateCount(count + 1)
                course.count = course.count + 1
                subtotal.value = subtotal.value + course.price.toDouble()
            }
        )
    }
}
