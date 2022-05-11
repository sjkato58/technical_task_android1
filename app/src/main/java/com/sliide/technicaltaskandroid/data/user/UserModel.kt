package com.sliide.technicaltaskandroid.data.user

import com.sliide.technicaltaskandroid.DEFAULT_INTEGER
import com.sliide.technicaltaskandroid.DEFAULT_STRING

data class UserModel(
    val id: Int = DEFAULT_INTEGER,
    val name: String = DEFAULT_STRING,
    val email: String = DEFAULT_STRING,
    val gender: String = DEFAULT_STRING,
    val status: String = DEFAULT_STRING
)