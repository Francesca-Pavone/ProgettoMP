package com.francescapavone.menuapp.control

import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.francescapavone.menuapp.R
import com.francescapavone.menuapp.control.download.DownloadHelper
import com.github.sumimakito.awesomeqr.AwesomeQrRenderer
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.logo.Logo

class MenuScreenControl(var context: Context, var downloadManager: DownloadManager) {
    fun generateQRCode(selectedRestaurantId: String): Bitmap? {

        /* set QR colors*/
        val color = com.github.sumimakito.awesomeqr.option.color.Color()
        color.light = 0xFFFFFFFF.toInt() /* for blank spaces */
        color.dark = 0xFF2E7855.toInt() /* for non-blank spaces */
        color.background = 0xFFFFFFFF.toInt() /* for the background */
        color.auto = false /* set to true to automatically pick out colors from the background image (will only work if background image is present) */

        val renderOption = RenderOption()

        /* url to encode */
        renderOption.content = "https://github.com/al3ssandrocaruso/restaurantsappdata/raw/main/menus/PDFsMenu/Menu${selectedRestaurantId}.pdf"

        renderOption.size = 600 /* size of the final QR code image */
        renderOption.borderWidth = 20 /* width of the empty space around the QR code */
        renderOption.patternScale = 0.75f /* (optional) specify a scale for patterns */
        renderOption.clearBorder = true /* if set to true, the background will NOT be drawn on the border area */
        renderOption.color = color /* set a color palette for the QR code */

        /* insert logo in the qr */
        val logoImage: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher)
        if (logoImage != null) {
            val logo = Logo()
            logo.bitmap = logoImage
            logo.borderRadius = 50
            logo.borderWidth = 50
            logo.scale = 0.4f
            renderOption.logo = logo
        }
        val result = AwesomeQrRenderer.render(renderOption)
        return result.bitmap /* return the bitmap of the result */
    }
    fun downloadFile(selectedRestaurantId: String){
        val downloadHelper= DownloadHelper()

        /* pdf to download */
        val urlMenuPdf = "https://github.com/al3ssandrocaruso/restaurantsappdata/raw/main/menus/PDFsMenu/Menu${selectedRestaurantId}.pdf"

        /* name of the downloaded file */
        val filename = "Menu$selectedRestaurantId"
        downloadHelper.download( urlMenuPdf, filename, downloadManager,context)
    }
}