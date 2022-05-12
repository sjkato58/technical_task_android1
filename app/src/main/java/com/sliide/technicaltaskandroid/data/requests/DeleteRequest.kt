package com.sliide.technicaltaskandroid.data.requests

import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.sliide.technicaltaskandroid.BuildConfig
import com.sliide.technicaltaskandroid.data.AUTHORIZATION
import com.sliide.technicaltaskandroid.data.BEARER

class DeleteRequest constructor(
    url: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
): StringRequest(
    Method.DELETE,
    url,
    listener,
    errorListener
) {

    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers[AUTHORIZATION] = "$BEARER ${BuildConfig.AUTH_TOKEN}"
        return headers
    }

    override fun parseNetworkError(volleyError: VolleyError?): VolleyError {
        if (volleyError?.networkResponse != null && volleyError.networkResponse.data != null) {
            val responseData = String(volleyError.networkResponse.data)
            if (responseData.isNotBlank() && responseData.isNotEmpty()) {
                return VolleyError(responseData)
            }
        }
        return super.parseNetworkError(volleyError)
    }
}