package com.divine.journey.posts.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a comment response.
 *
 *
 * @property id The ID of the comment.
 * @property postId The ID of the post that the comment belongs to.
 * @property name The name of the commenter.
 * @property email The email address of the commenter.
 * @property body The content of the comment.
 */
@Parcelize
data class Comment(
    val id: Int,
    val postId: Int,
    val name: String,
    val email: String,
    val body: String
) : Parcelable