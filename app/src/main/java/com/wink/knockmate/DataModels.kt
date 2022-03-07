package com.wink.knockmate

data class UserModel(
    var id: String? = null,
    var nickname: String? = null,
    var user: Boolean = false,
    var email: String? = null,
    var sequence: Int? = null,
    var invite: Boolean = false,
    var searchAble: Boolean = true,
    var follow: Int = 0,
    var isFav: Int = 0,
    var members: Int = 1,
    var invited: Boolean = false,
    var accepted: Int = 0,
)