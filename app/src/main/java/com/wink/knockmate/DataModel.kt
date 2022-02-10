package com.wink.knockmate

data class UserModel(
    var id:String? = null,
    var nickname: String? = null,
    var searchAble: Boolean = true,
    var follow: Boolean = false,
    var email: String? = null,
)
