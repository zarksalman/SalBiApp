package com.techventure.androidext.ext

import android.content.Context
import com.techventure.androidext.utils.ExtRegexUtils
import java.util.*
import java.util.regex.Pattern

fun String.isValidEmail(): Boolean = isValid(ExtRegexUtils.EMAIL_REGEX)
fun String.isValidPassword(): Boolean = isValid(ExtRegexUtils.PASSWORD_REGEX)


fun String.isValid(regex: String?): Boolean {
    if (regex.isNullOrEmpty()) return false
    val matcher = Pattern.compile(regex).matcher(this)
    return matcher.matches()
}

fun String.trimExtraSpaces(): String = replace("\\s+".toRegex(), " ").trim()

fun String.capFirstLetter(): String {
    if (isNullOrEmpty()) return ""
    return substring(0, 1)
        .toUpperCase(Locale.ROOT) + substring(1)
}

fun String.capWordFirstLatter(): String = this.split(' ')
    .joinToString(" ") { it.capitalize(Locale.getDefault()) }

fun String.capWordFirstLetterWithoutUnderscore(): String = this.split('_')
    .joinToString(" ") { it.capitalize(Locale.getDefault()) }

fun String.replaceWord(
    charToReplace:String,
    charReplaceWith:String
): String = replace(charToReplace, charReplaceWith)

fun Context.getResourceString(resId:Int):String{
   return resources.getString(resId)
}