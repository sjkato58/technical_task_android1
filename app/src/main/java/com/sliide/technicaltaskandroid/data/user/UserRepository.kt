package com.sliide.technicaltaskandroid.data.user

import com.android.volley.RequestQueue
import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.data.errors.DataErrorHandler
import com.sliide.technicaltaskandroid.data.errors.ErrorType
import com.sliide.technicaltaskandroid.data.ApiResponse
import dagger.hilt.android.scopes.ActivityScoped
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ActivityScoped
class UserRepository @Inject constructor(
    private val requestQueue: RequestQueue,
    private val userExtractor: UserExtractor,
    private val dataErrorHandler: DataErrorHandler
) {

    suspend fun downloadUserList(): ApiResponse<List<UserModel>> = suspendCoroutine { cont ->
        val stringRequest = UserRequest(
            USER_LIST_URL,
            { response ->
                val result = userExtractor.extractUserList(JSONArray(response))
                if (result.isNotEmpty()) {
                    cont.resume(ApiResponse.Success(result))
                } else {
                    cont.resume(ApiResponse.Error(dataErrorHandler.sortDownloadError(ErrorType.UNKNOWN)))
                }
            },
            { error ->
                cont.resume(ApiResponse.Error(dataErrorHandler.sortVolleyError(error), null))
            }
        )
        requestQueue.add(stringRequest)
    }

    suspend fun addNewUser(
        name: String,
        email: String,
        gender: String
    ): ApiResponse<UserModel> = suspendCoroutine { cont ->
        val params = mutableMapOf<String, String>()
        params[USER_NAME] = name
        params[USER_EMAIL] = email
        params[USER_GENDER] = gender
        params[USER_STATUS] = USER_STATUS_INACTIVE
        val request = PostRequest(
            USER_LIST_URL,
            params,
            { response ->
                val result = userExtractor.extractIndividualUser(JSONObject(response))
                if (result != null && result.id != DEFAULT_INTEGER) {
                    cont.resume(ApiResponse.Success(result))
                } else {
                    cont.resume(ApiResponse.Error(dataErrorHandler.sortDownloadError(ErrorType.UNKNOWN)))
                }
            },
            { error ->
                cont.resume(ApiResponse.Error(dataErrorHandler.sortVolleyError(error), null))
            }
        )
        requestQueue.add(request)
    }
}