package com.techventure.androidext.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Base64
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.techventure.androidext.utils.ExtConstant.JPEG_EXTENSION
import com.techventure.androidext.utils.ExtConstant.JPEG_MIME_TYPE
import com.techventure.androidext.utils.ExtConstant.JPG_EXTENSION
import com.techventure.androidext.utils.ExtConstant.PNG_EXTENSION
import com.techventure.androidext.utils.ExtConstant.PNG_MIME_TYPE
import java.io.ByteArrayOutputStream
import java.io.File

object ImageUtils {
    private fun getBase64String(file: File?): String {
        if (file != null) {
            val bytes = file.readBytes()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }
        return ""
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    fun getUriFromFile(authority:String,context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {

            FileProvider.getUriForFile(context, "$authority.provider", file)
        }
    }

    fun getTempImagePath(context: Context, name: String): File {
        return File.createTempFile(name, "", context.cacheDir)

    }

    fun convertToBase64(imagePath: String, quality: Int = 100): String {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    fun rotateImageIfRequired(img: Bitmap, selectedImage: String): Bitmap? {
        return try {
            val ei = ExifInterface(selectedImage)
            val orientation: Int? =
                ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
                else -> img
            }
        } catch (e: Exception) {
            img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg =
            Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    fun base64ToImage(base64String: String): Bitmap? {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun loadImageFromLocalStorage(imageView: ImageView, imagePath: String?) {
        val imgFile = File(imagePath!!)
        if (imgFile.exists()) {
            //val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageView.setImageURI(Uri.fromFile(imgFile))
        }
    }

    fun getImageExtension(type:String):String{
        return when (type){
            PNG_MIME_TYPE -> PNG_EXTENSION
            JPEG_MIME_TYPE -> JPEG_EXTENSION
            else -> JPG_EXTENSION
        }
    }

}