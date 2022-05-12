package com.sliide.technicaltaskandroid.ui.removeuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING
import com.sliide.technicaltaskandroid.base.BaseViewModel
import com.sliide.technicaltaskandroid.data.ApiResponse
import com.sliide.technicaltaskandroid.data.user.UserModel
import com.sliide.technicaltaskandroid.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveUserViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _removeUserState = MutableLiveData<RemoveUserViewState>()
    val removeUserState: LiveData<RemoveUserViewState> get() = _removeUserState

    var userId: Int = DEFAULT_INTEGER
    var userName: String = DEFAULT_STRING

    fun saveUserData(
        id: Int,
        name: String
    ) {
        userId = id
        userName = name
    }

    fun removeUser() {
        _removeUserState.postValue(RemoveUserViewState(showLoading = true))
        viewModelScope.launch {
            when (val apiResponse = userRepository.removeUser(userId)) {
                is ApiResponse.Success -> publishRemoveUserViewState(apiResponse)
                is ApiResponse.Error -> publishRemoveUserErrorViewState(apiResponse)
            }
        }
    }

    fun publishRemoveUserViewState(
        apiResponse: ApiResponse.Success<UserModel>
    ) {
        apiResponse.data?.let { userModel ->
            _removeUserState.value = RemoveUserViewState(
                id = userId,
                name = userName
            )
        }
    }

    fun publishRemoveUserErrorViewState(
        apiResponse: ApiResponse.Error<UserModel>
    ) {
        _removeUserState.value = RemoveUserViewState(showError = true, errorMessage = apiResponse.message ?: DEFAULT_STRING)
    }
}