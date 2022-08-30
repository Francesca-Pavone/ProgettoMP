package com.francescapavone.menuapp.view.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.francescapavone.menuapp.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

@Composable
fun NetworkImageComponentPicasso(
    url: String,
    modifier: Modifier = Modifier,
    size: Int

) {
    val context = LocalContext.current
    val sizeModifier = modifier
        .size(size.dp)
        .sizeIn(maxHeight = 40.dp)

    val error = BitmapFactory.decodeResource(context.resources, R.drawable.intl_closed)
    val waitPlease = BitmapFactory.decodeResource(context.resources, R.drawable.intl_closed)

    var image by remember { mutableStateOf<ImageBitmap?>(null) }
    var drawable by remember { mutableStateOf<Drawable?>(null) }

    DisposableEffect(url) {
        val picasso = Picasso.get()
        val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                image = waitPlease.asImageBitmap()
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                image = error.asImageBitmap()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                image = bitmap?.asImageBitmap()
            }

        }
        picasso
            .load(url)
            .noFade()
            .resize(380,380)
            .centerCrop()
            .error(R.drawable.ic_launcher_foreground)
            .into(target)
        onDispose {
            image = null
            drawable = null
            picasso.cancelRequest(target)
        }
    }

    val theImage = image
    val theDrawable = drawable
    if (theImage != null) {
        Column(
            modifier = sizeModifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(bitmap = theImage, contentDescription = null)
        }
    } else if (theDrawable != null) {
        Canvas(modifier = sizeModifier) {
            drawIntoCanvas { canvas ->
                theDrawable.draw(canvas.nativeCanvas)
            }
        }
    }
}
