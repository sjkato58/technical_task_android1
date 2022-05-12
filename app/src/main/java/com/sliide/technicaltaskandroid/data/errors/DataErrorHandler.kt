package com.sliide.technicaltaskandroid.data.errors

import android.content.Context
import com.android.volley.VolleyError
import com.sliide.technicaltaskandroid.R
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

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
        (!volleyError.message.isNullOrBlank()) -> {
            convertResponseToErrorMessage(volleyError.message!!)
        }
        (!volleyError.localizedMessage.isNullOrBlank()) -> volleyError.localizedMessage!!
        else -> context.resources.getString(R.string.err_download_unknown) + context.resources.getString(R.string.err_please_try_again_later)
    }

    fun convertResponseToErrorMessage(message: String): String {
        var json = JSONTokener(message).nextValue()
        when (json) {
            is JSONObject -> {
                val errorMessage = json.optString(ERROR_MESSAGE)
                if (errorMessage.isNotEmpty() && errorMessage.isNotBlank()) {
                    return errorMessage
                }
            }
            is JSONArray -> {
                val stringList = mutableListOf<String>()
                val length = json.length()
                for (i in 0 until length) {
                    stringList.add(extractErrorFields(json.optJSONObject(i)))
                }
                if (stringList.size > 0) {
                    return stringList.joinToString("\r\n")
                }
            }
        }
        return message
    }

    fun extractErrorFields(jsonObject: JSONObject): String = "${jsonObject.optString(ERROR_FIELD)} ${jsonObject.optString(ERROR_MESSAGE)}"
}

