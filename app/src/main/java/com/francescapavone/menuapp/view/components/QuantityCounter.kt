package com.francescapavone.menuapp.view.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.view.theme.myGreen
import com.francescapavone.menuapp.view.theme.myYellow

@Composable
fun QuantityCounter(
    modifier: Modifier = Modifier,
    count: Int,
    remove: () -> Unit,
    add: () -> Unit
) {
    Row(modifier = modifier
    ) {
        IconButton(
            onClick = remove,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(color = myYellow, shape = RoundedCornerShape(50))
                .size(25.dp)
        )
        {
            Icon(
                modifier = Modifier
                    .padding(3.dp)
                    .size(25.dp),
                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                tint = myGreen,
                contentDescription = "remove"
            )
        }

        Crossfade(
            targetState = count,
            modifier = Modifier
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "$it",
                color = MaterialTheme.colors.onSurface,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 30.dp)
            )
        }
        IconButton(
            onClick = add,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(color = myYellow, shape = RoundedCornerShape(50))
                .size(25.dp)
        )
        {
            Icon(
                modifier = Modifier
                    .padding(3.dp)
                    .size(25.dp),
                imageVector = Icons.Default.Add,
                tint = myGreen,
                contentDescription = "add"
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    QuantityCounter(count = 1, remove = { }, add = { })
}