package com.divine.journey.database.source

import com.divine.journey.database.dao.JourneyDao
import com.divine.journey.database.mapper.PostsMapper
import com.divine.journey.di.qualifiers.IO
import com.divine.journey.posts.data.model.Post
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LocalDataSourceImpl @Inject constructor(
    private val dao: JourneyDao,
    private val postsMapper: PostsMapper,
    @IO private val context: CoroutineContext
) : LocalDataSource {

    override suspend fun getPosts() = withContext(context) {
        val postEntity = dao.getPostsFromDatabase()
        if (postEntity != null)
            postsMapper.to(postEntity)
        else
            null
    }

    override suspend fun savePosts(posts: List<Post>) {
        withContext(context) {
            val postList = postsMapper.from(posts)
            dao.savePostsToDatabase(postList)
        }
    }
}