package com.seroter.MicroserviceBooking_app.controller;

import com.seroter.MicroserviceBooking_app.model.mongo.MovieCatalog;
import com.seroter.MicroserviceBooking_app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping
    public List<MovieCatalog> getAllMovies() {
        return movieService.getAllMovies();
    }
    
    @GetMapping("/genre/{genre}")
    public List<MovieCatalog> getMoviesByGenre(@PathVariable String genre) {
        return movieService.getMoviesByGenre(genre);
    }
    
    @GetMapping("/search")
    public List<MovieCatalog> searchMovies(@RequestParam String title) {
        return movieService.searchMovies(title);
    }
    
    @PostMapping
    public MovieCatalog addMovie(@RequestBody MovieCatalog movie) {
        return movieService.saveMovie(movie);
    }
}