package com.ihg.hotelbooking.repository;

import com.ihg.hotelbooking.entity.Booking;
import com.ihg.hotelbooking.entity.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByEmailIgnoreCase(String email);
    
    List<Booking> findByGuestNameContainingIgnoreCase(String guestName);
    
    List<Booking> findByStatus(BookingStatus status);
    
    List<Booking> findByHotelId(Long hotelId);
    
    @Query("SELECT b FROM Booking b WHERE b.checkInDate = :date")
    List<Booking> findByCheckInDate(@Param("date") LocalDate date);
    
    @Query("SELECT b FROM Booking b WHERE b.checkOutDate = :date")
    List<Booking> findByCheckOutDate(@Param("date") LocalDate date);
    
    @Query("SELECT b FROM Booking b WHERE b.checkInDate BETWEEN :startDate AND :endDate")
    List<Booking> findBookingsBetweenDates(@Param("startDate") LocalDate startDate, 
                                          @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COALESCE(SUM(b.numberOfRooms), 0) FROM Booking b WHERE b.hotel.id = :hotelId AND " +
           "((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)) AND " +
           "b.status = com.ihg.hotelbooking.entity.Booking$BookingStatus.CONFIRMED")
    Long countOverlappingRooms(@Param("hotelId") Long hotelId,
                              @Param("checkIn") LocalDate checkIn,
                              @Param("checkOut") LocalDate checkOut);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.hotel.id = :hotelId AND " +
           "((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)) AND " +
           "b.status = com.ihg.hotelbooking.entity.Booking$BookingStatus.CONFIRMED")
    Long countOverlappingBookings(@Param("hotelId") Long hotelId,
                                 @Param("checkIn") LocalDate checkIn,
                                 @Param("checkOut") LocalDate checkOut);
}
