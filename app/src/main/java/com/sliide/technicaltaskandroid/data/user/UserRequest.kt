package com.sliide.technicaltaskandroid.data.user

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.sliide.technicaltaskandroid.data.AUTH_TOK

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
        headers["Authorization"] = "Bearer $AUTH_TOK"
        return headers
    }
}