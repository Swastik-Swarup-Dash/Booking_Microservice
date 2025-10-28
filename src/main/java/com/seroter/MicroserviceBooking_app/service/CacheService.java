package com.seroter.MicroserviceBooking_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void cacheShowAvailability(Long showId, Integer availableSeats) {
        String key = "show:availability:" + showId;
        redisTemplate.opsForValue().set(key, availableSeats, 5, TimeUnit.MINUTES);
    }
    
    public Integer getShowAvailability(Long showId) {
        String key = "show:availability:" + showId;
        return (Integer) redisTemplate.opsForValue().get(key);
    }
    
    public void updateSeatAvailability(Long showId, Integer newCount) {
        cacheShowAvailability(showId, newCount);
    }
}