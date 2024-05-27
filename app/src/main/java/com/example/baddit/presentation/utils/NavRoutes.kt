package com.example.baddit.presentation.utils

import com.example.baddit.domain.model.posts.PostResponseDTOItem
import kotlinx.serialization.Serializable

@Serializable
object Auth

@Serializable
object Main

@Serializable
object Home

@Serializable
object CreatePost

@Serializable
object CreateTextPost
@Serializable
object CreateMediaPost

@Serializable
object Community

@Serializable
object Setting

@Serializable
object SignUp

@Serializable
object Login

@Serializable
object Verify

object LeftSideBar

@Serializable
object UserSideBar

@Serializable
object Search

@Serializable
data class Profile(
    val username: String
)

@Serializable
data class Post(
    val postId: String
)

@Serializable
data class Comment(
    val darkMode: Boolean,
    val postId: String?,
    val commentId: String?,
    val commentContent: String?
)

@Serializable
data class Editing(
    val darkMode: Boolean,
    val postId: String?,
    val commentId: String?,
    val commentContent: String?
)

@Serializable
data class CommunityDetail(
    val name: String
)

@Serializable
data class EditCommunity(
    val name: String
)

@Serializable
data class AddModerator(
    val name: String
)
