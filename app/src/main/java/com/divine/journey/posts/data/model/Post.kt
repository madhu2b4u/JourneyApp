package com.divine.journey.posts.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Data class representing a post response.
 *
 *
 * @property userId The ID of the user who created the post.
 * @property id The ID of the post.
 * @property title The title of the post.
 * @property body The content of the post.
 */

@Parcelize
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) : Parcelable