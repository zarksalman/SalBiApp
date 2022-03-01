package com.techventure.androidext.ext


import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import androidx.navigation.fragment.NavHostFragment


fun <T> Activity.startExtActivity(
    activityClass: Class<T>,
    activityOption: ActivityOptions? = null,
    isFinishAffinity: Boolean = false,
    isFinish: Boolean = false,
    intent: Intent? = null,
    bundle: Bundle? = null,
) {
    val newIntent = intent ?: Intent(this@startExtActivity, activityClass)
    bundle?.apply { newIntent.putExtras(bundle) }

    activityOption?.apply {
        startActivity(newIntent, toBundle())
    } ?: run {
        startActivity(newIntent)
    }
    if (isFinishAffinity) finishAffinity()
    if (isFinish && !isFinishAffinity) finish()
}

fun <T> Activity.startExtActivityOnResult(
    activityClass: Class<T>,
    activityOption: ActivityOptions? = null,
    intent: Intent? = null,
    bundle: Bundle? = null,
    resultCode:Int
) {
    val newIntent = intent ?: Intent(this@startExtActivityOnResult, activityClass)
    bundle?.apply { newIntent.putExtras(bundle) }

    activityOption?.apply {
        startActivityForResult(newIntent,resultCode,toBundle())
    } ?: run {
        startActivityForResult(newIntent,resultCode)
    }

}

fun <T> Fragment.startExtActivityOnResult(
    activityClass: Class<T>,
    activityOption: ActivityOptions? = null,
    intent: Intent? = null,
    bundle: Bundle? = null,
    resultCode:Int
) {
    val newIntent = intent ?: Intent(activity, activityClass)
    bundle?.apply { newIntent.putExtras(bundle) }

    activityOption?.apply {
        startActivityForResult(newIntent,resultCode,toBundle())
    } ?: run {
        startActivityForResult(newIntent,resultCode)
    }

}

fun Activity.startExplicitActivityOnResult(
    activityOption: ActivityOptions? = null,
    intent: Intent,
    bundle: Bundle? = null,
    resultCode:Int
) {
    bundle?.apply { intent.putExtras(bundle) }

    activityOption?.apply {
        startActivityForResult(intent,resultCode,toBundle())
    } ?: run {
        startActivityForResult(intent,resultCode)
    }

}
fun Fragment.startExplicitActivityOnResult(
    activityOption: ActivityOptions? = null,
    intent: Intent,
    bundle: Bundle? = null,
    resultCode:Int
) {
    bundle?.apply { intent.putExtras(bundle) }

    activityOption?.apply {
        startActivityForResult(intent,resultCode,toBundle())
    } ?: run {
        startActivityForResult(intent,resultCode)
    }
}
fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}


fun nav(view: View): NavController {
    return Navigation.findNavController(view)
}

fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    try {

        if (currentDestination?.id != resId) {
            navigate(resId, args, navOptions, navExtras)
        }
    } catch (e: Exception) {

    }
}





