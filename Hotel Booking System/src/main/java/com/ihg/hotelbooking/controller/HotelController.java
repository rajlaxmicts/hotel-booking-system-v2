package com.ihg.hotelbooking.controller;

import com.ihg.hotelbooking.entity.Hotel;
import com.ihg.hotelbooking.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;
    
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(hotels);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        return hotel.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        Hotel savedHotel = hotelService.saveHotel(hotel);
        return ResponseEntity.ok(savedHotel);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @Valid @RequestBody Hotel hotel) {
        Optional<Hotel> existingHotel = hotelService.getHotelById(id);
        if (existingHotel.isPresent()) {
            hotel.setId(id);
            Hotel updatedHotel = hotelService.saveHotel(hotel);
            return ResponseEntity.ok(updatedHotel);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        Optional<Hotel> hotel = hotelService.getHotelById(id);
        if (hotel.isPresent()) {
            hotelService.deleteHotel(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer rooms) {
        
        List<Hotel> hotels;
        
        if (name != null) {
            hotels = hotelService.searchHotelsByName(name);
        } else if (location != null || maxPrice != null || minRating != null || rooms != null) {
            hotels = hotelService.searchHotelsWithFilters(location, maxPrice, minRating, rooms);
        } else {
            hotels = hotelService.getAllHotels();
        }
        
        return ResponseEntity.ok(hotels);
    }
    
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Hotel>> getHotelsByLocation(@PathVariable String location) {
        List<Hotel> hotels = hotelService.searchHotelsByLocation(location);
        return ResponseEntity.ok(hotels);
    }
    
    @GetMapping("/availability/{hotelId}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long hotelId, 
                                                    @RequestParam Integer rooms) {
        boolean available = hotelService.isHotelAvailable(hotelId, rooms);
        return ResponseEntity.ok(available);
    }
}
