package com.sliide.technicaltaskandroid.ui.adduser

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sliide.technicaltaskandroid.DEFAULT_STRING
import com.sliide.technicaltaskandroid.R
import com.sliide.technicaltaskandroid.base.BaseViewModel
import com.sliide.technicaltaskandroid.data.ApiResponse
import com.sliide.technicaltaskandroid.data.user.UserModel
import com.sliide.technicaltaskandroid.data.user.UserRepository
import com.sliide.technicaltaskandroid.ui.userlist.UserListViewState
import com.sliide.technicaltaskandroid.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserBottomViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _addUserState = MutableLiveData<AddUserViewState>()
    val addUserState: LiveData<AddUserViewState> get() = _addUserState

    fun addNewUser(
        name: String,
        email: String,
        gender: String
    ) {
        when {
            name.isEmpty() -> {
                _addUserState.postValue(AddUserViewState(nameError = R.string.err_name_empty))
            }
            (email.isEmpty()) -> {
                _addUserState.postValue(AddUserViewState(emailError = R.string.err_email_empty))
            }
            (!isEmailValid(email)) -> {
                _addUserState.postValue(AddUserViewState(emailError = R.string.err_email_invalid))
            }
            else -> {
                _addUserState.postValue(AddUserViewState(showLoading = true))
                viewModelScope.launch {
                    when (val apiResponse = userRepository.addNewUser(name, email, gender)) {
                        is ApiResponse.Success -> publishAddUserViewState(apiResponse)
                        is ApiResponse.Error -> publishAddUserErrorViewState(apiResponse)
                    }
                }
            }
        }
    }

    fun publishAddUserViewState(
        apiResponse: ApiResponse.Success<UserModel>
    ) {
        apiResponse.data?.let { userModel ->
            _addUserState.value = AddUserViewState(
                id = userModel.id,
                name = userModel.name
            )
        }
    }

    fun publishAddUserErrorViewState(
        apiResponse: ApiResponse.Error<UserModel>
    ) {
        _addUserState.value = AddUserViewState(showError = true, errorMessage = apiResponse.message ?: DEFAULT_STRING)
    }
}