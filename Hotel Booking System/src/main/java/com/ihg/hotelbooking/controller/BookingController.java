package com.ihg.hotelbooking.controller;

import com.ihg.hotelbooking.entity.Booking;
import com.ihg.hotelbooking.entity.Hotel;
import com.ihg.hotelbooking.service.BookingService;
import com.ihg.hotelbooking.service.HotelService;
import com.ihg.hotelbooking.entity.User;
import com.ihg.hotelbooking.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private HotelService hotelService;
    
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        logger.debug("Creating booking request: {}", bookingRequest);
        
        try {
            // Validate hotel exists
            logger.debug("Validating hotel with ID: {}", bookingRequest.getHotelId());
            Optional<Hotel> hotel = hotelService.getHotelById(bookingRequest.getHotelId());
            if (hotel.isEmpty()) {
                logger.warn("Hotel not found with ID: {}", bookingRequest.getHotelId());
                return ResponseEntity.badRequest().body("Hotel not found");
            }
            
            // Validate dates
            logger.debug("Validating dates - CheckIn: {}, CheckOut: {}", 
                        bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());
            if (bookingRequest.getCheckInDate().isAfter(bookingRequest.getCheckOutDate())) {
                logger.warn("Invalid date range - CheckIn after CheckOut");
                return ResponseEntity.badRequest().body("Check-in date must be before check-out date");
            }
            
            if (bookingRequest.getCheckInDate().isBefore(LocalDate.now())) {
                logger.warn("Check-in date is in the past: {}", bookingRequest.getCheckInDate());
                return ResponseEntity.badRequest().body("Check-in date must be in the future");
            }
            
            // Check room availability
            logger.debug("Checking room availability for hotel: {}, rooms: {}", 
                        bookingRequest.getHotelId(), bookingRequest.getNumberOfRooms());
            boolean isAvailable = bookingService.isRoomAvailable(
                bookingRequest.getHotelId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getNumberOfRooms()
            );
            
            if (!isAvailable) {
                logger.warn("Rooms not available for booking request");
                return ResponseEntity.badRequest().body("Requested rooms are not available for the selected dates");
            }
            
            // Create booking
            logger.debug("Creating booking entity");
            Booking booking = new Booking();
            booking.setGuestName(bookingRequest.getGuestName());
            booking.setEmail(bookingRequest.getEmail());
            booking.setPhoneNumber(bookingRequest.getPhoneNumber());
            booking.setCheckInDate(bookingRequest.getCheckInDate());
            booking.setCheckOutDate(bookingRequest.getCheckOutDate());
            booking.setNumberOfGuests(bookingRequest.getNumberOfGuests());
            booking.setNumberOfRooms(bookingRequest.getNumberOfRooms());
            booking.setSpecialRequests(bookingRequest.getSpecialRequests());
            booking.setHotel(hotel.get());
            // Link booking to authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                userService.findByUsername(username).ifPresent(booking::setUser);
            }
            logger.debug("Saving booking to database");
            Booking savedBooking = bookingService.saveBooking(booking);
            logger.info("Successfully created booking with ID: {}", savedBooking.getId());
            return ResponseEntity.ok(savedBooking);
            
        } catch (Exception e) {
            logger.error("Error creating booking: ", e);
            return ResponseEntity.badRequest().body("Error creating booking: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @Valid @RequestBody Booking booking) {
        Optional<Booking> existingBooking = bookingService.getBookingById(id);
        if (existingBooking.isPresent()) {
            booking.setId(id);
            Booking updatedBooking = bookingService.saveBooking(booking);
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) {
            bookingService.deleteBooking(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        Booking cancelledBooking = bookingService.cancelBooking(id);
        if (cancelledBooking != null) {
            return ResponseEntity.ok(cancelledBooking);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Booking>> getBookingsByEmail(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByEmail(email);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/guest/{guestName}")
    public ResponseEntity<List<Booking>> getBookingsByGuestName(@PathVariable String guestName) {
        List<Booking> bookings = bookingService.getBookingsByGuestName(guestName);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable Booking.BookingStatus status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Booking>> getBookingsByHotel(@PathVariable Long hotelId) {
        List<Booking> bookings = bookingService.getBookingsByHotel(hotelId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/checkin/{date}")
    public ResponseEntity<List<Booking>> getBookingsByCheckInDate(@PathVariable LocalDate date) {
        List<Booking> bookings = bookingService.getBookingsByCheckInDate(date);
        return ResponseEntity.ok(bookings);
    }
    
    // DTO class for booking requests
    public static class BookingRequest {
        private String guestName;
        private String email;
        private String phoneNumber;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private Integer numberOfGuests;
        private Integer numberOfRooms;
        private String specialRequests;
        private Long hotelId;
        
        // Getters and Setters
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        public LocalDate getCheckInDate() { return checkInDate; }
        public void setCheckInDate(LocalDate checkInDate) { this.checkInDate = checkInDate; }
        
        public LocalDate getCheckOutDate() { return checkOutDate; }
        public void setCheckOutDate(LocalDate checkOutDate) { this.checkOutDate = checkOutDate; }
        
        public Integer getNumberOfGuests() { return numberOfGuests; }
        public void setNumberOfGuests(Integer numberOfGuests) { this.numberOfGuests = numberOfGuests; }
        
        public Integer getNumberOfRooms() { return numberOfRooms; }
        public void setNumberOfRooms(Integer numberOfRooms) { this.numberOfRooms = numberOfRooms; }
        
        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
        
        public Long getHotelId() { return hotelId; }
        public void setHotelId(Long hotelId) { this.hotelId = hotelId; }
        
        @Override
        public String toString() {
            return "BookingRequest{" +
                    "guestName='" + guestName + '\'' +
                    ", email='" + email + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", checkInDate=" + checkInDate +
                    ", checkOutDate=" + checkOutDate +
                    ", numberOfGuests=" + numberOfGuests +
                    ", numberOfRooms=" + numberOfRooms +
                    ", hotelId=" + hotelId +
                    '}';
        }
    }
}
