package com.seroter.MicroserviceBooking_app.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "movies")
public class MovieCatalog implements Serializable {
    @Id
    private String id;
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private String language;
    private LocalDate releaseDate;
    private String posterUrl;
    private Double rating;
    private List<String> cast;
}