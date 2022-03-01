package com.techventure.androidbase.network

import androidx.lifecycle.MutableLiveData
import com.techventure.androidbase.ext.getErrorBody
import com.techventure.androidbase.models.AppException
import com.techventure.androidbase.network.`interface`.BaseResponse
import okhttp3.Headers
import retrofit2.Response

sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T?): ResultState<T> = Success(data)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)
    }
    data class Success<out T>(val data: T?) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()
}

fun <T> MutableLiveData<ResultState<T>>.paresResult(result: BaseResponse<T>) {
    value = if (result.isSuccess()) ResultState.onAppSuccess(result.getResponseData()) else
        ResultState.onAppError(
            AppException(
                result.getResponseCode(),
                result.getResponseMsg()
            )
        )
}

fun <T> MutableLiveData<ResultState<T>>.paresResult(result: Response<T>) {
    value =
        if (result.isSuccessful && result.body() != null) {
            ResultState.onAppSuccess(result.body())
        } else {
            ResultState.onAppError(getErrorBody(result))
        }
}

fun <T> Response<T>.paresResult(result: Response<T>): ResultState<T> {
    return if (result.isSuccessful && result.body() != null) {
        ResultState.onAppSuccess(result.body())
    } else {
        ResultState.onAppError(getErrorBody(result))
    }
}

fun <T> MutableLiveData<Headers>.parseHeader(result: Response<T>) {
    val header = if (result.isSuccessful && result.body() != null) {
        try {
            result.headers()
            if (result.headers()["x-auth-token"] != null)
                result.headers()["x-auth-token"]
            else
                ""
        } catch (e: Exception) {
            ""
        }
    } else {
        ""
    }
    postValue(result.headers())

}

fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onAppSuccess(result)
}

fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onAppError(ExceptionHandle.handleException(e))
}


fun <T> paresException(e: Throwable): ResultState<T> {
    return ResultState.onAppError(ExceptionHandle.handleException(e))
}