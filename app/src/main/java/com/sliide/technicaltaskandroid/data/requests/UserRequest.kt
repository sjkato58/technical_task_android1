package com.sliide.technicaltaskandroid.data.requests

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.sliide.technicaltaskandroid.BuildConfig
import com.sliide.technicaltaskandroid.data.AUTHORIZATION
import com.sliide.technicaltaskandroid.data.BEARER

class UserRequest constructor(
    url: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
): StringRequest(
    Method.GET,
    url,
    listener,
    errorListener
) {

    override fun getHeaders(): MutableMap<String, String> {
        val headers = mutableMapOf<String, String>()
        headers[AUTHORIZATION] = "$BEARER ${BuildConfig.AUTH_TOKEN}"
        return headers
    }
}