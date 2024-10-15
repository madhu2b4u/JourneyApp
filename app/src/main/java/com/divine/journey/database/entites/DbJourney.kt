package com.divine.journey.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_posts")
data class DbPosts(
    @PrimaryKey val id: Int,
    val posts: String
)