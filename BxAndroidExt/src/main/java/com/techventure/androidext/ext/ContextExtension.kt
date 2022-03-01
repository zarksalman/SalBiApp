package com.techventure.androidext.ext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Context.runTimePermissions(
    permissions: List<String>,
    permissionResponse: (Boolean, Boolean) -> Unit,
) {
    Dexter.withContext(this)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {

                    if (report.areAllPermissionsGranted()) {
                        permissionResponse(true, false)
                        return
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        permissionResponse(false, true)
                        return
                    }
                    permissionResponse(false, false)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?,
            ) {
                token?.continuePermissionRequest()
                permissionResponse(false, false)
            }
        })
        .withErrorListener {
        }
        .check()
}

fun Context.openSettings() {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.checkUserPermissionGranted(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.checkStoragePermission(): Boolean {
    val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    return checkUserPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) || minSdk29
}