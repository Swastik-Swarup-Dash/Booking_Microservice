package com.seroter.MicroserviceBooking_app.service;
import com.seroter.MicroserviceBooking_app.model.mongo.MovieCatalog;
import com.seroter.MicroserviceBooking_app.repository.mongo.MovieCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
public class MovieService {
      @Autowired
      private MovieCatalogRepository movieCatalogRepository;

      @Cacheable("movies")
      public List<MovieCatalog> getAllMovies(){
          return movieCatalogRepository.findAll();
      }

      @Cacheable("moviesByGenre")
      public List<MovieCatalog> getMoviesByGenre(String genre){
          return movieCatalogRepository.findByGenre(genre);
      }
      @Cacheable("searchMovies")
      public List<MovieCatalog> searchMovies(String movie){
          return movieCatalogRepository.findByTitleContainingIgnoreCase(movie);
      }

    public MovieCatalog saveMovie(MovieCatalog movie) {
        return movieCatalogRepository.save(movie);
    }
}
