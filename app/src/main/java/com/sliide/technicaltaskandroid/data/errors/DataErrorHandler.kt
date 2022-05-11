package com.sliide.technicaltaskandroid.data.errors

import android.content.Context
import com.android.volley.VolleyError
import com.sliide.technicaltaskandroid.R

class DataErrorHandler constructor(
    private val context: Context
) {

    fun sortDownloadError(
        errorType: ErrorType
    ): String = when (errorType) {
        ErrorType.JSONARRAY -> context.resources.getString(R.string.err_download_jsonarray)
        else -> context.resources.getString(R.string.err_download_unknown)
    } + context.resources.getString(R.string.err_please_try_again_later)

    fun sortVolleyError(
        volleyError: VolleyError
    ): String = when {
        (!volleyError.message.isNullOrBlank()) -> volleyError.message!!
        (!volleyError.localizedMessage.isNullOrBlank()) -> volleyError.localizedMessage!!
        else -> context.resources.getString(R.string.err_download_unknown) + context.resources.getString(R.string.err_please_try_again_later)
    }
}

