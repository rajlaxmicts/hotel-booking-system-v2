# Hotel Booking API Testing Commands

## Test Hotel Endpoints
# Get all hotels
curl -X GET http://localhost:8081/api/hotels

# Get hotel by ID
curl -X GET http://localhost:8081/api/hotels/1

# Search hotels
curl -X GET "http://localhost:8081/api/hotels/search?location=Paris&maxPrice=300"

## Test Booking Endpoints
# Get all bookings
curl -X GET http://localhost:8081/api/bookings

# Get booking by email
curl -X GET http://localhost:8081/api/bookings/email/test@email.com

# Create booking (POST with JSON)
curl -X POST http://localhost:8081/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "guestName": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+1234567890",
    "checkInDate": "2025-08-15",
    "checkOutDate": "2025-08-20",
    "numberOfGuests": 2,
    "numberOfRooms": 1,
    "hotelId": 1,
    "specialRequests": "Late checkout if possible"
  }'

# Cancel booking
curl -X POST http://localhost:8081/api/bookings/1/cancel

## Test Chatbot
curl -X POST http://localhost:8081/api/chatbot/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Find hotels in Paris"}'

## Health Check
curl -X GET http://localhost:8081/actuator/health
