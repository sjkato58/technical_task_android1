package com.sliide.technicaltaskandroid.ui.userlist

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class UserListViewState(
    val id : Int = -DEFAULT_INTEGER,
    val name: String = DEFAULT_STRING,
    val email: String = DEFAULT_STRING,
    val gender: String = DEFAULT_STRING,
    val isLoading: Boolean = false,
    val isFilter: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String = DEFAULT_STRING,
)