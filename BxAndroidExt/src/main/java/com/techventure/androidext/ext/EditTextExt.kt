package com.techventure.androidext.ext

import android.R
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView


fun EditText.getTextWidth(line: Int = -1): Float {
    val input = if (line >= 0) getTextForLine(line) else text?.toString()
    return input?.let { paint.measureText(input) } ?: 0f
}

fun EditText.getTextForLine(line: Int): String? =
    layout?.let {
        val input = text?.toString()
        val lineCharacters = input?.length ?: 0
        val start = it.getLineStart(line)
        val end = it.getLineEnd(line)
        val isInBounds = start < end && start < lineCharacters && end <= lineCharacters
        if (isInBounds) input?.substring(start, end) else null
    }

fun EditText.showPassword(status: Boolean) {
    inputType = if (status) {
        InputType.TYPE_CLASS_TEXT
    } else {
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    setSelection(text?.length ?: 0)

}

fun EditText.enablePasswordVisibility(status: Boolean) {
    transformationMethod = if (!status){
        PasswordTransformationMethod.getInstance()
    }else{
        HideReturnsTransformationMethod.getInstance()
    }
    setSelection(text?.length ?: 0)
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        when(actionId){
            EditorInfo.IME_ACTION_SEARCH, EditorInfo.IME_ACTION_DONE -> {
                callback.invoke()
                true
            }
        }
        false
    }
}

fun EditText.onTextChangeListener(callback: (updateString: CharSequence) -> Unit){
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {}
        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {}

        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
           callback(s)
        }
    })
}