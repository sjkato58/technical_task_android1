package com.sliide.technicaltaskandroid.ui.adduser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sliide.technicaltaskandroid.DEFAULT_STRING
import com.sliide.technicaltaskandroid.base.BaseViewModel
import com.sliide.technicaltaskandroid.data.ApiResponse
import com.sliide.technicaltaskandroid.data.user.UserModel
import com.sliide.technicaltaskandroid.data.user.UserRepository
import com.sliide.technicaltaskandroid.utils.isEmailValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserBottomViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _addUserBottomState = MutableLiveData<AddUserBottomViewState>()
    val addUserBottomState: LiveData<AddUserBottomViewState> get() = _addUserBottomState

    fun addNewUser(
        name: String,
        email: String,
        gender: String
    ) {
        when {
            name.isEmpty() -> {
                _addUserBottomState.postValue(AddUserBottomViewState(nameError = true))
            }
            (email.isEmpty()) -> {
                _addUserBottomState.postValue(AddUserBottomViewState(emailError = AddUserBottomViewState.EmailErrorType.EMPTY_EMAIL))
            }
            (!isEmailValid(email)) -> {
                _addUserBottomState.postValue(AddUserBottomViewState(emailError = AddUserBottomViewState.EmailErrorType.INVALID_EMAIL))
            }
            else -> {
                _addUserBottomState.postValue(AddUserBottomViewState(showLoading = true))
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
            _addUserBottomState.value = AddUserBottomViewState(
                id = userModel.id,
                name = userModel.name
            )
        }
    }

    fun publishAddUserErrorViewState(
        apiResponse: ApiResponse.Error<UserModel>
    ) {
        _addUserBottomState.value = AddUserBottomViewState(showError = true, errorMessage = apiResponse.message ?: DEFAULT_STRING)
    }
}