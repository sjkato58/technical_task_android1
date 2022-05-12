package com.sliide.technicaltaskandroid.ui.adduser

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class AddUserViewState(
    val id: Int= DEFAULT_INTEGER,
    val name: String= DEFAULT_STRING,
    val showLoading: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String= DEFAULT_STRING,
    val nameError: Int = DEFAULT_INTEGER,
    val emailError: Int = DEFAULT_INTEGER,
    val genderError: Int = DEFAULT_INTEGER
)