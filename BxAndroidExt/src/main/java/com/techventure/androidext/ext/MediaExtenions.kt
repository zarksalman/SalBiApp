package com.techventure.androidext.ext

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.techventure.androidext.utils.ExtConstant
import com.techventure.androidext.utils.ExtConstant.ONE_FLOAT
import com.techventure.androidext.utils.ImageUtils
import com.techventure.androidext.utils.getCalender
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*


fun File.getFileExtension(): String? {
    return Uri.fromFile(this)?.lastPathSegment?.split(".")?.get(1)
}

fun Uri.getFileExtension(): String? {
    try{
        return Uri.fromFile(File(this.path))?.lastPathSegment?.split(".")?.get(1)
    }catch (ex: Exception){

    }
    return null
}

fun File.getUriFromFile(context: Context, authority: String): Uri {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(this)
    } else {
        FileProvider.getUriForFile(context, authority, this)
    }
}

fun createDir(path: String, dirName: String): String {
    val userDir = File("${path}/${dirName}")
    if (!userDir.isDirectory) {
        userDir.mkdirs()
    }
    return userDir.absolutePath
}

fun createFile(name: String, path: String): File {
    val file = File(path, name)
    file.createNewFile()
    return file
}


fun Context.createTempFile(prefix: String, suffix: String, shouldOverride: Boolean): File {
    return File.createTempFile(
        prefix + if (!shouldOverride) UUID.randomUUID() else "", suffix, cacheDir
    )
}

fun Any.imageToBase64(quality: Int = 100): String? {
    val filePath = when (this) {
        is File -> path
        is String -> this
        is Uri -> this.path
        else -> return null
    }
    val bitmap = BitmapFactory.decodeFile(filePath)
    val outputStream = ByteArrayOutputStream()
    try {
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }catch (ex: OutOfMemoryError){
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }catch (e: Exception){

    }
    return null
}

fun File.convertToBase64(): String {
    return Base64.encodeToString(this.readBytes(), Base64.DEFAULT)
}


fun Any.imageToByteArray(quality: Int = 100): ByteArray? {
    val filePath = when (this) {
        is File -> path
        is String -> this
        else -> return null
    }
    val bitmap = BitmapFactory.decodeFile(filePath)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return outputStream.toByteArray()
}

fun Uri.convertUriToBase64(context: Context?) : String? {
    try {
        val fileInputStream = context?.contentResolver?.openInputStream(this)
        val bytes = fileInputStream?.let { getBytes(it) }
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
    catch (ex: OutOfMemoryError){
        print(ex)
    }
    catch (e: Exception) {
        print(e)
    }
    return null
}

@Throws(IOException::class)
fun getBytes(inputStream: InputStream): ByteArray? {
    try {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }catch (e: Exception){
        print(e)
    }
    return null
}


fun Uri.getFileSizeInBytes(context: Context):Long{
    context.contentResolver.query(this, null, null, null, null)?.use { cursor->
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()
        return cursor.getLong(sizeIndex)
    }
    return 0
}

fun Uri.convertFileSize(context: Context,sizeType:String):Long{
    val fileByte =  getFileSizeInBytes(context)
    val byte = 1024
    val sizeKB = fileByte / byte
    val sizeMB = sizeKB / byte
    val sizeGB = sizeMB / byte
    with(ExtConstant){
        when(sizeType){
            KB->return sizeKB
            MB->return sizeMB
            GB->return sizeGB
            else -> -1
        }
    }
    return -1
}

fun Activity.openDocument(resultCode:Int) {
    with(ExtConstant){
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_OPEN_DOCUMENT
            addCategory(Intent.CATEGORY_OPENABLE)
            val mimeTypes = arrayOf(PDF_MIME_TYPE, DOCX_MIME_TYPE)
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            type = ALL_TYPE
        }
        startExplicitActivityOnResult(intent = intent, resultCode = resultCode)
    }
}

fun Context.downloadFileFromURL(url: String,fileContentType:String,downloadingMsg:String) {
    fileContentType.also {
        resources.apply {
            ExtConstant.apply {
                GlobalScope.launch(Dispatchers.IO) {
                    val downloadManager =
                        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    var fileName = getCalender().timeInMillis.toString()
                    val request = DownloadManager.Request(
                        Uri.parse(url)
                    )
                    request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                    )
                        .setAllowedOverRoaming(false).setTitle(fileName)
                        .setMimeType(it)
                        .setDescription(
                            when (it) {
                                PDF_MIME_TYPE -> downloadingMsg
                                DOCX_MIME_TYPE -> downloadingMsg
                                else -> downloadingMsg
                            })
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            "$fileName${if (it == PDF_MIME_TYPE) PDF_EXTENSION else if(it== DOCX_MIME_TYPE) DOCX_EXTENSION else ImageUtils.getImageExtension(it)}"
                        )
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    downloadManager.enqueue(request)
                }
            }
        }
    }
}

fun Activity.takeImage(
    isUsingCamera: Boolean,
    launcher: ActivityResultLauncher<Intent>,
) {
    ImagePicker.with(this).apply {
        saveDir(cacheDir)
        if (isUsingCamera) {
            cameraOnly()
        } else {
            galleryOnly()
        }
        crop(ONE_FLOAT, ONE_FLOAT)
        galleryMimeTypes(
            mimeTypes = arrayOf(
                ExtConstant.PNG_MIME_TYPE,
                ExtConstant.JPG_MIME_TYPE,
                ExtConstant.JPEG_MIME_TYPE
            )
        )
        createIntent {
            launcher.launch(it)
        }
    }
}