package com.alameyo.furiouscinema.controllers

import com.alameyo.furiouscinema.http.FuriousHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MoviesController {
    @Autowired
    lateinit var furiousHttpClient: FuriousHttpClient

    @GetMapping("/furious/movie/details/{movieId}")
    fun getMovieDetails(@PathVariable movieId: String): ResponseEntity<Any> {
        return ResponseEntity(furiousHttpClient.fetchMovieDetails(movieId), OK)
    }
}