package com.sliide.technicaltaskandroid.ui.userlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sliide.technicaltaskandroid.DEFAULT_STRING
import com.sliide.technicaltaskandroid.data.ApiResponse
import com.sliide.technicaltaskandroid.data.user.UserModel
import com.sliide.technicaltaskandroid.data.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userList = MutableLiveData<List<UserListViewState>>()
    val userList: LiveData<List<UserListViewState>> get() = _userList

    fun downloadUserList() {
        _userList.postValue(listOf(UserListViewState(isLoading = true)))
        viewModelScope.launch {
            when (val apiResponse = userRepository.downloadUserList()) {
                is ApiResponse.Success -> publishUserListViewState(apiResponse)
                is ApiResponse.Error -> publishUserListErrorViewState(apiResponse)
            }
        }
    }

    fun publishUserListViewState(
        apiResponse: ApiResponse.Success<List<UserModel>>
    ) {
        apiResponse.data?.let { responseList ->
            _userList.value = responseList.map { userModel ->
                UserListViewState(
                    id = userModel.id,
                    name = userModel.name,
                    email = userModel.email,
                    gender = userModel.gender
                )
            }
        }
    }

    fun publishUserListErrorViewState(
        apiResponse: ApiResponse.Error<List<UserModel>>
    ) {
        _userList.value = listOf(UserListViewState(showError = true, errorMessage = apiResponse.message ?: DEFAULT_STRING))
    }

    fun navigateToDeleteUser(
        data: UserListViewState
    ) {
        Log.d("seiji", "navigateToDeleteUser: $data")
    }
}