package com.sliide.technicaltaskandroid.data.user

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING
import org.json.JSONArray
import org.json.JSONObject

class UserExtractor {

    fun extractUserList(
        resultJson: JSONArray?
    ): List<UserModel> {
        val userList = mutableListOf<UserModel>()
        resultJson?.let { jsonArray ->
            val length = jsonArray.length()
            for (i in 0 until length) {
                extractIndividualUser(jsonArray.optJSONObject(i))?.let {
                    userList.add(it)
                }
            }
        }
        return userList
    }

    private fun extractIndividualUser(
        jsonObject: JSONObject?
    ): UserModel? = jsonObject?.let { userJSON ->
        UserModel(
            id = userJSON.optInt(USER_ID, DEFAULT_INTEGER),
            name = userJSON.optString(USER_NAME, DEFAULT_STRING),
            email = userJSON.optString(USER_EMAIL, DEFAULT_STRING),
            gender = userJSON.optString(USER_GENDER, DEFAULT_STRING),
            status = userJSON.optString(USER_STATUS, DEFAULT_STRING)
        )
    }
}