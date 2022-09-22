package com.my.githubapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Github(
    var username: String,
    var name: String,
    var avatar: String,
    var company: String,
    var location: String,
    var repository: Int,
    var follower: Int,
    var following:Int
) : Parcelable