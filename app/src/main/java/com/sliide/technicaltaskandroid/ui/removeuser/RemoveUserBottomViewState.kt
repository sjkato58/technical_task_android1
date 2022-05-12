package com.sliide.technicaltaskandroid.ui.removeuser

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class RemoveUserBottomViewState(
    val id: Int = DEFAULT_INTEGER,
    val name: String= DEFAULT_STRING,
    val showLoading: Boolean = false,
    val showError: Boolean = false,
    val errorMessage: String= DEFAULT_STRING
)