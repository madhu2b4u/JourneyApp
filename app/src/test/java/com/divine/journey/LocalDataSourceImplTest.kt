package com.divine.journey

import com.divine.journey.database.dao.JourneyDao
import com.divine.journey.database.entites.DbPosts
import com.divine.journey.database.mapper.PostsMapper
import com.divine.journey.database.source.LocalDataSourceImpl
import com.divine.journey.posts.data.model.Post
import io.mockk.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {

    private lateinit var localDataSource: LocalDataSourceImpl
    private val dao = mockk<JourneyDao>()
    private val postsMapper = mockk<PostsMapper>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        localDataSource = LocalDataSourceImpl(dao, postsMapper, testDispatcher)
    }

    // Test for getPosts()
    @Test
    fun `getPosts returns mapped posts when posts are available in the database`() = runTest(testDispatcher) {
        // Arrange
        val dbPosts = DbPosts(id = 1, posts = "Title: Body")
        val post = listOf(Post(1, 1, "Title", "Body"))
        
        coEvery { dao.getPostsFromDatabase() } returns dbPosts
        coEvery { postsMapper.to(dbPosts) } returns post

        // Act
        val result = localDataSource.getPosts()

        // Assert
        assertNotNull(result)
        assertEquals(post, result)
        coVerify(exactly = 1) { dao.getPostsFromDatabase() }
        coVerify(exactly = 1) { postsMapper.to(dbPosts) }
    }

    @Test
    fun `getPosts returns null when no posts are available in the database`() = runTest(testDispatcher) {
        // Arrange
        coEvery { dao.getPostsFromDatabase() } returns null

        // Act
        val result = localDataSource.getPosts()

        // Assert
        assertNull(result)
        coVerify(exactly = 1) { dao.getPostsFromDatabase() }
        coVerify(exactly = 0) { postsMapper.to(any()) }
    }

    // Test for savePosts()
    @Test
    fun `savePosts saves posts to the database using the mapper`() = runTest(testDispatcher) {
        // Arrange
        val posts = listOf(Post(1, 1, "Title", "Body"))
        val postEntities = DbPosts(id = 1, posts = "Title: Body")

        coEvery { postsMapper.from(posts) } returns postEntities
        coEvery { dao.savePostsToDatabase(postEntities) } just Runs

        // Act
        localDataSource.savePosts(posts)

        // Assert
        coVerify(exactly = 1) { postsMapper.from(posts) }
        coVerify(exactly = 1) { dao.savePostsToDatabase(postEntities) }
    }
}
