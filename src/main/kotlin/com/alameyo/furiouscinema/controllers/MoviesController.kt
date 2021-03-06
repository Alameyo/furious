package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.http.FuriousHttpClient
import com.alameyo.furiouscinema.inputvalidation.MovieIdValidator
import com.alameyo.furiouscinema.inputvalidation.MovieReviewValidator
import com.alameyo.furiouscinema.repositories.MovieRepository
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MoviesController : FuriousController {
    @Autowired
    lateinit var furiousHttpClient: FuriousHttpClient

    @Autowired
    lateinit var movieRepository: MovieRepository

    @Autowired
    lateinit var movieReviewValidator: MovieReviewValidator

    @Autowired
    lateinit var movieIdValidator: MovieIdValidator

    @GetMapping("/furious/movie/details/{movieId}")
    fun getMovieDetails(@PathVariable movieId: String): ResponseEntity<Any> {
        return when {
            validateInput(movieId, movieIdValidator) -> ResponseEntity<Any>(BAD_REQUEST)
            else -> ResponseEntity(furiousHttpClient.fetchMovieDetails(movieId), OK)
        }
    }

    @PostMapping("/furious/movie/review/{movieId}")
    fun reviewMovie(@PathVariable movieId: String, @RequestBody body: String): ResponseEntity<*> {
        return when {
            validateInput(movieId, movieIdValidator) -> ResponseEntity<Any>(BAD_REQUEST)
            validateInput(body, movieReviewValidator) -> ResponseEntity<Any>(BAD_REQUEST)
            else -> {
                val reviewDocument = movieRepository.createMovieReviewDocument(movieId, body.asJsonObject())
                movieRepository.commitReview(reviewDocument)
                ResponseEntity<Any>(CREATED)
            }
        }
    }

    @GetMapping("/furious/movie/review/{movieId}")
    fun review(@PathVariable movieId: String): ResponseEntity<List<Document>> {
        return when {
            validateInput(movieId, movieIdValidator) -> ResponseEntity(BAD_REQUEST)
            else -> ResponseEntity<List<Document>>(movieRepository.getReviews(movieId), OK)
        }
    }

    @GetMapping("/furious/movie/reviews")
    fun reviews() = ResponseEntity<List<Document>>(movieRepository.getReviews(), OK)

    @GetMapping("/furious/movie/reviews/average/{movieId}")
    fun averageRating(@PathVariable movieId: String): ResponseEntity<Double> {
        return when {
            validateInput(movieId, movieIdValidator) -> ResponseEntity(BAD_REQUEST)
            else -> when (val rating = movieRepository.getAverageRatingForMovie(movieId)) {
                -1.0 -> ResponseEntity(NOT_FOUND)
                else -> ResponseEntity(rating, OK)
            }
        }
    }
}
