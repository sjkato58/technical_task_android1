package com.sliide.technicaltaskandroid.data.user

import com.android.volley.RequestQueue
import com.sliide.technicaltaskandroid.data.errors.DataErrorHandler
import com.sliide.technicaltaskandroid.data.errors.ErrorType
import com.sliide.technicaltaskandroid.data.ApiResponse
import dagger.hilt.android.scopes.ActivityScoped
import org.json.JSONArray
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
}