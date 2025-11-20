package com.seroter.MicroserviceBooking_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/databases")
    public Map<String, String> checkDatabases() {
        Map<String, String> status = new HashMap<>();
        
        // PostgreSQL/H2 check
        try (Connection conn = dataSource.getConnection()) {
            status.put("postgresql", "Connected - " + conn.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            status.put("postgresql", "Failed: " + e.getMessage());
        }
        
        // MongoDB check
        try {
            mongoTemplate.getDb().getName();
            status.put("mongodb", "Connected - " + mongoTemplate.getDb().getName());
        } catch (Exception e) {
            status.put("mongodb", "Failed: " + e.getMessage());
        }
        
        // Redis check
        try {
            redisTemplate.opsForValue().set("health-check", "test");
            status.put("redis", "Connected");
        } catch (Exception e) {
            status.put("redis", "Failed: " + e.getMessage());
        }
        
        return status;
    }
}