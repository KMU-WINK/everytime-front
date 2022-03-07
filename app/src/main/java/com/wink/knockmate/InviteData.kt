package com.wink.knockmate

class InviteData {
    companion object {
        var nowChangeUsers = mutableListOf<UserModel>()
        var nowChangeGroups = mutableListOf<UserModel>()
        var fixUsers = mutableListOf<UserModel>()
        var fixGroups = mutableListOf<UserModel>()
    }
}