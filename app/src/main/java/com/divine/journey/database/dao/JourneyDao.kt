package com.divine.journey.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.divine.journey.database.entites.DbPosts

/**
 * Data Access Object (DAO) for interacting with the Journey database.
 *
 * This DAO provides methods for accessing and storing posts.
 */
@Dao
abstract class JourneyDao {

    /**
     * Retrieves all posts from the database.
     *
     * @return A `DbPosts` object containing all posts, or null if the database is empty.
     */
    @Query("SELECT * FROM db_posts")
    abstract fun getPostsFromDatabase(): DbPosts?

    /**
     * Saves posts to the database.
     *
     * If a post with the same ID already exists, it will be replaced.
     *
     * @param posts The `DbPosts` object to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun savePostsToDatabase(posts: DbPosts)
}
