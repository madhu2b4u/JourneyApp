package com.divine.journey.database.mapper

import com.divine.journey.database.entites.DbPosts
import com.divine.journey.posts.data.model.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

/**
 * Mapper class for converting between `DbPosts` and `List<Post>`.
 *
 * This class uses Gson to serialize and deserialize the list of posts
 * to and from a JSON string stored in the `DbPosts` object.
 */
class PostsMapper @Inject constructor(val gson: Gson) : Mapper<DbPosts, List<Post>> {

    /**
     * Converts a list of `Post` objects to a `DbPosts` object.
     *
     * @param e The list of `Post` objects to convert.
     * @return A `DbPosts` object containing the serialized list of posts.
     */
    override fun from(e: List<Post>) = DbPosts(1, gson.toJson(e))

    /**
     * Converts a `DbPosts` object to a list of `Post` objects.
     *
     * @param t The `DbPosts` object to convert.
     * @return A list of `Post` objects deserialized from the JSON string
     *         stored in the `DbPosts` object.
     */
    override fun to(t: DbPosts): List<Post> {
        return gson.fromJson(
            t.posts,
            TypeToken.getParameterized(ArrayList::class.java, Post::class.java).type
        )
    }
}