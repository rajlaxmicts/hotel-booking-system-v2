package com.ihg.hotelbooking.service;

import com.ihg.hotelbooking.entity.Hotel;
import com.ihg.hotelbooking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    
    @Autowired
    private HotelRepository hotelRepository;
    
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    
    public Optional<Hotel> getHotelById(Long id) {
        return hotelRepository.findById(id);
    }
    
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }
    
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
    
    public List<Hotel> searchHotelsByLocation(String location) {
        return hotelRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public List<Hotel> searchHotelsByName(String name) {
        return hotelRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Hotel> getHotelsByMaxPrice(BigDecimal maxPrice) {
        return hotelRepository.findByPricePerNightLessThanEqual(maxPrice);
    }
    
    public List<Hotel> getHotelsByMinRating(Integer minRating) {
        return hotelRepository.findByRatingGreaterThanEqual(minRating);
    }
    
    public List<Hotel> getHotelsWithAvailableRooms(Integer rooms) {
        return hotelRepository.findByAvailableRoomsGreaterThanEqual(rooms);
    }
    
    public List<Hotel> searchHotelsWithFilters(String location, BigDecimal maxPrice, 
                                              Integer minRating, Integer rooms) {
        return hotelRepository.findHotelsWithFilters(location, maxPrice, minRating, rooms);
    }
    
    public boolean isHotelAvailable(Long hotelId, Integer roomsNeeded) {
        Optional<Hotel> hotel = getHotelById(hotelId);
        return hotel.isPresent() && hotel.get().getAvailableRooms() >= roomsNeeded;
    }
    
    public void updateAvailableRooms(Long hotelId, Integer roomsToReduce) {
        Optional<Hotel> hotelOpt = getHotelById(hotelId);
        if (hotelOpt.isPresent()) {
            Hotel hotel = hotelOpt.get();
            hotel.setAvailableRooms(hotel.getAvailableRooms() - roomsToReduce);
            saveHotel(hotel);
        }
    }
}
