package com.shashankmunda.photoresizercompressor.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.MimeTypeMap

object Utils {

    const val THRESHOLD: Int = 1024

    val UNITS = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    fun getImageDimensions(context: Context, uri: Uri): Pair<Int, Int>{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        val input = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(input, null, options)
        input?.close()
        return Pair(options.outWidth, options.outHeight);
    }

    fun getFileSize(context: Context, uri: Uri): Long{
        val fileDescriptor = context.contentResolver.openAssetFileDescriptor(uri, "r")
        return fileDescriptor!!.length
    }

    fun format(size: Double, decimals: Int): String {
        var size = size
        size = if (size < 0) 0.0 else size
        var u = 0
        while (u < UNITS.size - 1 && size >= THRESHOLD) {
            size /= 1024.0
            ++u
        }
        return java.lang.String.format("%." + decimals + "f %s", size, UNITS[u])
    }

    fun getFileMimeType(context: Context, uri: Uri): String {
       val mimeType = context.contentResolver.getType(uri) ?: return "N/A"
        return mimeType.substringAfter("/").uppercase()
    }
}