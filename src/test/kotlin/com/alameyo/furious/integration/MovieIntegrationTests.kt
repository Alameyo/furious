package com.alameyo.furious.integration

import com.alameyo.furious.TestingHttpClient
import com.alameyo.furiouscinema.Application
import com.alameyo.furiouscinema.asJsonArray
import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.repositories.MovieRepository
import com.mongodb.client.MongoDatabase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [Application::class])
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = ["dev"])
@Disabled("Before enabling run application and database, depends also on open movie api")
class MovieIntegrationTests(@Autowired private val database: MongoDatabase) {

    @Autowired
    private lateinit var movieRepository: MovieRepository
    private val testHttpClient = TestingHttpClient()

    private val movieId = "tt0232500"
    private val movieId2 = "tt0322259"

    @Test
    fun `Review movie - should return 201 and be saved to the database`() {
        val movieReview = """
            {
                "rate": 4,
                "comment": "This is test comment"
            }
        """.trim().asJsonObject()

        val sizeOfReviewCollectionBeforePost = movieRepository.getReviews(movieId).size
        val response = testHttpClient.post("/movie/review/$movieId", movieReview)
        assertTrue(response?.statusCode() == 201, "Response was created")
        val sizeOfReviewCollectionAfterPost = movieRepository.getReviews(movieId).size
        assertTrue(sizeOfReviewCollectionBeforePost + 1 == sizeOfReviewCollectionAfterPost, "Collection of reviews increased by 1")
    }

    @Test
    fun `Review movie with invalid rate - should return 400 and not be saved to database`() {
        val movieReview = """
            {
                "rate": -2,
                "comment": "This is test comment"
            }
        """.trim().asJsonObject()

        val sizeOfReviewCollectionBeforePost = movieRepository.getReviews(movieId).size
        val response = testHttpClient.post("/movie/review/$movieId", movieReview)
        assertTrue(response?.statusCode() == 400, "Response was Bad Request")
        val sizeOfReviewCollectionAfterPost = movieRepository.getReviews(movieId).size

        assertTrue(sizeOfReviewCollectionBeforePost == sizeOfReviewCollectionAfterPost, "Collection of reviews didn't change")
    }

    @Test
    fun `Review 2 different movies 6 times - should get all reviews, reviews per movie and average score`() {
        val movieReview = arrayOf(
            """{"rate": 2}""".trim().asJsonObject(),
            """{"rate": 5}""".trim().asJsonObject(),
            """{"rate": 2}""".trim().asJsonObject(),
            """{"rate": 2}""".trim().asJsonObject(),
            """{"rate": 4}""".trim().asJsonObject(),
            """{"rate": 4}""".trim().asJsonObject()
        )
        val expectedAverageForMovie1 = 3.5
        val expectedAverageForMovie2 = 3.0
        val expectedSizeOfCollection1AfterReviews = 2
        val expectedSizeOfCollection2AfterReviews = 4
        val expectedSizeOfCollectionAtTheBeginning = 0

        val sizeOfReviewCollection1BeforePost = testHttpClient.get("/movie/review/$movieId")?.body()?.asJsonArray()?.size()
        val sizeOfReviewCollection2BeforePost = testHttpClient.get("/movie/review/$movieId2")?.body()?.asJsonArray()?.size()
        assertTrue(sizeOfReviewCollection1BeforePost == expectedSizeOfCollectionAtTheBeginning)
        assertTrue(sizeOfReviewCollection2BeforePost == expectedSizeOfCollectionAtTheBeginning)

        for (i in 0..1) {
            testHttpClient.post("/movie/review/$movieId", movieReview[i])
        }
        for (i in 2..5) {
            testHttpClient.post("/movie/review/$movieId2", movieReview[i])
        }

        val sizeOfReviewCollection1AfterPost = testHttpClient.get("/movie/review/$movieId")?.body()?.asJsonArray()?.size()
        val sizeOfReviewCollection2AfterPost = testHttpClient.get("/movie/review/$movieId2")?.body()?.asJsonArray()?.size()

        assertTrue(sizeOfReviewCollection1AfterPost == expectedSizeOfCollection1AfterReviews)
        assertTrue(sizeOfReviewCollection2AfterPost == expectedSizeOfCollection2AfterReviews)

        val averageScoreForMovie1 = testHttpClient.get("/movie/reviews/average/$movieId")?.body()?.toDouble()
        val averageScoreForMovie2 = testHttpClient.get("/movie/reviews/average/$movieId2")?.body()?.toDouble()

        assertTrue(averageScoreForMovie1 == expectedAverageForMovie1)
        assertTrue(averageScoreForMovie2 == expectedAverageForMovie2)
    }

    @Test
    fun `For given set of movie ID's - should return details about movie with status code 200, movie title for ID should match give one`() {
        val movieIds = arrayOf(
            "tt0232500",
            "tt0322259",
            "tt0463985",
            "tt1013752",
            "tt1596343",
            "tt1905041",
            "tt2820852",
            "tt4630562"
        )
        val movieTitles = arrayOf(
            "The Fast and the Furious",
            "2 Fast 2 Furious",
            "The Fast and the Furious: Tokyo Drift",
            "Fast & Furious",
            "Fast Five",
            "Furious 6",
            "Fast & Furious 7",
            "The Fate of the Furious"
        )
        movieIds.forEach {
            val response = testHttpClient.get("/movie/details/$it")
            assertTrue(response?.statusCode() == 200)
            val title = response?.body()?.asJsonObject()?.get("Title")?.asString
            assertTrue(movieTitles.contains(title))
        }
    }

    @AfterEach
    fun cleanUp() {
        val reviewsCollection = database.getCollection("reviews")
        reviewsCollection.drop()
    }
}
