package com.sliide.technicaltaskandroid.ui.adduser

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class AddUserViewState(
    val id: Int= DEFAULT_INTEGER,
    val name: String= DEFAULT_STRING,
    val showLoading: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String= DEFAULT_STRING,
    val nameError: Boolean = false,
    val emailError: EmailErrorType = EmailErrorType.DEFAULT,
    val genderError: Boolean = false
) {
    enum class EmailErrorType {
        DEFAULT,
        EMPTY_EMAIL,
        INVALID_EMAIL
    }
}