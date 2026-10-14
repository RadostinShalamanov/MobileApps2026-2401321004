package bg.uni_plovdiv.fmi.plantcare.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File

object ImageUtils {

    fun createImageFile(context: Context): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.filesDir
        return File.createTempFile("plant_", ".jpg", dir)
    }

    /** Зарежда снимка умалена до около [reqSize] px, за да не се пълни паметта. */
    fun decodeScaled(path: String, reqSize: Int): Bitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(path, bounds)
        var sample = 1
        while (bounds.outWidth / (sample * 2) >= reqSize && bounds.outHeight / (sample * 2) >= reqSize) {
            sample *= 2
        }
        return BitmapFactory.decodeFile(path, BitmapFactory.Options().apply { inSampleSize = sample })
    }
}