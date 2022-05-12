package com.sliide.technicaltaskandroid.ui.removeuser

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class RemoveUserViewState(
    val id: Int = DEFAULT_INTEGER,
    val showLoading: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String= DEFAULT_STRING
)