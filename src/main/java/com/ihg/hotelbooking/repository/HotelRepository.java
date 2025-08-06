package com.ihg.hotelbooking.repository;

import com.ihg.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    List<Hotel> findByLocationContainingIgnoreCase(String location);
    
    List<Hotel> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT h FROM Hotel h WHERE h.pricePerNight <= :maxPrice ORDER BY h.pricePerNight ASC")
    List<Hotel> findByPricePerNightLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT h FROM Hotel h WHERE h.availableRooms >= :rooms ORDER BY h.rating DESC")
    List<Hotel> findByAvailableRoomsGreaterThanEqual(@Param("rooms") Integer rooms);
    
    @Query("SELECT h FROM Hotel h WHERE h.rating >= :rating ORDER BY h.rating DESC")
    List<Hotel> findByRatingGreaterThanEqual(@Param("rating") Integer rating);
    
    @Query("SELECT h FROM Hotel h WHERE " +
           "(:location IS NULL OR LOWER(h.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:maxPrice IS NULL OR h.pricePerNight <= :maxPrice) AND " +
           "(:minRating IS NULL OR h.rating >= :minRating) AND " +
           "(:rooms IS NULL OR h.availableRooms >= :rooms) " +
           "ORDER BY h.rating DESC, h.pricePerNight ASC")
    List<Hotel> findHotelsWithFilters(@Param("location") String location,
                                     @Param("maxPrice") BigDecimal maxPrice,
                                     @Param("minRating") Integer minRating,
                                     @Param("rooms") Integer rooms);
}
