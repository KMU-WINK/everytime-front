package com.wink.knockmate

data class UserModel(
    var id: String? = null,
    var nickname: String? = null,
    var user: Boolean = false,
    var email: String? = null,
    var invite: Boolean = false,
)

data class GroupModel(
    var id: String? = null,
    var nickname: String? = null,
    var invite: Boolean = false,
)