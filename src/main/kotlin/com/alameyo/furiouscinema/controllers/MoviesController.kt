package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.asJsonObject
import com.alameyo.furiouscinema.http.FuriousHttpClient
import com.alameyo.furiouscinema.repositories.MovieRepository
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MoviesController {
    @Autowired
    lateinit var furiousHttpClient: FuriousHttpClient
    @Autowired
    lateinit var movieRepository: MovieRepository

    @GetMapping("/furious/movie/details/{movieId}")
    fun getMovieDetails(@PathVariable movieId: String): ResponseEntity<Any> {
        return ResponseEntity(furiousHttpClient.fetchMovieDetails(movieId), OK)
    }

    @PostMapping("/furious/movie/review/{movieId}")
    fun reviewMovie(@PathVariable movieId: String, @RequestBody body: String): HttpStatus {
        movieRepository.reviewMovie(movieId, body.asJsonObject())
        return CREATED
    }

    @GetMapping("/furious/movie/review/{movieId}")
    fun review(@PathVariable movieId: String) = ResponseEntity<List<Document>>(movieRepository.getReviews(movieId), OK)

    @GetMapping("/furious/movie/reviews")
    fun reviews() = ResponseEntity<List<Document>>(movieRepository.getReviews(), OK)

    @GetMapping("/furious/movie/reviews/average/{movieId}")
    fun averageRating(@PathVariable movieId: String): ResponseEntity<Double> {
        return when (val rating = movieRepository.getAverageRatingForMovie(movieId)) {
            -1.0 -> ResponseEntity(NOT_FOUND)
            else -> ResponseEntity(rating, OK)
        }
    }
}
