package com.ihg.hotelbooking.service;

import com.ihg.hotelbooking.entity.Booking;
import com.ihg.hotelbooking.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChatbotService {
    
    @Autowired
    private HotelService hotelService;
    
    @Autowired
    private BookingService bookingService;
    
    public Map<String, Object> processUserQuery(String query) {
        Map<String, Object> response = new HashMap<>();
        
        query = query.toLowerCase().trim();
        
        try {
            if (query.contains("search") || query.contains("find") || query.contains("hotel")) {
                return handleHotelSearch(query);
            } else if (query.contains("book") || query.contains("reserve")) {
                return handleBookingIntent(query);
            } else if (query.contains("cancel")) {
                return handleCancellationIntent(query);
            } else if (query.contains("my booking") || query.contains("check booking")) {
                return handleBookingInquiry(query);
            } else if (query.contains("help") || query.contains("what can you do")) {
                return getHelpResponse();
            } else {
                response.put("message", "I didn't understand that. You can ask me to:\n" +
                        "- Search for hotels (e.g., 'Find hotels in Paris')\n" +
                        "- Make a booking (e.g., 'Book a room')\n" +
                        "- Check your bookings (e.g., 'My bookings for john@email.com')\n" +
                        "- Cancel a booking (e.g., 'Cancel booking 123')\n" +
                        "- Get help (e.g., 'What can you do?')");
                response.put("success", false);
            }
        } catch (Exception e) {
            response.put("message", "Sorry, I encountered an error processing your request. Please try again.");
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
    
    private Map<String, Object> handleHotelSearch(String query) {
        Map<String, Object> response = new HashMap<>();
        
        // Extract location
        String location = extractLocation(query);
        
        // Extract price range
        BigDecimal maxPrice = extractMaxPrice(query);
        
        // Extract rating
        Integer minRating = extractMinRating(query);
        
        // Extract number of rooms
        Integer rooms = extractNumberOfRooms(query);
        
        List<Hotel> hotels;
        
        if (location != null || maxPrice != null || minRating != null || rooms != null) {
            hotels = hotelService.searchHotelsWithFilters(location, maxPrice, minRating, rooms);
        } else {
            hotels = hotelService.getAllHotels();
        }
        
        if (hotels.isEmpty()) {
            response.put("message", "Sorry, I couldn't find any hotels matching your criteria.");
            response.put("success", false);
        } else {
            StringBuilder message = new StringBuilder("I found " + hotels.size() + " hotel(s):\n\n");
            for (Hotel hotel : hotels) {
                message.append("🏨 ").append(hotel.getName()).append("\n");
                message.append("📍 ").append(hotel.getLocation()).append("\n");
                message.append("⭐ ").append(hotel.getRating()).append("/5 stars\n");
                message.append("💰 $").append(hotel.getPricePerNight()).append(" per night\n");
                message.append("🛏️ ").append(hotel.getAvailableRooms()).append(" rooms available\n");
                if (hotel.getDescription() != null) {
                    message.append("ℹ️ ").append(hotel.getDescription()).append("\n");
                }
                message.append("\n");
            }
            response.put("message", message.toString());
            response.put("hotels", hotels);
            response.put("success", true);
        }
        
        return response;
    }
    
    private Map<String, Object> handleBookingIntent(String query) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "I'd be happy to help you make a booking! Please provide the following information:\n" +
                "- Hotel ID or name\n" +
                "- Your name\n" +
                "- Email address\n" +
                "- Phone number\n" +
                "- Check-in date (YYYY-MM-DD)\n" +
                "- Check-out date (YYYY-MM-DD)\n" +
                "- Number of guests\n" +
                "- Number of rooms\n\n" +
                "You can use our booking API endpoint: POST /bookings");
        response.put("action", "booking_form");
        response.put("success", true);
        return response;
    }
    
    private Map<String, Object> handleCancellationIntent(String query) {
        Map<String, Object> response = new HashMap<>();
        
        // Extract booking ID
        String bookingId = extractBookingId(query);
        
        if (bookingId != null) {
            try {
                Long id = Long.parseLong(bookingId);
                Booking cancelledBooking = bookingService.cancelBooking(id);
                if (cancelledBooking != null) {
                    response.put("message", "Booking #" + id + " has been successfully cancelled.");
                    response.put("booking", cancelledBooking);
                    response.put("success", true);
                } else {
                    response.put("message", "Booking #" + id + " not found.");
                    response.put("success", false);
                }
            } catch (NumberFormatException e) {
                response.put("message", "Invalid booking ID format.");
                response.put("success", false);
            }
        } else {
            response.put("message", "Please provide a booking ID to cancel. Example: 'Cancel booking 123'");
            response.put("success", false);
        }
        
        return response;
    }
    
    private Map<String, Object> handleBookingInquiry(String query) {
        Map<String, Object> response = new HashMap<>();
        
        // Extract email
        String email = extractEmail(query);
        
        if (email != null) {
            List<Booking> bookings = bookingService.getBookingsByEmail(email);
            if (bookings.isEmpty()) {
                response.put("message", "No bookings found for " + email);
                response.put("success", false);
            } else {
                StringBuilder message = new StringBuilder("Found " + bookings.size() + " booking(s) for " + email + ":\n\n");
                for (Booking booking : bookings) {
                    message.append("🆔 Booking #").append(booking.getId()).append("\n");
                    message.append("🏨 ").append(booking.getHotel().getName()).append("\n");
                    message.append("📅 ").append(booking.getCheckInDate()).append(" to ").append(booking.getCheckOutDate()).append("\n");
                    message.append("👥 ").append(booking.getNumberOfGuests()).append(" guests, ").append(booking.getNumberOfRooms()).append(" room(s)\n");
                    message.append("💰 Total: $").append(booking.getTotalPrice()).append("\n");
                    message.append("📊 Status: ").append(booking.getStatus()).append("\n\n");
                }
                response.put("message", message.toString());
                response.put("bookings", bookings);
                response.put("success", true);
            }
        } else {
            response.put("message", "Please provide an email address to check bookings. Example: 'My bookings for john@email.com'");
            response.put("success", false);
        }
        
        return response;
    }
    
    private Map<String, Object> getHelpResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "🤖 Hotel Booking Chatbot Help\n\n" +
                "I can help you with:\n\n" +
                "🔍 **Search Hotels:**\n" +
                "- 'Find hotels in Paris'\n" +
                "- 'Search hotels under $200'\n" +
                "- 'Hotels with 4+ stars'\n" +
                "- 'Find 2 rooms in London'\n\n" +
                "📝 **Make Bookings:**\n" +
                "- 'Book a room' (I'll guide you through the process)\n\n" +
                "📋 **Check Bookings:**\n" +
                "- 'My bookings for john@email.com'\n" +
                "- 'Check booking for Jane Smith'\n\n" +
                "❌ **Cancel Bookings:**\n" +
                "- 'Cancel booking 123'\n\n" +
                "💡 **Tips:**\n" +
                "- Be specific with your requests\n" +
                "- Include dates, locations, and preferences\n" +
                "- Use email addresses for booking inquiries");
        response.put("success", true);
        return response;
    }
    
    // Helper methods for extracting information from queries
    private String extractLocation(String query) {
        Pattern locationPattern = Pattern.compile("in ([a-zA-Z\\s]+)");
        Matcher matcher = locationPattern.matcher(query);
        return matcher.find() ? matcher.group(1).trim() : null;
    }
    
    private BigDecimal extractMaxPrice(String query) {
        Pattern pricePattern = Pattern.compile("under \\$?(\\d+)|less than \\$?(\\d+)|below \\$?(\\d+)|max \\$?(\\d+)");
        Matcher matcher = pricePattern.matcher(query);
        if (matcher.find()) {
            String priceStr = matcher.group(1) != null ? matcher.group(1) : 
                             matcher.group(2) != null ? matcher.group(2) : 
                             matcher.group(3) != null ? matcher.group(3) : matcher.group(4);
            return new BigDecimal(priceStr);
        }
        return null;
    }
    
    private Integer extractMinRating(String query) {
        Pattern ratingPattern = Pattern.compile("(\\d+)\\+ star|rating (\\d+)|above (\\d+) star");
        Matcher matcher = ratingPattern.matcher(query);
        if (matcher.find()) {
            String ratingStr = matcher.group(1) != null ? matcher.group(1) : 
                              matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
            return Integer.parseInt(ratingStr);
        }
        return null;
    }
    
    private Integer extractNumberOfRooms(String query) {
        Pattern roomPattern = Pattern.compile("(\\d+) room");
        Matcher matcher = roomPattern.matcher(query);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }
    
    private String extractEmail(String query) {
        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
        Matcher matcher = emailPattern.matcher(query);
        return matcher.find() ? matcher.group(1) : null;
    }
    
    private String extractBookingId(String query) {
        Pattern idPattern = Pattern.compile("booking (\\d+)|#(\\d+)");
        Matcher matcher = idPattern.matcher(query);
        return matcher.find() ? (matcher.group(1) != null ? matcher.group(1) : matcher.group(2)) : null;
    }
}
