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
class RemoveUserBottomViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel() {

    private val _removeUserBottomState = MutableLiveData<RemoveUserBottomViewState>()
    val removeUserBottomState: LiveData<RemoveUserBottomViewState> get() = _removeUserBottomState

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
        _removeUserBottomState.postValue(RemoveUserBottomViewState(showLoading = true))
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
        apiResponse.data?.let {
            _removeUserBottomState.value = RemoveUserBottomViewState(
                id = userId,
                name = userName
            )
        }
    }

    fun publishRemoveUserErrorViewState(
        apiResponse: ApiResponse.Error<UserModel>
    ) {
        _removeUserBottomState.value = RemoveUserBottomViewState(showError = true, errorMessage = apiResponse.message ?: DEFAULT_STRING)
    }
}