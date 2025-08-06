package com.ihg.hotelbooking.service;

import com.ihg.hotelbooking.entity.Booking;
import com.ihg.hotelbooking.entity.Hotel;
import com.ihg.hotelbooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private HotelService hotelService;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public Booking saveBooking(Booking booking) {
        // Calculate total price based on nights and room price
        if (booking.getTotalPrice() == null) {
            calculateTotalPrice(booking);
        }
        
        // No need to update hotel room availability here - 
        // availability is calculated dynamically based on bookings
        
        return bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByEmailIgnoreCase(email);
    }
    
    public List<Booking> getBookingsByGuestName(String guestName) {
        return bookingRepository.findByGuestNameContainingIgnoreCase(guestName);
    }
    
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    
    public List<Booking> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }
    
    public List<Booking> getBookingsByCheckInDate(LocalDate date) {
        return bookingRepository.findByCheckInDate(date);
    }
    
    public List<Booking> getBookingsByCheckOutDate(LocalDate date) {
        return bookingRepository.findByCheckOutDate(date);
    }
    
    public List<Booking> getBookingsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findBookingsBetweenDates(startDate, endDate);
    }
    
    public boolean isRoomAvailable(Long hotelId, LocalDate checkIn, LocalDate checkOut, Integer roomsNeeded) {
        // Check if hotel has enough rooms
        if (!hotelService.isHotelAvailable(hotelId, roomsNeeded)) {
            return false;
        }
        
        // Check for overlapping room bookings
        Long overlappingRooms = bookingRepository.countOverlappingRooms(hotelId, checkIn, checkOut);
        Optional<Hotel> hotel = hotelService.getHotelById(hotelId);
        
        if (hotel.isPresent()) {
            return (hotel.get().getAvailableRooms() - overlappingRooms.intValue()) >= roomsNeeded;
        }
        
        return false;
    }
    
    public Booking cancelBooking(Long bookingId) {
        Optional<Booking> bookingOpt = getBookingById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            
            // No need to restore room availability since we don't reduce it anymore
            // Room availability is calculated dynamically based on confirmed bookings
            
            return saveBooking(booking);
        }
        return null;
    }
    
    private void calculateTotalPrice(Booking booking) {
        if (booking.getHotel() != null && booking.getCheckInDate() != null && booking.getCheckOutDate() != null) {
            long nights = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
            BigDecimal totalPrice = booking.getHotel().getPricePerNight()
                    .multiply(BigDecimal.valueOf(nights))
                    .multiply(BigDecimal.valueOf(booking.getNumberOfRooms()));
            booking.setTotalPrice(totalPrice);
        }
    }
}
