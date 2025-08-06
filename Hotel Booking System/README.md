# 🏨 Hotel Booking Chatbot System

A modern, AI-powered hotel booking system built with Java Spring Boot and featuring an intelligent chatbot interface for natural language interactions.

## 🚀 Features

### 🤖 Intelligent Chatbot
- Natural language processing for hotel searches
- Conversational booking management
- Context-aware responses with rich formatting
- Support for complex queries like "Find 2 rooms in Paris under $300"

### 🏗️ Comprehensive Backend
- **Spring Boot 3.2.0** with Java 17
- **JPA/Hibernate** for data persistence
- **H2 Database** for development (easily configurable for production databases)
- **RESTful APIs** for all hotel and booking operations
- **Validation** and error handling throughout

### 🎨 Modern Web Interface
- **Bootstrap 5** responsive design
- Interactive chatbot interface with typing indicators
- Hotel search and filtering capabilities
- Booking management dashboard
- Mobile-friendly design with smooth animations

### 📊 Core Functionality
- **Hotel Management**: CRUD operations, search by location/price/rating
- **Booking System**: Create, view, update, and cancel reservations
- **Availability Tracking**: Real-time room availability management
- **Multi-criteria Search**: Location, price range, rating, room count
- **Data Validation**: Comprehensive input validation and error handling

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0, Spring Data JPA
- **Database**: H2 (development), easily configurable for MySQL/PostgreSQL
- **Frontend**: HTML5, CSS3, JavaScript (ES6+), Bootstrap 5.3.0
- **Icons**: Font Awesome 6.0.0
- **Build Tool**: Maven
- **Validation**: Jakarta Validation API

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hotel-booking-system
   ```

2. **Build and run the application**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application**
   - **Main Website**: http://localhost:8081
   - **Chatbot Interface**: http://localhost:8081/chatbot
   - **Hotels Page**: http://localhost:8081/hotels-page
   - **Bookings Page**: http://localhost:8081/bookings-page
   - **API Documentation**: http://localhost:8081/api
   - **H2 Database Console**: http://localhost:8081/h2-console
     - JDBC URL: `jdbc:h2:mem:hotelbooking`
     - Username: `sa`
     - Password: (leave empty)

## 🎯 Usage Examples

### Chatbot Interactions

The chatbot understands natural language queries:

```
User: "Find hotels in Paris"
Bot: Lists available hotels in Paris with details

User: "Hotels under $200 with 4+ stars"
Bot: Shows filtered results matching criteria

User: "My bookings for john@email.com"
Bot: Displays all bookings for the email

User: "Cancel booking 123"
Bot: Cancels the specified booking
```

### API Endpoints

#### Hotels
- `GET /api/hotels` - Get all hotels
- `GET /api/hotels/{id}` - Get hotel by ID
- `GET /api/hotels/search?location=Paris&maxPrice=200` - Search hotels
- `POST /api/hotels` - Create new hotel
- `PUT /api/hotels/{id}` - Update hotel
- `DELETE /api/hotels/{id}` - Delete hotel

#### Bookings
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `GET /api/bookings/email/{email}` - Get bookings by email
- `POST /api/bookings` - Create new booking
- `POST /api/bookings/{id}/cancel` - Cancel booking
- `PUT /api/bookings/{id}` - Update booking

#### Chatbot
- `POST /api/chatbot/chat` - Send message to chatbot
- `GET /api/chatbot/help` - Get help information

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/ihg/hotelbooking/
│   │   ├── HotelBookingChatbotApplication.java
│   │   ├── config/
│   │   │   └── DataInitializer.java
│   │   ├── controller/
│   │   │   ├── BookingController.java
│   │   │   ├── ChatbotController.java
│   │   │   ├── HotelController.java
│   │   │   └── WebController.java
│   │   ├── entity/
│   │   │   ├── Booking.java
│   │   │   └── Hotel.java
│   │   ├── repository/
│   │   │   ├── BookingRepository.java
│   │   │   └── HotelRepository.java
│   │   └── service/
│   │       ├── BookingService.java
│   │       ├── ChatbotService.java
│   │       └── HotelService.java
│   └── resources/
│       ├── static/
│       │   ├── css/style.css
│       │   └── js/
│       │       ├── app.js
│       │       └── chatbot.js
│       ├── templates/
│       │   ├── index.html
│       │   ├── chatbot.html
│       │   ├── hotels.html
│       │   └── bookings.html
│       └── application.properties
```

## 🔧 Configuration

### Database Configuration
The application uses H2 in-memory database by default. To use a different database, update `application.properties`:

```properties
# For MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/hotelbooking
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# For PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/hotelbooking
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Server Configuration
```properties
server.port=8081
server.servlet.context-path=/api
```

## 🧪 Sample Data

The application comes pre-loaded with 8 sample hotels:
- Grand Palace Hotel (Paris) - $350/night, 5 stars
- Business Center Inn (New York) - $250/night, 4 stars
- Seaside Resort (Miami) - $200/night, 4 stars
- Mountain View Lodge (Aspen) - $180/night, 3 stars
- Budget Comfort Inn (London) - $120/night, 3 stars
- Royal Heritage Hotel (Jaipur) - $150/night, 5 stars
- Tokyo Business Tower (Tokyo) - $300/night, 4 stars
- Mediterranean Villa (Santorini) - $400/night, 5 stars

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
- Font Awesome for the beautiful icons
- H2 Database for the embedded database solution

## 📞 Support

If you encounter any issues or have questions:
1. Check the application logs for error details
2. Verify all dependencies are properly installed
3. Ensure Java 17+ is being used
4. Check that port 8081 is available

For additional help, please create an issue in the repository.

---

**Built with ❤️ using Java Spring Boot and modern web technologies**

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Application: http://localhost:8081
   - H2 Database Console: http://localhost:8081/api/h2-console
   - API Endpoints: http://localhost:8081/api

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/ihg/hotelbooking/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/             # Business Logic
│   │   ├── repository/          # Data Access Layer
│   │   ├── entity/              # JPA Entities
│   │   ├── config/              # Configuration Classes
│   │   └── HotelBookingChatbotApplication.java
│   └── resources/
│       ├── templates/           # Thymeleaf Templates
│       ├── static/             # CSS, JS, Images
│       └── application.properties
└── test/                       # Unit Tests
```

## 📡 API Endpoints

### Hotels
- `GET /api/hotels` - Get all hotels
- `GET /api/hotels/{id}` - Get hotel by ID
- `GET /api/hotels/search` - Search hotels with filters
- `POST /api/hotels` - Create new hotel
- `PUT /api/hotels/{id}` - Update hotel
- `DELETE /api/hotels/{id}` - Delete hotel

### Bookings
- `GET /api/bookings` - Get all bookings
- `GET /api/bookings/{id}` - Get booking by ID
- `POST /api/bookings` - Create new booking
- `POST /api/bookings/{id}/cancel` - Cancel booking
- `GET /api/bookings/email/{email}` - Get bookings by email

### Chatbot
- `POST /api/chatbot/chat` - Send message to chatbot
- `GET /api/chatbot/help` - Get help information

## 🤖 Chatbot Commands

The chatbot understands natural language queries such as:

### Hotel Search
- "Find hotels in Paris"
- "Search hotels under $200"
- "Hotels with 4+ stars"
- "Find 2 rooms in London"

### Booking Management
- "My bookings for john@email.com"
- "Check booking for Jane Smith"
- "Cancel booking 123"

### General
- "Help" or "What can you do?"
- "Book a room" (guided process)

## 🎯 Sample Data

The application comes pre-loaded with sample hotels in various locations:
- Grand Palace Hotel (Paris, France)
- Business Center Inn (New York, USA)
- Seaside Resort (Miami, USA)
- Mountain View Lodge (Aspen, USA)
- Budget Comfort Inn (London, UK)
- Royal Heritage Hotel (Jaipur, India)
- Tokyo Business Tower (Tokyo, Japan)
- Mediterranean Villa (Santorini, Greece)

## 🔧 Configuration

### Database Configuration
```properties
# H2 Database (Development)
spring.datasource.url=jdbc:h2:mem:hotelbooking
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

### Server Configuration
```properties
server.port=8081
server.servlet.context-path=/api
```

## 🧪 Testing

Run unit tests:
```bash
mvn test
```

## 📱 Usage Examples

### Searching Hotels via Chatbot
1. Navigate to the Chatbot page
2. Type: "Find hotels in Paris under $300"
3. View formatted results with booking options

### Making a Booking
1. Search for hotels
2. Click "Book Now" on desired hotel
3. Fill in guest information
4. Confirm booking

### Checking Bookings
1. Type: "My bookings for john@email.com"
2. View all bookings for the email
3. Cancel if needed

## 🎨 UI Features

- **Responsive Design**: Works on desktop, tablet, and mobile
- **Dark/Light Theme**: Automatic theme adaptation
- **Smooth Animations**: CSS transitions and animations
- **Loading States**: Visual feedback during API calls
- **Toast Notifications**: Real-time user feedback

## 🔮 Future Enhancements

- [ ] User authentication and authorization
- [ ] Payment integration
- [ ] Email notifications
- [ ] Advanced chatbot with ML/AI
- [ ] Multi-language support
- [ ] Hotel reviews and ratings
- [ ] Loyalty program
- [ ] Advanced reporting dashboard

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Support

For support and questions:
- Create an issue in the repository
- Contact the development team
- Check the documentation

---

Built with ❤️ using Spring Boot and modern web technologies.
