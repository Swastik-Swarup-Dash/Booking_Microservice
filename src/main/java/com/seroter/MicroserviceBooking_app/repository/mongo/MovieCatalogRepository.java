package com.seroter.MicroserviceBooking_app.repository.mongo;

import com.seroter.MicroserviceBooking_app.model.mongo.MovieCatalog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieCatalogRepository extends MongoRepository<MovieCatalog, String> {
    List<MovieCatalog> findByGenre(String genre);
    List<MovieCatalog> findByLanguage(String language);
    List<MovieCatalog> findByTitleContainingIgnoreCase(String title);
}